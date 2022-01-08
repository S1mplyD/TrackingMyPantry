package com.example.trackingmypantry.room_database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//Classe che contiene i campi di ogni prodotto
@Entity(tableName = "products_table")
data class Product(
    val nome: String,
    val descrizione: String,
    @PrimaryKey val barcode: String,
    val dataDiScadenza: Date?,
    val dataDiAcquisto: Date?,
    val categoria: String
)