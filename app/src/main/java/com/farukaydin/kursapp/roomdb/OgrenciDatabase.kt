package com.farukaydin.kursapp.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.farukaydin.kursapp.model.Ogrenci

@Database(entities = [Ogrenci::class], version = 2)
@TypeConverters(Converters::class)


abstract class OgrenciDatabase: RoomDatabase() {
    abstract fun ogrenciDao(): OgrenciDAO
}