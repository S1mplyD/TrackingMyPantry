package com.example.trackingmypantry.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("SELECT * FROM products_table")
    fun getAll(): LiveData<List<Product>>

    @Query("SELECT * FROM products_table WHERE nome= :name")
    fun getByName(name: String): List<Product>

    @Query("SELECT * FROM products_table WHERE barcode= :barcode")
    fun getByBarcode(barcode : String): List<Product>

    @Insert
    fun insert(vararg product: Product)

    @Delete
    fun delete(product: Product)
}