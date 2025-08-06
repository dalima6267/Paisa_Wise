package com.dalima.paisawise.viewmodel

import androidx.lifecycle.*
import com.dalima.paisawise.data.Expense
import com.dalima.paisawise.data.ExpenseDao
import com.dalima.paisawise.db.HomeUIState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(private val expenseDao: ExpenseDao) : ViewModel() {

    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()

    val uiState: LiveData<HomeUIState> = allExpenses.map { expenses ->
        if (expenses.isEmpty()) {
            HomeUIState.NoExpenses
        } else {
            calculateMonthlyExpenseState(expenses)
        }
    }

    private fun calculateMonthlyExpenseState(expenses: List<Expense>): HomeUIState.HasExpenses {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val monthFormat = SimpleDateFormat("MM-yyyy", Locale.getDefault())
        val currentMonthName = SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())

        val currentMonthKey = monthFormat.format(Date())
        val lastMonthCalendar = Calendar.getInstance().apply { add(Calendar.MONTH, -1) }
        val lastMonthKey = monthFormat.format(lastMonthCalendar.time)

        val currentMonthExpenses = expenses.filter {
            try {
                val parsedDate = sdf.parse(it.date)
                parsedDate != null && monthFormat.format(parsedDate) == currentMonthKey
            } catch (e: Exception) {
                false
            }
        }

        val lastMonthExpenses = expenses.filter {
            try {
                val parsedDate = sdf.parse(it.date)
                parsedDate != null && monthFormat.format(parsedDate) == lastMonthKey
            } catch (e: Exception) {
                false
            }
        }

        val currentTotal = currentMonthExpenses.sumOf { it.amount }
        val lastTotal = lastMonthExpenses.sumOf { it.amount }

        val comparisonText = when {
            lastTotal == 0.0 && currentTotal > 0 -> "Compared to last month: +100%"
            lastTotal == 0.0 && currentTotal == 0.0 -> "No comparison available"
            else -> {
                val diff = currentTotal - lastTotal
                val percent = (diff / lastTotal) * 100
                if (percent >= 0)
                    "Compared to last month: +${"%.1f".format(percent)}%"
                else
                    "Compared to last month: ${"%.1f".format(percent)}%"
            }
        }

        // (Optional) Highest category
        val highestCategory = currentMonthExpenses
            .groupBy { it.category }
            .mapValues { it.value.sumOf { exp -> exp.amount } }
            .maxByOrNull { it.value }?.key ?: "N/A"

        return HomeUIState.HasExpenses(
            totalAmount = currentTotal,
            comparisonText = comparisonText,
            highestCategory = highestCategory,
            currentMonthName = currentMonthName
        )

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
