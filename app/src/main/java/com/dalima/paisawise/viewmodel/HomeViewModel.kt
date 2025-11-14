package com.dalima.paisawise.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.dalima.paisawise.data.Expense
import com.dalima.paisawise.data.ExpenseDao
import com.dalima.paisawise.db.HomeUIState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
//import com.dalima.paisawise.BuildConfig
import com.dalima.paisawise.data.ChatMessage
import com.dalima.paisawise.data.ChatRequest
import com.dalima.paisawise.data.OpenAIClient
import io.ktor.client.request.invoke
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject

class HomeViewModel(private val expenseDao: ExpenseDao) : ViewModel() {

    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()

    private val selectedMonth = MutableLiveData(getCurrentMonthName())

    private val _aiReport=MutableLiveData<String>()
    val aiReport: LiveData<String> = _aiReport

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

        val currentMonthExpenses = expenses.filter {
            try {
                val parsedDate = sdf.parse(it.date)
                val cal = Calendar.getInstance().apply { time = parsedDate!! }
                cal.get(Calendar.MONTH) == selectedMonthNum && cal.get(Calendar.YEAR) == selectedYear
            } catch (_: Exception) {
                false
            }
        }

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

        val todayExpenses = currentMonthExpenses.filter {
            try {
                val parsedDate = sdf.parse(it.date)
                todayFormat.format(parsedDate!!) == todayDate
            } catch (_: Exception) {
                false
            }
        }

        val totalAmount = currentMonthExpenses.sumOf { it.amount }
        val todayAmount = todayExpenses.sumOf { it.amount }
        val lastTotal = lastMonthExpenses.sumOf { it.amount }

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
    fun generateAIReport(
        month: String,
        totalExpense: Double,
        categoryData: Map<String, Double>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiKey = "AIzaSyBMmHPttVZQc_R4tAE86_J8M0iRJ78CvXQ"
                val model = "gemini-2.5-flash"
                val prompt = buildString {
                    append("Generate a professional financial summary for the month of $month.\n")
                    append("Total expense: ₹$totalExpense\n")
                    append("Category-wise breakdown:\n")
                    categoryData.forEach { (category, amount) ->
                        append("- $category: ₹$amount\n")
                    }
                    append(
                        "\nProvide insights about spending habits, savings suggestions, " +
                                "and identify areas where expenses can be reduced. Keep it short and clear."
                    )
                }

                // ✅ Gemini expects this format
                val json = JSONObject().apply {
                    put(
                        "contents", JSONArray().put(
                            JSONObject().put(
                                "parts", JSONArray().put(
                                    JSONObject().put("text", prompt)
                                )
                            )
                        )
                    )
                }

                val client = OkHttpClient.Builder()
                    .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    .build()
                val requestBody =
                    RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())

                // ✅ Correct endpoint for AI Studio keys
                val request = Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey")
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody == null) {
                    _aiReport.postValue("Error ${response.code}: ${response.message}")
                    return@launch
                }

                val jsonResponse = JSONObject(responseBody)
                val text = jsonResponse
                    .optJSONArray("candidates")
                    ?.optJSONObject(0)
                    ?.optJSONObject("content")
                    ?.optJSONArray("parts")
                    ?.optJSONObject(0)
                    ?.optString("text") ?: "No report generated"

                _aiReport.postValue(text.trim())
            } catch (e: Exception) {
                _aiReport.postValue("Error generating report: ${e.message}")
            }
        }
    }
    fun resetAIReport() {
        _aiReport.postValue(null)   // Clear old summary
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
