package com.dalima.paisawise.transactionScreen

import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dalima.paisawise.data.Expense
import com.dalima.paisawise.data.ExpenseDao
import com.dalima.paisawise.viewmodel.HomeViewModel
import com.dalima.paisawise.viewmodel.HomeViewModelFactory
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.dalima.paisawise.AnalysisScreen
import com.dalima.paisawise.db.HomeUIState
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun TransactionScreen(navController: NavHostController, expenseDao: ExpenseDao,selectedMonth:String) {
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(expenseDao))
    val transactions by viewModel.allExpenses.observeAsState(emptyList())
    val uiState by viewModel.uiState.observeAsState(HomeUIState.NoExpenses)

    var selectedTab by remember { mutableStateOf("Day") }
    val tabs = listOf("Day", "Week", "Month", "Year")

    var selectedMonth by remember { mutableStateOf("May") }
    val months = listOf("Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    val balance = transactions.sumOf { it.amount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
            Card(
                shape = RoundedCornerShape(50),
                backgroundColor = Color.White,
                elevation = 6.dp,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                when (uiState) {
                    is HomeUIState.HasExpenses -> {
                        val state = uiState as HomeUIState.HasExpenses
//                        Text(
//                            text = "₹${state.totalAmount}",
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 16.sp,
//                            color = Color.Black,
//                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                        )
                    }

                    is HomeUIState.NoExpenses -> {
                        AnalysisScreen(
                            totalExpense = 0.0,
                            expensesByType = emptyMap(),
                            expenseDao = expenseDao,
                            selectedMonth = selectedMonth
                        )
                    }
                }
            }
        LineChartView(transactions, selectedTab, selectedMonth)

        // ---- Month Selector (abbreviations) ----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            months.forEach { month ->
                Text(
                    text = month,
                    color = if (month == selectedMonth) Color.Black else Color.Gray,
                    fontWeight = if (month == selectedMonth) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier
                        .clickable { selectedMonth = month }
                        .padding(vertical = 6.dp)
                )
            }
        }

        // ---- Tabs ----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                .padding(4.dp),
        ) {
            tabs.forEach { tab ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (tab == selectedTab) Color(0xFF4CAF50) else Color.Transparent)
                        .clickable { selectedTab = tab }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab,
                        color = if (tab == selectedTab) Color.White else Color.Gray,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ---- Transaction List ----
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(transactions) { _, transaction ->
                TransactionRow(transaction)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TransactionRow(expense: Expense) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = expense.categoryIconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                androidx.compose.material3.Text(
                    expense.category,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                androidx.compose.material3.Text(expense.date, fontSize = 12.sp, color = Color.Gray)
            }
        }
        androidx.compose.material3.Text(
            text = "₹${expense.amount}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Red
        )
    }
}
@Composable
fun LineChartView(transactions: List<Expense>, selectedTab:String, selectedMonth:String) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(vertical = 8.dp),
        factory={context->
            LineChart(context).apply{
//                layoutParams = ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT
//                )
                setBackgroundColor(android.graphics.Color.WHITE)
                description.isEnabled = false
                legend.isEnabled = false
                axisRight.isEnabled = false
                axisLeft.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    textColor = android.graphics.Color.GRAY
                    textSize = 10f
                }
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    granularity = 1f
                    textSize = 10f
                    isGranularityEnabled = true
                    textColor = android.graphics.Color.GRAY
                }
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(false)
                animateX(1200, Easing.EaseInOutQuad)
            }

        },
        update = { chart ->
            if (transactions.isEmpty()) {
                chart.clear()
                return@AndroidView
            }

            // 🔹 Filter data based on selected tab and selected month
            val filteredTransactions = when (selectedTab) {
                "Day" -> transactions.filter { getMonth(it.date) == selectedMonth }
                "Week" -> transactions.filter { getMonth(it.date) == selectedMonth }
                "Month" -> transactions.filter { getYear(it.date) == getCurrentYear() }
                "Year" -> transactions // all years
                else -> emptyList()
            }

            val grouped = when (selectedTab) {
                "Day" -> filteredTransactions
                    .groupBy { it.date } // group per date
                    .mapValues { it.value.sumOf { exp -> exp.amount } }
                    .toSortedMap(compareBy { parseDate(it) })

                "Week" -> filteredTransactions
                    .groupBy { getWeekOfMonth(it.date) }
                    .mapValues { it.value.sumOf { exp -> exp.amount } }
                    .toSortedMap()

                "Month" -> filteredTransactions
                    .groupBy { getMonth(it.date) }
                    .mapValues { it.value.sumOf { exp -> exp.amount } }
                    .toSortedMap(compareBy { monthOrder(it) })

                "Year" -> filteredTransactions
                    .groupBy { getYear(it.date) }
                    .mapValues { it.value.sumOf { exp -> exp.amount } }
                    .toSortedMap()

                else -> emptyMap()
            }

            if (grouped.isEmpty()) {
                chart.clear()
                return@AndroidView
            }
            val labels = grouped.keys.toList()
            val entries = labels.mapIndexed { index, key ->
                Entry(index.toFloat(), grouped[key]!!.toFloat())
            }


            // 🔹 Ensure curve always draws
            val safeEntries = if (entries.size == 1)
                entries + Entry(entries[0].x + 1f, entries[0].y)
            else entries

            val dataSet = LineDataSet(safeEntries, "Expenses").apply {
                color = android.graphics.Color.parseColor("#4CAF50")
                lineWidth = 3f
                setDrawCircles(true)
                setCircleColor(android.graphics.Color.parseColor("#4CAF50"))
                circleRadius = 5f
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
                cubicIntensity = 0.25f
                setDrawFilled(true)
                fillDrawable = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(
                        android.graphics.Color.parseColor("#A5D6A7"),
                        android.graphics.Color.TRANSPARENT
                    )
                )
            }

            chart.data = LineData(dataSet)
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(
                labels.map {
                    when (selectedTab) {
                        "Day" -> it.substring(0, 5) // show only "dd/MM"
                        "Week" -> it
                        "Month" -> it
                        "Year" -> it
                        else -> it
                    }
                }
            )
            chart.invalidate()
        }
    )
}
fun parseDate(dateStr: String): Long {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.parse(dateStr)?.time ?: 0L
}

fun monthOrder(month: String): Int {
    val months = listOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec")
    return months.indexOf(month)
}

fun getMonth(dateStr: String): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = sdf.parse(dateStr)
    return SimpleDateFormat("MMM", Locale.getDefault()).format(date!!)
}

fun getYear(dateStr: String): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = sdf.parse(dateStr)
    return SimpleDateFormat("yyyy", Locale.getDefault()).format(date!!)
}

fun getWeekOfMonth(dateStr: String): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = sdf.parse(dateStr)
    val cal = Calendar.getInstance()
    cal.time = date!!
    return "Week ${cal.get(Calendar.WEEK_OF_MONTH)}"
}
fun getCurrentYear(): String {
    return SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
}
