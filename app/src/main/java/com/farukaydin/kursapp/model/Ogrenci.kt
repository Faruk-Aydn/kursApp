package com.farukaydin.kursapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ogrenci (
    @ColumnInfo(name = "isim")
    var isim : String,
    @ColumnInfo(name = "yas")
    var yas  : String,
    @ColumnInfo(name = " telNo")
    var telNo : String,
    @ColumnInfo(name = "gorsel")
    var gorsel :ByteArray,
    @ColumnInfo(name = "dua")
    var dua: List<String> = emptyList(),
    @ColumnInfo(name = "cuz")
    var cuz: List<String> = emptyList(),
    @ColumnInfo(name = "kuran")
    var kuran: List<String> = emptyList()


){
    @PrimaryKey(autoGenerate = true)
    var id = 0
}
