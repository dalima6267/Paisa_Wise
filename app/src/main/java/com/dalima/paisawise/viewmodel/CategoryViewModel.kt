package com.dalima.paisawise.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dalima.paisawise.PinStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoryViewModel(application: Application): AndroidViewModel(application) {

    private val context= application.applicationContext
    private val _selectedTags = MutableStateFlow(PinStorage.getSelectedTags(context))
    val selectedTags = _selectedTags.asStateFlow()

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