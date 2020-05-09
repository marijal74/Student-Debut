package com.example.studentdebut.Database

import android.app.Application
import android.content.Context
import android.util.Log
import android.util.Log.d
import androidx.lifecycle.*
import com.example.studentdebut.MyApp.Companion.ListOfJobItems
import com.example.studentdebut.MyApp.Companion.filtersJob
import com.example.studentdebut.MyApp.Companion.filtersLanguage
import com.example.studentdebut.MyApp.Companion.filtersPosition
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


// ViewModel je korisna stvarcica koja pazi na sve promene u bazi i komunicira sa UI-om
// kako bi prikaz bio izmenjen, takodje pazi i na LiveData objekte
// fun fact: when room queries return LiveData they are automatically
// run asynchronously on a background thread a.k.a. fastfastfast a ne moramo ni da se mucimo oko toga
//
class JobsViewModel( application: Application) :AndroidViewModel(application)  {

    val repo : JobsRepository
    // za kesiranje svih poslova
    var  allJobs: List<jobItem>

    //kostruktor
    init {
        val jobsDao = JobsDatabase.getDatabase(application, viewModelScope).jobDao()
        repo = JobsRepository(jobsDao)
        allJobs=emptyList()//ListOfJobItems
        d("KONSTRUKTOR", allJobs.toString())

    }
    public fun emptyFilters() : Boolean{
        if(filtersLanguage.isEmpty() && filtersJob.isEmpty() && filtersPosition.isEmpty()){
            return true
        }
        else
            return false
    }

    fun velicinaBaze():Double{
        var velicina:Double = -5.0
        viewModelScope.launch(Dispatchers.IO){
            velicina = repo.velicinaBaze()
        }
        return velicina
    }
    /*fun loadData() {

        d("LOADUJEM", "ASAAAAAAAAAAAAAAAAAAAAAAAAA")

        viewModelScope.launch(Dispatchers.IO){
            allJobs= async{
                repo.initializeDb()//filterThroughLanguages()

            }.await()
            d("VRACAMMMMM", allJobs.toString())

        }
        d("VRACAMMM", allJobs.toString())

    }*/
    fun insert() = viewModelScope.launch(Dispatchers.IO) {
        repo.insert()
    }

    fun deleteEverything() = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteEverything()
    }
    fun startDB() = viewModelScope.launch(Dispatchers.IO) {
        repo.startDB()
    }


}

class ViewModelFactory (private val app: Application) : ViewModelProvider.Factory {

    @Suppress ("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return JobsViewModel(app) as T

    }
}

