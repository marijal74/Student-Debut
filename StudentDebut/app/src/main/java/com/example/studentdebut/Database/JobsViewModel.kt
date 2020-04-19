package com.example.studentdebut.Database

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// ViewModel je korisna stvarcica koja pazi na sve promene u bazi i komunicira sa UI-om
// kako bi prikaz bio izmenjen, takodje pazi i na LiveData objekte
// fun fact: when room queries return LiveData they are automatically
// run asynchronously on a background thread a.k.a. fastfastfast a ne moramo ni da se mucimo oko toga
//
class JobsViewModel( application: Application) :AndroidViewModel(application)  {

    private val repo : JobsRepository
    // za kesiranje svih poslova
    val allJobs : LiveData<List<jobItem>>

    //konstruktor
    init {
        val jobsDao = JobsDatabase.getDatabase(application, viewModelScope).jobDao()
        repo = JobsRepository(jobsDao)
        allJobs = repo.allJobs
    }

    //wrapper za insert radi enkapsulacije od UI-a
    // poziva novu korutinu da ne bi blokirala UI
    fun insert(job: jobItem) = viewModelScope.launch(Dispatchers.IO) {
        repo.insert(job)
    }


}

class ViewModelFactory (private val app: Application) : ViewModelProvider.Factory {

    @Suppress ("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return JobsViewModel(app) as T

    }
}

