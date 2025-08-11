package com.dalima.paisawise.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import retrofit2.http.GET

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses")
    fun getAllExpenses(): LiveData<List<Expense>>

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY id DESC LIMIT 1")
    fun getLatestExpense(): LiveData<Expense?>

    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteExpenseById(expenseId: Int)

}
interface CurrencyApi {
    @GET("symbols")
    suspend fun getSymbols(): CurrencySymbolResponse
}
