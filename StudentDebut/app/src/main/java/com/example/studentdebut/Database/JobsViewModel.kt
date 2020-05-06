package com.example.studentdebut.Database

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.studentdebut.MyApp.Companion.filtersLanguage
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


// ViewModel je korisna stvarcica koja pazi na sve promene u bazi i komunicira sa UI-om
// kako bi prikaz bio izmenjen, takodje pazi i na LiveData objekte
// fun fact: when room queries return LiveData they are automatically
// run asynchronously on a background thread a.k.a. fastfastfast a ne moramo ni da se mucimo oko toga
//
class JobsViewModel( application: Application) :AndroidViewModel(application)  {

    private val repo : JobsRepository
    // za kesiranje svih poslova
    var allJobs: MutableLiveData<List<jobItem>> = MutableLiveData()
    //val result: LiveData<List<jobItem>>

    //konstruktor
    init {
        val jobsDao = JobsDatabase.getDatabase(application, viewModelScope).jobDao()
        repo = JobsRepository(jobsDao)
    }


    //wrapper za insert radi enkapsulacije od UI-a
    // poziva novu korutinu da ne bi blokirala UI

    fun getResult(): LiveData<List<jobItem>>{
        return allJobs

    }
    fun loadData(){


        CoroutineScope(Dispatchers.IO).launch{
            val list =async {
                repo.filterThroughLanguages()
            } .await()
            withContext(Main){
                allJobs.setValue(list)
            }

        }


    }
    /*fun getAllJobs() = viewModelScope.launch(Dispatchers.IO) {
        allJobs.value = repo.getAllJobs()
    }*/



}

class ViewModelFactory (private val app: Application) : ViewModelProvider.Factory {

    @Suppress ("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return JobsViewModel(app) as T

    }
}

