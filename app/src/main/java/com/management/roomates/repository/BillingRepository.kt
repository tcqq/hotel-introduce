package com.management.roomates.repository

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.management.roomates.data.dao.BillingDao
import com.management.roomates.data.model.BillingItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillingRepository(private val billingDao: BillingDao) {

    private val firestore = FirebaseFirestore.getInstance()
    private var firestoreListener: ListenerRegistration? = null

    val allBillings: LiveData<List<BillingItem>> = billingDao.getAllBillings()

    init {
        firestoreListener = firestore.collection("billing_items")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                snapshots?.documentChanges?.forEach { documentChange ->
                    val billingItem = documentChange.document.toObject(BillingItem::class.java)
                    when (documentChange.type) {
                        com.google.firebase.firestore.DocumentChange.Type.ADDED -> {
                            insertToRoom(billingItem)
                        }
                        com.google.firebase.firestore.DocumentChange.Type.MODIFIED -> {
                            insertToRoom(billingItem)
                        }
                        com.google.firebase.firestore.DocumentChange.Type.REMOVED -> {
                            deleteFromRoom(billingItem)
                        }
                    }
                }
            }
    }

    private fun insertToRoom(billingItem: BillingItem) {
        kotlinx.coroutines.GlobalScope.launch(Dispatchers.IO) {
            billingDao.insertBilling(billingItem)
        }
    }

    private fun deleteFromRoom(billingItem: BillingItem) {
        kotlinx.coroutines.GlobalScope.launch(Dispatchers.IO) {
            billingDao.deleteBilling(billingItem)
        }
    }

    suspend fun insert(billingItem: BillingItem) {
        withContext(Dispatchers.IO) {
            billingDao.insertBilling(billingItem)
            firestore.collection("billing_items").document(billingItem.id.toString()).set(billingItem)
        }
    }

    suspend fun delete(billingItem: BillingItem) {
        withContext(Dispatchers.IO) {
            billingDao.deleteBilling(billingItem)
            firestore.collection("billing_items").document(billingItem.id.toString()).delete()
        }
    }

    fun cleanup() {
        firestoreListener?.remove()
    }
}
