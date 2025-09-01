package com.dalima.paisawise.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dalima.paisawise.PinStorage
import com.dalima.paisawise.R
import com.dalima.paisawise.category.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoryViewModel(application: Application): AndroidViewModel(application) {

    private val context= application.applicationContext
    private val _selectedTags = MutableStateFlow(PinStorage.getSelectedTags(context))
    val selectedTags = _selectedTags.asStateFlow()

    private val _selectedCategory = MutableLiveData<Pair<String, Int>?>()
    val selectedCategory: LiveData<Pair<String, Int>?> = _selectedCategory

    val categories = listOf(
        Category("Essentials & Living",
            listOf("Groceries", "Rent", "Utilities", "Internet", "Mobile", "Transportation", "Medical", "EMIs"),
            R.drawable.essentials),
        Category("Food & Dining",
            listOf("Restaurants", "Cafe", "Snacks", "Delivery", "Office Lunch", "Date", "Dinner"),
            R.drawable.food),
        Category("Entertainment & Leisure",
            listOf("Movies", "Events", "Gaming", "Outdoor", "Stand Up", "Subscriptions", "Show"),
            R.drawable.entertainment),
        Category("Shopping",
            listOf("Footwear", "Accessories", "Electronics", "Books", "Pens"),
            R.drawable.shopping),
        Category("Health & Wellness",
            listOf("Gym/Fitness", "Yoga/Meditation", "Medical Bills", "Medicines"),
            R.drawable.health),
        Category("Travel",
            listOf("Flight", "Train", "Hotel", "Stays", "Cabs", "Gifts", "Souvenir"),
            R.drawable.travels),
        Category("Family & Kids",
            listOf("School Fees", "Childcare", "Gifts/Toys", "Family Outings"),
            R.drawable.family),
        Category("Finance & Insurance",
            listOf("Credit Card Payments", "Insurance Premiums", "Investment Contributions", "Savings/FD"),
            R.drawable.finance),
        Category("Gifts & Donations",
            listOf("Festival Gifting", "Charity Donations", "Weddings/Birthdays"),
            R.drawable.gift),
        Category("Home & Maintenance",
            listOf("Repairs", "Furniture", "Cleaning Services"),
            R.drawable.success),
        Category("Work & Business",
            listOf("Office Supplies", "Business Travel", "Client Meetings"),
            R.drawable.work),
        Category("Education & Learning",
            listOf("Courses/Online Classes", "Books/Stationery", "Workshops"),
            R.drawable.education),
        Category("Others/Miscellaneous",
            listOf("Uncategorized", "Tips", "Cash Withdrawals"),
            R.drawable.others)
    )
    fun selectCategory(category: Category) {
        _selectedCategory.value = Pair(category.name, category.iconRes)
        PinStorage.saveSelectedCategory(getApplication(), category.name, category.iconRes)
    }

    fun toggleTag(tag: String) {
        val current = _selectedTags.value.toMutableList()
        if (tag in current) {
            current.remove(tag)
        } else {
            current.add(tag)
        }
        _selectedTags.value = current
        saveTags(current)
    }
    fun clearTags() {
        _selectedTags.value = emptyList()
        PinStorage.clearSelectedTags(context)
    }
    private fun saveTags(tags: List<String>) {
        PinStorage.saveSelectedTags(context, tags)
    }
}