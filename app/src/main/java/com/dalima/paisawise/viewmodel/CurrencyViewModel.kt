package com.dalima.paisawise.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dalima.paisawise.db.RetrofitClient
import kotlinx.coroutines.launch
import androidx.compose.runtime.setValue

class CurrencyViewModel : ViewModel() {
    var currencies by mutableStateOf<Map<String, String>>(emptyMap())
        private set

    var selectedCurrency by mutableStateOf("INR") // default selection

    init {
        fetchCurrencies()
    }

    private fun fetchCurrencies() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getSymbols()
                if (response.success) {
                    currencies = response.symbols.mapValues { it.value.description }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun selectCurrency(code: String) {
        selectedCurrency = code
    }
}
