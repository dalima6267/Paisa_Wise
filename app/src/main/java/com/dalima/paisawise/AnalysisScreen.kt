package com.dalima.paisawise

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dalima.paisawise.data.ExpenseDao
import com.dalima.paisawise.ui.theme.ButtonGreen
import com.dalima.paisawise.ui.theme.Lightred
import com.dalima.paisawise.viewmodel.HomeViewModel
import com.dalima.paisawise.viewmodel.HomeViewModelFactory
import androidx.compose.runtime.getValue
@Composable
fun AnalysisScreen(
    analysisSummary: String,
    expensesByType: Map<String, Float>,
    onDownloadClick: () -> Unit,
    onPdfGenerateClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text("Expense Analysis Chart", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(14.dp))

        AndroidView(
            factory = { context ->
                PieChart(context).apply {
                    description.isEnabled = false
                    setUsePercentValues(false)
                    setDrawEntryLabels(true)
                    setEntryLabelColor(Color.BLACK)
                    setEntryLabelTextSize(12f)
                    centerText = "Expense Breakdown"
                    setCenterTextSize(18f)
                    isRotationEnabled = true

                    legend.isEnabled = true
                    legend.textSize = 14f
                    legend.formSize = 14f
                }
            },
            update = { chart ->
                val entries = expensesByType.map { (category, total) ->
                    PieEntry(total, category) // Only category, no amount
                }

                val dataSet = PieDataSet(entries, "").apply {
                    colors = ColorTemplate.MATERIAL_COLORS.toList()
                    sliceSpace = 3f
                    selectionShift = 5f
                    valueTextColor = Color.BLACK
                    valueTextSize = 14f
                    setDrawValues(false) // ✅ Hides value numbers from pie slices
                }

                val pieData = PieData(dataSet)
                pieData.setDrawValues(false) // ✅ Optional, double assurance

                chart.data = pieData
                chart.invalidate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Analysis Summary", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Button(
                onClick = onDownloadClick,
                colors = ButtonDefaults.buttonColors(ButtonGreen)
            ) {
                Text("Download", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = analysisSummary,
            modifier = Modifier
                .fillMaxWidth()
                .background(ComposeColor.LightGray, RoundedCornerShape(8.dp))
                .padding(12.dp),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(214.dp))

        Button(
            onClick = onPdfGenerateClick,
            colors = ButtonDefaults.buttonColors(Lightred),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Get this report in PDF", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AnalysisScreenRoute(expenseDao: ExpenseDao) {
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(expenseDao))

    // ✅ specify type explicitly for safety
    val categoryExpenses: Map<String, Float> by viewModel.expensesByCategory.observeAsState(initial = emptyMap())

    val summary = remember(categoryExpenses) {
        if (categoryExpenses.isNotEmpty()) {
            val maxCategoryEntry = categoryExpenses.maxByOrNull { it.value }
            "This month, ${maxCategoryEntry?.key} made up the largest share of your expenses."
        } else {
            "No expenses available to analyze."
        }
    }

    AnalysisScreen(
        analysisSummary = summary,
        expensesByType = categoryExpenses,
        onDownloadClick = {  },
        onPdfGenerateClick = {  }
    )
}

@Composable
@Preview(showBackground = true)
fun SampleChartUsage() {
    val sampleData = mapOf(
        "Food" to 4500f,
        "Travel" to 2000f,
        "Entertainment" to 1500f,
        "Bills" to 1000f,
        "Essential" to 3500f,
        "Shopping" to 1000f,
        "School" to 1500f
        // "Bills" duplicate removed
    )
    val summary = "This month, Food made up the largest share of your expenses."

    AnalysisScreen(
        analysisSummary = summary,
        expensesByType = sampleData,
        onDownloadClick = { /* TODO */ },
        onPdfGenerateClick = { /* TODO */ }
    )
}
