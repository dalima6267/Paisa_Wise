package com.dalima.paisawise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.dalima.paisawise.data.Expense
import com.dalima.paisawise.data.ExpenseDao
import com.dalima.paisawise.db.HomeUIState
import kotlinx.coroutines.launch

class HomeViewModel(private val expenseDao: ExpenseDao) : ViewModel() {

    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()

    val totalAmount: LiveData<Double> = allExpenses.map { list -> list.sumOf { it.amount } }


    val uiState: LiveData<HomeUIState> = allExpenses.map { list ->
        if (list.isEmpty()) {
            HomeUIState.NoExpenses
        } else {
            HomeUIState.HasExpenses(
                totalAmount = list.sumOf { it.amount },
                comparisonText = "\u20B9200 more than last month" // can replace with logic
            )
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            expenseDao.insertExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            expenseDao.deleteExpenseById(expense.id.toInt())
        }
    }
}
class HomeViewModelFactory(private val expenseDao: ExpenseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(expenseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

