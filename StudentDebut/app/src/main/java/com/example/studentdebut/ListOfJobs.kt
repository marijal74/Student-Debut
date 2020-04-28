package com.example.studentdebut


import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentdebut.Adapter.FeedAdapter
import com.example.studentdebut.Database.JobsViewModel
import com.example.studentdebut.Database.ViewModelFactory
import com.example.studentdebut.Database.jobItem
import kotlinx.android.synthetic.main.activity_list_of_jobs.*
import kotlinx.coroutines.*
import com.example.studentdebut.MyApp.Companion.done
import com.example.studentdebut.MyApp.Companion.ListOfJobItems
import com.example.studentdebut.MyApp.Companion.filtersJob
import com.example.studentdebut.MyApp.Companion.filtersLanguage
import com.example.studentdebut.MyApp.Companion.filtersPosition


// uvodna pricica kako sve ovo funkcionise na : https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#0
// introduction deo

class ListOfJobs : AppCompatActivity() {




    //viewModel
    private lateinit var viewModel: JobsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_jobs)

        toolbar_listofjobs.title = "NEWS"
        setSupportActionBar(toolbar_listofjobs)

        //postavlja RecyclerView
        // ovo nam je inicijalno radila showResult fja i msm da je zbg toga bio onaj bug da se recycler view
        // vraca na pocetak, jer se vise puta postavljao adapter u forEach petlji
        val recyclerView = findViewById<RecyclerView>(R.id.RecyclerView)
        val adapter = FeedAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val uiScope = CoroutineScope(Dispatchers.IO)


        //postavljamo viewmodel
        //ovaj viewModelFactory necete naci u ofc dokumentaciji, nema ga iz nekog razloga, a neophodan je da
        // bi se uopste napravila instanca ViewModel-a
        viewModel =
            ViewModelProvider(this, ViewModelFactory(application)).get(JobsViewModel::class.java)

        println("JESI LIIIIIIII PRAZAN ${filtersJob}")
        println("JESI LIIIIIIII PRAZAN ${filtersPosition}")
        println("JESI LIIIIIIII PRAZAN ${filtersLanguage}")
       /* if(filtersJob.isEmpty()|| filtersPosition.isEmpty() || filtersLanguage.isEmpty()) {
            //pravimo observer koji obavestava UI da je doslo do izmena
            viewModel.allJobs.observe(this, Observer { jobs ->
                // Update the cached copy of the words in the adapter.
                jobs?.let { adapter.setJobs(it) }
            })
        }else {*/

            viewModel.applyAllFilters.observe(
                this,
                Observer { jobs -> jobs?.let { adapter.setJobs(it) } })
       // }

        uiScope.launch {
            ListOfJobItems.forEach() {
                //TODO ??? mozda , mozda ne, ovo je lista svih itema iz baze, mozda raditi na njima ovde, mozda je skroz nepotrebno


            }
        }
    }
}








