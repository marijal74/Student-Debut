package com.example.studentdebut.Database

import androidx.lifecycle.LiveData


// repozitorijum koji sluzi za upravljanje vise razlicitih izvora podataka
// koliko sam skontala nije neophodan, ali preporuceno je jer obezbedjuje cistiji kod
// i pojam komunikacije izmedju baze i ViewModel-a
class JobsRepository(private val jobsDao: DataAccessObject) {


    val allJobs: LiveData<List<jobItem>> = jobsDao.getAllJobs()


    suspend fun insert(job : jobItem) {
        jobsDao.insert(job)
    }

}
