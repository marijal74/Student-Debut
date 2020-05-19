package com.example.studentdebut.Database


import com.example.studentdebut.MyApp.Companion.ListOfJobItems

/**
 * repozitorijum koji sluzi za upravljanje vise razlicitih izvora podataka
 */
class JobsRepository(private val jobsDao: DataAccessObject) {

    /**
     * startDB je upit koji uzima sve iz baze
     */
    fun startDB(): List<jobItem>{
        return jobsDao.startDB()
    }

    /**
     * filterThroughlLanguages su upiti koji filtriraju podatke u bazi
     */
    fun filterThroughLanguages() : List<jobItem> {
         return jobsDao.filterThroughLanguages()
     }
}
