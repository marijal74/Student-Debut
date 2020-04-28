package com.example.studentdebut.Database

import androidx.lifecycle.LiveData
import androidx.room.*


// interfejs za komunikaciju izmedju repozitorijuma i same baze
@Dao
interface DataAccessObject {

    // ignorise ako vec postoji u tabeli
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job : jobItem)

    // update i delete nigde nije korisceno, ostavila sam ako zatreba
    // insert, update i delete su podrazumevane fje, ne treba im telo
    @Update
    fun update(job : jobItem)

    @Delete
    fun delete(job : jobItem)

    // sve ostale potrebne fje za izvlacenje podataka iz tabele koristie @Query anotaciju i
    // prosledite mu sql upit

    @Query("SELECT * from jobs_table")
    fun getAllJobs(): LiveData<List<jobItem>>

    @Query("DELETE FROM jobs_table")
    fun deleteEverything()


    //TODO dodati Query fje za filtriranje i dodatne opcije
    @Query("SELECT * FROM jobs_table WHERE job IN (:filtersJob) " + "OR position IN (:filtersPosition) " + "OR language IN (:filtersLanguage)")
    fun applyFilters(filtersJob: MutableList<String>, filtersPosition: MutableList<String>, filtersLanguage: MutableList<String>): LiveData<List<jobItem>>

}