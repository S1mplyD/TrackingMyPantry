package com.example.trackingmypantry.room_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//Database locale
@Database(entities = [Product::class], version = 1, exportSchema = false)
//Convertitore delle date
@TypeConverters(TypeConverter::class)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun ProductDao(): ProductDao
    //Creo un companion object con l'istanza del database
    companion object {
        @Volatile
        private var INSTANCE: ProductDatabase? = null
        //Funzione che ritorna l'istanza del database
        fun getDatabase(context: Context): ProductDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                //Creo l'istanza del database
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDatabase::class.java,
                    "products_table"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}