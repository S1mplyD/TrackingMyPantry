package com.example.trackingmypantry.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {

    @Query("SELECT * FROM products_table")
    fun getAll(): LiveData<List<Product>>

    @Query("SELECT * FROM products_table WHERE nome LIKE :searchQuery OR barcode LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg product: Product)

    @Delete
    fun delete(product: Product)
}