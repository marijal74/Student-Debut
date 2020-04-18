package com.example.studentdebut.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface DataAccessObject {

    @Insert
    fun insert(row : Entity)

    @Update
    fun update(row : Entity)

    @Delete
    fun delete(row : Entity)

    //TODO dodati Query fje za filtriranje i dodatne opcije
}