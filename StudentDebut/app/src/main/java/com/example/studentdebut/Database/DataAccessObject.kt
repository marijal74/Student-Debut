package com.example.studentdebut.Database

import androidx.lifecycle.LiveData
import androidx.room.*


// interfejs za komunikaciju izmedju repozitorijuma i same baze
@Dao
interface DataAccessObject {

    // ignorise ako vec postoji u tabeli
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(job : List<jobItem>)

    // update i delete nigde nije korisceno, ostavila sam ako zatreba
    // insert, update i delete su podrazumevane fje, ne treba im telo
    @Update
    fun update(row : jobItem)

    @Delete
    fun delete(row : jobItem)

    // sve ostale potrebne fje za izvlacenje podataka iz tabele koristie @Query anotaciju i
    // prosledite mu sql upit

    @Query("SELECT * from jobs_table")
    fun getAllJobs(): LiveData<List<jobItem>>


    //TODO dodati Query fje za filtriranje i dodatne opcije
}