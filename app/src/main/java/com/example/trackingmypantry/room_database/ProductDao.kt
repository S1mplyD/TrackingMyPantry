package com.example.trackingmypantry.room_database

import androidx.lifecycle.LiveData
import androidx.room.*

//Interfaccia per accedere alle funzioni del database
@Dao
interface ProductDao {

    //Funzione che ritorna tutti gli elementi del database
    @Query("SELECT * FROM products_table")
    fun getAll(): LiveData<List<Product>>

    //Funzione che ritorna un prodotto se ha un nome o barcode simile alla stringa passata come parametro
    @Query("SELECT * FROM products_table WHERE nome LIKE :searchQuery OR barcode LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<Product>>

    //Funzione che ritorna un prodotto in base alla categoria
    @Query("SELECT * FROM products_table WHERE categoria=:category ")
    fun searchByCategory(category: String): LiveData<List<Product>>

    //Funzione che inserisce un prodotto nel database, se il prodotto esiste già lo sostituisce
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg product: Product)

    //Funzione che cancella un prodotto da un database
    @Delete
    fun delete(product: Product)
}