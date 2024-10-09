package com.farukaydin.kursapp.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.farukaydin.kursapp.model.Ogrenci
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable


@Dao
interface OgrenciDAO  {
    @Query("SELECT * FROM Ogrenci")
    fun getall() : Flowable<List<Ogrenci>>

    @Query("SELECT * FROM Ogrenci WHERE id = :id")
    fun findById(id : Int) : Flowable<Ogrenci>

    @Insert
    fun insert(ogrenci: Ogrenci) : Completable

    @Delete
    fun delete(ogrenci: Ogrenci) : Completable

    @Update
    fun update (ogrenci: Ogrenci) : Completable


}