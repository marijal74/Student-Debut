package com.example.studentdebut.Database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studentdebut.ListOfJobs
import com.example.studentdebut.MyApp.Companion.ListOfJobItems
import com.example.studentdebut.MyApp.Companion.filtersJob
import com.example.studentdebut.MyApp.Companion.filtersPosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// repozitorijum koji sluzi za upravljanje vise razlicitih izvora podataka
// koliko sam skontala nije neophodan, ali preporuceno je jer obezbedjuje cistiji kod
// i pojam komunikacije izmedju baze i ViewModel-a
class JobsRepository(private val jobsDao: DataAccessObject) {




   fun deleteEverything(){
        jobsDao.deleteEverything()
    }
     fun initializeDb(): List<jobItem>{
        return jobsDao.initializeDb()
    }
    fun velicinaBaze():Double{
        return jobsDao.velicinaBaze()
    }
    suspend fun insert(){
        jobsDao.insert(ListOfJobItems)
    }
     fun filterThroughLanguages() : List<jobItem> {
         return jobsDao.filterThroughLanguages()
     }
}
