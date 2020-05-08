package com.example.studentdebut.Database

import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.studentdebut.MyApp.Companion.filtersLanguage
import com.example.studentdebut.MyApp.Companion.filtersJob
import com.example.studentdebut.MyApp.Companion.filtersPosition


// interfejs za komunikaciju izmedju repozitorijuma i same baze
@Dao
interface DataAccessObject {


    @Query("SELECT * FROM jobs_table")
    fun initializeDb(): List<jobItem>

    // ignorise ako vec postoji u tabeli
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job : List<jobItem>)

    // update i delete nigde nije korisceno, ostavila sam ako zatreba
    // insert, update i delete su podrazumevane fje, ne treba im telo
    @Update
    fun update(job : jobItem)

    @Delete
    fun delete(job : jobItem)

    // sve ostale potrebne fje za izvlacenje podataka iz tabele koristie @Query anotaciju i
    // prosledite mu sql upit

    /*@Query("SELECT DISTINCT * from jobs_table WHERE job IN (:filtersJob) AND position IN (:filtersPosition)")
    fun getAllJobs(
        filtersJob: MutableList<String>,
        filtersPosition: MutableList<String>
    ): MutableList<jobItem>*/


    @Query("DELETE FROM jobs_table")
    fun deleteEverything()


    //TODO dodati Query fje za filtriranje i dodatne opcije
   // @Query("SELECT * FROM jobs_table WHERE job IN (:filtersJob) " + "OR position IN (:filtersPosition) " + "OR EXISTS (SELECT * " +
           // "FROM STRING_SPLIT(languages, '_') " + "WHERE value IN (:filtersLanguage))")

   /* @Query("WITH split(word, str) AS ( " +
                 "SELECT '', language FROM jobs_table " + "WHERE job IN (:filtersJob) " + "AND position IN (:filtersPosition) " +
                 "UNION ALL " +
                 "SELECT substr(str, 0, instr(str, '_')), substr(str, instr(str, '_')+1) " +
                 "FROM split WHERE str!='' AND ) " +
                 "SELECT * FROM jobs_table WHERE job IN (:filtersJob) " + "AND position IN (:filtersPosition) " + "AND EXISTS (SELECT * "+
                      "FROM split " + "WHERE word!='' AND word IN (:filtersLanguage))")
    fun applyFilters(filtersJob: MutableList<String>, filtersPosition: MutableList<String>, filtersLanguage: MutableList<String>): LiveData<List<jobItem>>*/
    @Query("Select COUNT(*) from jobs_table")
    fun velicinaBaze():Double

    //TODO ispraviti upit
    @Query("SELECT DISTINCT * FROM jobs_table WHERE  job IN (:filtersJob) AND position IN (:filtersPosition) AND instr(language, (:lang)) > 0 ")
    fun singleLanguage(
        lang: String,
        filtersJob: MutableList<String>,
        filtersPosition: MutableList<String>
    ) : MutableList<jobItem>

    @Query("SELECT DISTINCT * FROM jobs_table WHERE job IN (:filtersJob)")
    fun getOnlyJobs(filtersJob: MutableList<String>) : MutableList<jobItem>

    @Query("SELECT DISTINCT * FROM jobs_table WHERE position IN (:filtersPosition)")
    fun getOnlyPosition(filtersPosition: MutableList<String>) : MutableList<jobItem>

    @Query("SELECT DISTINCT * FROM jobs_table WHERE instr(language, (:lang)) > 0 ")
    fun getOnlyLanguage(lang: String) : MutableList<jobItem>

    @Query("SELECT DISTINCT * FROM jobs_table")
    fun getWithoutFilters() : MutableList<jobItem>


    @Query("SELECT DISTINCT * from jobs_table WHERE job IN (:filtersJob) AND position IN (:filtersPosition)")
    fun getWithoutLanguage(
        filtersJob: MutableList<String>,
        filtersPosition: MutableList<String>
    ): MutableList<jobItem>

    @Query("SELECT DISTINCT * FROM jobs_table WHERE position IN (:filtersPosition) AND instr(language, (:lang)) > 0 ")
    fun getWithoutJob(
        lang: String,
        filtersPosition: MutableList<String>
    ) : MutableList<jobItem>

    @Query("SELECT DISTINCT * FROM jobs_table WHERE job IN (:filtersJob) AND instr(language, (:lang)) > 0 ")
    fun getWithoutPosition(
        lang: String,
        filtersJob: MutableList<String>
    ) : MutableList<jobItem>


    fun filterThroughLanguages() : MutableList<jobItem>{

        var list = mutableListOf<jobItem>()


        if(filtersJob.isEmpty() && filtersPosition.isEmpty() && filtersLanguage.isEmpty()){

            list = getWithoutFilters()
        }
        else if(filtersPosition.isEmpty() && filtersLanguage.isEmpty()){

            list = getOnlyJobs(filtersJob)

        }
        else if(filtersJob.isEmpty() && filtersPosition.isEmpty()){
            filtersLanguage.forEach{

                list.addAll(getOnlyLanguage(it))
            }
        }
        else if(filtersJob.isEmpty() && filtersLanguage.isEmpty()){

            list = getOnlyPosition(filtersPosition)

        }
        else if(filtersJob.isEmpty()){

            filtersLanguage.forEach{

                list.addAll(getWithoutJob(it, filtersPosition))
            }
        }
        else if(filtersPosition.isEmpty()){

            filtersLanguage.forEach{

                list.addAll(getWithoutPosition(it, filtersJob))
            }

        }else if(filtersLanguage.isEmpty()){

            list = getWithoutLanguage(filtersJob, filtersPosition)

        }else{
            filtersLanguage.forEach {
                println("JEZZZZZZZZZZZZZZZZZZZ $it")
                list.addAll(singleLanguage(it, filtersJob, filtersPosition))
            }
        }
        d("FILTERTHROUGHHHH", list.toString())


        return list
    }

}