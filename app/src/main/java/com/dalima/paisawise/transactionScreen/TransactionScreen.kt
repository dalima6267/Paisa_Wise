package com.dalima.paisawise.transactionScreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dalima.paisawise.data.Expense
import com.dalima.paisawise.data.ExpenseDao
import com.dalima.paisawise.viewmodel.HomeViewModel
import com.dalima.paisawise.viewmodel.HomeViewModelFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.runtime.setValue

@Composable
fun TransactionScreen(navController: NavHostController, expenseDao: ExpenseDao) {
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(expenseDao))
    val transactions by viewModel.allExpenses.observeAsState(emptyList())

    var selectedTab by remember { mutableStateOf("Day") }
    val tabs = listOf("Day", "Week", "Month", "Year")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // ---- Balance + Chart Section ----
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                backgroundColor = Color.White,
                elevation = 6.dp,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                val balance = transactions.sumOf { it.amount }
                Text(
                    text = "₹$balance",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            // Chart Placeholder (replace with real chart lib like MPAndroidChart)
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                drawArc(
                    color = Color(0xFF4CAF50),
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = false,
                    style = Stroke(width = 6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---- Tabs ----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
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

        Spacer(modifier = Modifier.height(16.dp))

        // ---- Transaction List ----
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(transactions) { index, transaction ->
                TransactionRow(transaction)
                Divider()
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
