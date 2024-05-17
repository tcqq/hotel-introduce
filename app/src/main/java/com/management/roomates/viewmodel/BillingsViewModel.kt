package com.management.roomates.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.management.roomates.data.AppDatabase
import com.management.roomates.data.model.BillingItem
import com.management.roomates.repository.BillingRepository
import kotlinx.coroutines.launch

class BillingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BillingRepository
    val allBillings: LiveData<List<BillingItem>>

    init {
        val billingDao = AppDatabase.getDatabase(application).billingDao()
        repository = BillingRepository(billingDao)
        allBillings = repository.allBillings
    }

    fun insert(billingItem: BillingItem) = viewModelScope.launch {
        repository.insert(billingItem)
    }

    fun delete(billingItem: BillingItem) = viewModelScope.launch {
        repository.delete(billingItem)
    }

    override fun onCleared() {
        super.onCleared()
        repository.cleanup()
    }
}
