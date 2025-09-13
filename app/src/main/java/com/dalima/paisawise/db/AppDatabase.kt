package com.dalima.paisawise.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dalima.paisawise.data.Expense
import com.dalima.paisawise.data.ExpenseDao

@Database(entities = [Expense::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
companion object{
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "expense_db"
            )
                .fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
        }
    }
}
}

sealed class HomeUIState {
    object NoExpenses : HomeUIState()
    data class HasExpenses(
        val totalAmount: Double,
        val lastMonthAmount: Double,
        val todayAmount: Double,
        val comparisonText: String,
        val highestCategory: String,
        val currentMonthName: String
    ) : HomeUIState()
}
