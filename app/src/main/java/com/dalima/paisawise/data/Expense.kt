package com.dalima.paisawise.data

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val categoryIconRes: Int,  // <-- new field to store icon
    val title: String,
    val amount: Double,
    val date: String,
    val description: String = "",
    val invoice: String = ""
)

data class CurrencySymbolResponse(
    val success: Boolean,
    val symbols: Map<String, CurrencyInfo>
)

data class CurrencyInfo(
    val description: String,
    val code: String
)
