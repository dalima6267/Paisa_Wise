package com.dalima.paisawise.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
@PrimaryKey(autoGenerate = true)
val id: Long = 0,
val name: String,
val type: String,
val amount: Double,
val date: String
)
