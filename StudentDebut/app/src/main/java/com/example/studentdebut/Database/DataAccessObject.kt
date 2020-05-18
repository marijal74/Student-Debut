package com.example.studentdebut.Database

import android.util.Log.d
import androidx.room.*
import com.example.studentdebut.MyApp.Companion.filtersLanguage
import com.example.studentdebut.MyApp.Companion.filtersJob
import com.example.studentdebut.MyApp.Companion.filtersPosition


/**
 * interfejs za komunikaciju izmedju repozitorijuma i same baze
 */
@Dao
interface DataAccessObject {


    @Query("SELECT * FROM jobs_table")
    fun startDB(): List<jobItem>

    /**
     * ignorise ako vec postoji u tabeli
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job : List<jobItem>)

    /**
     *  insert, update su podrazumevane fje
     */
    @Update
    fun update(job : jobItem)

    /**
     * delete je takodje podrazumevana fja
     */
    @Delete
    fun delete(job : jobItem)

    /**
     * brise sve iz baze
     */
    @Query("DELETE FROM jobs_table")
    fun deleteEverything()

    /**
     * vraca velicinu baze
     */
    @Query("Select COUNT(*) from jobs_table")
    fun velicinaBaze():Double

    //upiti za filtriranje

    /**
     * upit kada korisnik izabere opciju iz sva tri pitanja
     */
    @Query("SELECT DISTINCT * FROM jobs_table WHERE  job IN (:filtersJob) AND position IN (:filtersPosition) AND instr(language, (:lang)) > 0 ")
    fun singleLanguage(
        lang: String,
        filtersJob: MutableList<String>,
        filtersPosition: MutableList<String>
    ) : MutableList<jobItem>

    /**
     * upit kada korisnik odabere samo opciju iz prvog pitanja
     */
    @Query("SELECT DISTINCT * FROM jobs_table WHERE job IN (:filtersJob)")
    fun getOnlyJobs(filtersJob: MutableList<String>) : MutableList<jobItem>

    /**
     * upit kada korisnik odabere samo opciju iz drugog pitanja
     */
    @Query("SELECT DISTINCT * FROM jobs_table WHERE position IN (:filtersPosition)")
    fun getOnlyPosition(filtersPosition: MutableList<String>) : MutableList<jobItem>

    /**
     * upit kada korisnik odabere samo opciju iz treceg pitanja
     */
    @Query("SELECT DISTINCT * FROM jobs_table WHERE instr(language, (:lang)) > 0 ")
    fun getOnlyLanguage(lang: String) : MutableList<jobItem>

    /**
     * upit kada korisnik nista ne odabere
     */
    @Query("SELECT DISTINCT * FROM jobs_table")
    fun getWithoutFilters() : MutableList<jobItem>

    /**
     * upit kada korisnik odabere opciju iz prvog i drugog pitanja
     */
    @Query("SELECT DISTINCT * from jobs_table WHERE job IN (:filtersJob) AND position IN (:filtersPosition)")
    fun getWithoutLanguage(
        filtersJob: MutableList<String>,
        filtersPosition: MutableList<String>
    ): MutableList<jobItem>

    /**
     * upit kada korisnik odabere opciju iz drugog i treceg pitanja
     */
    @Query("SELECT DISTINCT * FROM jobs_table WHERE position IN (:filtersPosition) AND instr(language, (:lang)) > 0 ")
    fun getWithoutJob(
        lang: String,
        filtersPosition: MutableList<String>
    ) : MutableList<jobItem>

    /**
     * upit kada korisnik odabere opciju iz prvog i treceg pitanja
     */
    @Query("SELECT DISTINCT * FROM jobs_table WHERE job IN (:filtersJob) AND instr(language, (:lang)) > 0 ")
    fun getWithoutPosition(
        lang: String,
        filtersJob: MutableList<String>
    ) : MutableList<jobItem>


    /**
     * funkcija koja sluzi da u zavisnosti od kombinacije izabranih filtera
     * pozove odgovarajuci upit
     */
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

        d("FILTER THROUGH", list.toString())

        return list
    }

}