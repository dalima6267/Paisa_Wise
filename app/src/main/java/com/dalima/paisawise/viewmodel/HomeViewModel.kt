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

    private val selectedMonth = MutableLiveData(getCurrentMonthName())

    val uiState: LiveData<HomeUIState> = MediatorLiveData<HomeUIState>().apply {
        fun updateState(expenses: List<Expense>?, month: String?) {
            value = if (expenses.isNullOrEmpty()) {
                HomeUIState.NoExpenses
            } else {
                getUiStateFromExpenses(expenses, month ?: getCurrentMonthName())
            }
        }

        addSource(allExpenses) { expenses ->
            updateState(expenses, selectedMonth.value)
        }

        addSource(selectedMonth) { month ->
            updateState(allExpenses.value, month)
        }
    }

    val expensesByCategory: LiveData<Map<String, Double>> = MediatorLiveData<Map<String, Double>>().apply {
        fun updateCategory(expenses: List<Expense>?, month: String?) {
            value = expenses
                ?.let { filterExpensesByMonth(it, month ?: getCurrentMonthName()) }
                ?.groupBy { it.category }
                ?.mapValues { entry -> entry.value.sumOf { exp -> exp.amount } }
                ?: emptyMap()
        }

        addSource(allExpenses) { expenses ->
            updateCategory(expenses, selectedMonth.value)
        }

        addSource(selectedMonth) { month ->
            updateCategory(allExpenses.value, month)
        }
    }

    fun setSelectedMonth(month: String) {
        selectedMonth.value = month
    }

    private fun filterExpensesByMonth(expenses: List<Expense>, selectedMonth: String): List<Expense> {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val monthNameFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        return expenses.filter {
            try {
                val date = sdf.parse(it.date)
                date != null && monthNameFormat.format(date).equals(selectedMonth, ignoreCase = true)
            } catch (_: Exception) {
                false
            }
        }
    }

    fun getUiStateFromExpenses(expenses: List<Expense>, selectedMonth: String): HomeUIState.HasExpenses {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val monthNameFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        val todayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val todayDate = todayFormat.format(Date())

        // Parse selected month (e.g., "November") into calendar
        val selectedMonthCalendar = Calendar.getInstance().apply {
            time = Date()
            val parsedMonth = monthNameFormat.parse(selectedMonth)
            if (parsedMonth != null) {
                val tempCal = Calendar.getInstance().apply { time = parsedMonth }
                set(Calendar.MONTH, tempCal.get(Calendar.MONTH))
            }
        }

        val selectedMonthNum = selectedMonthCalendar.get(Calendar.MONTH)
        val selectedYear = selectedMonthCalendar.get(Calendar.YEAR)

        // ✅ 1. Filter current month
        val currentMonthExpenses = expenses.filter {
            try {
                val parsedDate = sdf.parse(it.date)
                val cal = Calendar.getInstance().apply { time = parsedDate!! }
                cal.get(Calendar.MONTH) == selectedMonthNum && cal.get(Calendar.YEAR) == selectedYear
            } catch (_: Exception) {
                false
            }
        }

        // ✅ 2. Filter last month (handles year transitions properly)
        val lastMonthCalendar = (selectedMonthCalendar.clone() as Calendar).apply {
            add(Calendar.MONTH, -1)
        }
        val lastMonthNum = lastMonthCalendar.get(Calendar.MONTH)
        val lastMonthYear = lastMonthCalendar.get(Calendar.YEAR)

        val lastMonthExpenses = expenses.filter {
            try {
                val parsedDate = sdf.parse(it.date)
                val cal = Calendar.getInstance().apply { time = parsedDate!! }
                cal.get(Calendar.MONTH) == lastMonthNum && cal.get(Calendar.YEAR) == lastMonthYear
            } catch (_: Exception) {
                false
            }
        }

        // ✅ 3. Filter today’s expenses (ensure same string pattern)
        val todayExpenses = currentMonthExpenses.filter {
            try {
                val parsedDate = sdf.parse(it.date)
                todayFormat.format(parsedDate!!) == todayDate
            } catch (_: Exception) {
                false
            }
        }

        // ✅ 4. Sum amounts
        val totalAmount = currentMonthExpenses.sumOf { it.amount }
        val todayAmount = todayExpenses.sumOf { it.amount }
        val lastTotal = lastMonthExpenses.sumOf { it.amount }

        // ✅ 5. Comparison text
        val comparisonText = when {
            lastTotal == 0.0 && totalAmount > 0 -> "Compared to last month: +100%"
            lastTotal == 0.0 && totalAmount == 0.0 -> "No comparison available"
            else -> {
                val diff = totalAmount - lastTotal
                val percent = (diff / lastTotal) * 100
                if (percent >= 0) "Compared to last month: +${"%.1f".format(percent)}%"
                else "Compared to last month: ${"%.1f".format(percent)}%"
            }
        }

        // ✅ 6. Highest category
        val highestCategory = if (currentMonthExpenses.isNotEmpty()) {
            currentMonthExpenses
                .groupBy { it.category }
                .mapValues { it.value.sumOf { exp -> exp.amount } }
                .maxByOrNull { it.value }?.key ?: "N/A"
        } else "N/A"

        return HomeUIState.HasExpenses(
            totalAmount = totalAmount,
            lastMonthAmount = lastTotal,
            todayAmount = todayAmount,
            comparisonText = comparisonText,
            highestCategory = highestCategory,
            currentMonthName = selectedMonth
        )
    }


    private fun getCurrentMonthName(): String {
        return SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())
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
