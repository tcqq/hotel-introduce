package com.management.roomates.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "billing_items")
data class BillingItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val amount: Double,
    val status: String,
    val lastUpdatedBy: String,
    val lastUpdatedAt: Long
)
