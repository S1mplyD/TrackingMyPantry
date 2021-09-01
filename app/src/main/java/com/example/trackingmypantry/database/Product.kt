package com.example.trackingmypantry.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat

@Entity(tableName = "products_table")
data class Product(
    val nome: String,
    val descrizione: String,
    @PrimaryKey val barcode: String,
    //val dataDiScadenza: SimpleDateFormat,
    //val dataDiAcquisto: SimpleDateFormat,
    val categoria: String,
    val numeroProdotti: Int
)