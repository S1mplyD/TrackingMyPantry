package com.example.trackingmypantry.room_database

import androidx.room.TypeConverter
import java.util.*

//Classe che converte il formato Date in Long
class TypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}