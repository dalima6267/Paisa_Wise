package com.dalima.paisawise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dalima.paisawise.data.ExpenseDao
import com.dalima.paisawise.db.HomeUIState
import kotlinx.coroutines.launch

class HomeViewModel(private val expenseDao: ExpenseDao) : ViewModel() {
    private val _uiState = MutableLiveData<HomeUIState>(HomeUIState.NoExpenses)
    val uiState: LiveData<HomeUIState> = _uiState

    init {
        getExpenses()
    }

    private fun getExpenses() {
        viewModelScope.launch {
            val expenses = expenseDao.getAllExpenses()
            if (expenses.isEmpty()) {
                _uiState.postValue(HomeUIState.NoExpenses)
            } else {
                val total = expenses.sumOf { it.amount }
                _uiState.postValue(
                    HomeUIState.HasExpenses(
                        totalAmount = total,
                        comparisonText = "\u20B9200 more than last month"
                    )
                )
            }
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
