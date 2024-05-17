package com.management.roomates.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.management.roomates.data.AppDatabase
import com.management.roomates.data.model.GroceryItem
import com.management.roomates.repository.GroceryRepository
import kotlinx.coroutines.launch

class GroceriesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GroceryRepository
    val allGroceries: LiveData<List<GroceryItem>>

    init {
        val groceryDao = AppDatabase.getDatabase(application).groceryDao()
        repository = GroceryRepository(groceryDao)
        allGroceries = repository.allGroceries
    }

    fun insert(groceryItem: GroceryItem) = viewModelScope.launch {
        repository.insert(groceryItem)
    }

    fun delete(groceryItem: GroceryItem) = viewModelScope.launch {
        repository.delete(groceryItem)
    }

    override fun onCleared() {
        super.onCleared()
        repository.cleanup()
    }
}
