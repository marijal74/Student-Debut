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
   // @Query("SELECT * FROM jobs_table WHERE job IN (:filtersJob) " + "OR position IN (:filtersPosition) " + "OR EXISTS (SELECT * " +
           // "FROM STRING_SPLIT(languages, '_') " + "WHERE value IN (:filtersLanguage))")

    @Query("WITH split(word, str) AS ( " +
                 "SELECT '', language FROM jobs_table " +
                 "UNION ALL " +
                 "SELECT substr(str, 0, instr(str, '_')), substr(str, instr(str, '_')+1) " +
                 "FROM split WHERE str!='') " +
                 "SELECT * FROM jobs_table WHERE job IN (:filtersJob) " + "AND position IN (:filtersPosition) " + "AND EXISTS (SELECT * "+
                      "FROM split " + "WHERE word!='' AND word IN (:filtersLanguage))")
    fun applyFilters(filtersJob: MutableList<String>, filtersPosition: MutableList<String>, filtersLanguage: MutableList<String>): LiveData<List<jobItem>>

}