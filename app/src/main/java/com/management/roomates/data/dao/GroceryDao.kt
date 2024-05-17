package com.management.roomates.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.management.roomates.data.model.GroceryItem

@Dao
interface GroceryDao {
    @Query("SELECT * FROM grocery_items ORDER BY lastUpdatedAt DESC")
    fun getAllGroceries(): LiveData<List<GroceryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrocery(groceryItem: GroceryItem)

    @Delete
    suspend fun deleteGrocery(groceryItem: GroceryItem)
}
