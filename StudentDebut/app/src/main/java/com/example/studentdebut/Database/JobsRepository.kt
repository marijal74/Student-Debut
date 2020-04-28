package com.example.studentdebut.Database

import androidx.lifecycle.LiveData
import com.example.studentdebut.MyApp.Companion.filtersJob
import com.example.studentdebut.MyApp.Companion.filtersLanguage
import com.example.studentdebut.MyApp.Companion.filtersPosition

// repozitorijum koji sluzi za upravljanje vise razlicitih izvora podataka
// koliko sam skontala nije neophodan, ali preporuceno je jer obezbedjuje cistiji kod
// i pojam komunikacije izmedju baze i ViewModel-a
class JobsRepository(private val jobsDao: DataAccessObject) {

   // lateinit var allJobs: LiveData<List<jobItem>>


    //val allJobs: LiveData<List<jobItem>> = jobsDao.getAllJobs()

    val applyAllFilters : LiveData<List<jobItem>> = jobsDao.applyFilters(filtersJob, filtersPosition, filtersLanguage)

    suspend fun insert(job : jobItem) {
        jobsDao.insert(job)
    }

}
