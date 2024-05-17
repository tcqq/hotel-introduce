package com.management.roomates.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.management.roomates.data.model.BillingItem

@Dao
interface BillingDao {
    @Query("SELECT * FROM billing_items")
    fun getAllBillings(): LiveData<List<BillingItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBilling(billingItem: BillingItem)

    @Delete
    suspend fun deleteBilling(billingItem: BillingItem)
}
