package com.dalima.paisawise.transactionScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.navigation.NavHostController

@Composable
fun TransactionScreen(navController: NavHostController,expenseDao: ExpenseDao) {
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(expenseDao))
    val transactions by viewModel.allExpenses.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(transactions) { index: Int, transaction: Expense ->
                TransactionCardItem(
                    expense = transaction,
                    onDelete = {
                        viewModel.deleteExpense(transaction)
                    },
                    backgroundColor = cardColors[index % cardColors.size]
                )
            }
        }
    }
}
