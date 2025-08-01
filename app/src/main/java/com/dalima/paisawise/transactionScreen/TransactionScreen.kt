package com.dalima.paisawise.transactionScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dalima.paisawise.R

data class Transaction(
    val category: String,
    val subcategory: String,
    val date: String,
    val amount: String,
    val icon: Int
)

@Composable
fun TransactionScreen() {
    val transactions = remember {
        listOf(
            Transaction("Groceries", "Bills","2025-07-17", "₹1,250", R.drawable.food),
            Transaction("Transport", "Bills","2025-07-16", "₹300", R.drawable.food),
            Transaction("Bills", "Bills","2025-07-15", "₹2,100", R.drawable.shopping),
            Transaction("Entertainment", "Fun","2025-07-14", "₹800", R.drawable.food),
            Transaction("Dining", "Restaurant","2025-07-13", "₹650", R.drawable.shopping),
            Transaction("Health", "Pharmacy","2025-07-12", "₹900", R.drawable.food),
            Transaction("Travel", "Flight","2025-07-11", "₹7,000", R.drawable.shopping)
        )
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(58.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(transactions) { index, transaction ->
                TransactionCardItem(
                    icon = transaction.icon,
                    category = transaction.category,
                    subcategory = transaction.subcategory,
                    date = transaction.date,
                    amount = transaction.amount,
                    onDelete = { /* handle deletion */ },
                    backgroundColor = cardColors[index % cardColors.size]
                )
            }
        }
    }}
@Preview(showBackground = true)
@Composable
fun TransactionScreenPreview() {
    TransactionScreen()
}

