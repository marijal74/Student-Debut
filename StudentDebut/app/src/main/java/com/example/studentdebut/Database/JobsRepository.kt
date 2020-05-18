package com.example.studentdebut.Database


import com.example.studentdebut.MyApp.Companion.ListOfJobItems

/**
 * repozitorijum koji sluzi za upravljanje vise razlicitih izvora podataka
 */
class JobsRepository(private val jobsDao: DataAccessObject) {

    fun startDB(): List<jobItem>{
        return jobsDao.startDB()
    }

    fun filterThroughLanguages() : List<jobItem> {
         return jobsDao.filterThroughLanguages()
     }
}
