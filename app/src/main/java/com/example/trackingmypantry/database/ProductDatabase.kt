package com.example.trackingmypantry.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trackingmypantry.ProductData
import kotlinx.coroutines.CoroutineScope
import java.time.chrono.HijrahChronology.INSTANCE

@Database(entities = [Product::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun ProductDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: ProductDatabase? = null

        fun getDatabase(context: Context): ProductDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
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