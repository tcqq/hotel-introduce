package com.management.roomates.repository

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.management.roomates.data.dao.GroceryDao
import com.management.roomates.data.model.GroceryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroceryRepository(private val groceryDao: GroceryDao) {

    private val firestore = FirebaseFirestore.getInstance()
    private var firestoreListener: ListenerRegistration? = null

    val allGroceries: LiveData<List<GroceryItem>> = groceryDao.getAllGroceries()

    init {
        firestoreListener = firestore.collection("grocery_items")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                snapshots?.documentChanges?.forEach { documentChange ->
                    val groceryItem = documentChange.document.toObject(GroceryItem::class.java)
                    when (documentChange.type) {
                        com.google.firebase.firestore.DocumentChange.Type.ADDED -> {
                            insertToRoom(groceryItem)
                        }
                        com.google.firebase.firestore.DocumentChange.Type.MODIFIED -> {
                            insertToRoom(groceryItem)
                        }
                        com.google.firebase.firestore.DocumentChange.Type.REMOVED -> {
                            deleteFromRoom(groceryItem)
                        }
                    }
                }
            }
    }

    private fun insertToRoom(groceryItem: GroceryItem) {
        kotlinx.coroutines.GlobalScope.launch(Dispatchers.IO) {
            groceryDao.insertGrocery(groceryItem)
        }
    }

    private fun deleteFromRoom(groceryItem: GroceryItem) {
        kotlinx.coroutines.GlobalScope.launch(Dispatchers.IO) {
            groceryDao.deleteGrocery(groceryItem)
        }
    }

    suspend fun insert(groceryItem: GroceryItem) {
        withContext(Dispatchers.IO) {
            groceryDao.insertGrocery(groceryItem)
            firestore.collection("grocery_items").document(groceryItem.id.toString()).set(groceryItem)
        }
    }

    suspend fun delete(groceryItem: GroceryItem) {
        withContext(Dispatchers.IO) {
            groceryDao.deleteGrocery(groceryItem)
            firestore.collection("grocery_items").document(groceryItem.id.toString()).delete()
        }
    }

    fun cleanup() {
        firestoreListener?.remove()
    }
}
