package com.example.trackingmypantry.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "products_table")
data class Product(
    val nome: String,
    val descrizione: String,
    @PrimaryKey val barcode: String,
    val dataDiScadenza: Date,
    val dataDiAcquisto: Date,
    val categoria: String,
    val numeroProdotti: Int
)