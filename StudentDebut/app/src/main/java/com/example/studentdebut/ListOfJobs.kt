package com.example.studentdebut

import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentdebut.Adapter.FeedAdapter
import com.example.studentdebut.Database.JobsViewModel
import com.example.studentdebut.Database.ViewModelFactory
import com.example.studentdebut.MyApp.Companion.ListOfJobItems
import com.example.studentdebut.MyApp.Companion.filtersJob
import com.example.studentdebut.MyApp.Companion.filtersLanguage
import com.example.studentdebut.MyApp.Companion.filtersPosition
import kotlinx.android.synthetic.main.activity_list_of_jobs.*
import kotlinx.android.synthetic.main.row.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ListOfJobs : AppCompatActivity() {


    private  var brojLayota:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_jobs)
        val mIntent = intent
        val broj = mIntent.getIntExtra("Visibilty",50)
        d("extraaaaaaaaaa",broj.toString())

        setSupportActionBar(toolbar_listofjobs)
        //postavlja RecyclerView
        // ovo nam je inicijalno radila showResult fja i msm da je zbg toga bio onaj bug da se recycler view
        // vraca na pocetak, jer se vise puta postavljao adapter u forEach petlji
        val recyclerView = findViewById<RecyclerView>(R.id.RecyclerView)
        val adapter = FeedAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        brojLayota=broj;

        val uiScope = CoroutineScope(Dispatchers.IO)


        //postavljamo viewmodel
        //ovaj viewModelFactory necete naci u ofc dokumentaciji, nema ga iz nekog razloga, a neophodan je da
        // bi se uopste napravila instanca ViewModel-a


        println("JESI LIIIIIIII PRAZAN JOB ${filtersJob}")
        println("JESI LIIIIIIII PRAZAN POS ${filtersPosition}")
        println("JESI LIIIIIIII PRAZAN LANG ${filtersLanguage}")



        adapter.setJobs(ListOfJobItems)
        adapter.notifyDataSetChanged()
        if(adapter.itemCount==0)
            Toast.makeText(this,"Ne postoje trazeni zahtevi", Toast.LENGTH_LONG).show()



    }

    /*
    fun SeeMoreClick(v: View) {
        val tb = v as ToggleButton
        txtContent.setMaxLines(if (tb.isChecked) 10 else 4)
    }

    override fun afterTextChanged(s: Editable?) {
        if (txtContent.getLineCount() >= 4) {

            txtSeeMore.visibility = View.VISIBLE
        }
    }
    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int,count: Int, after: Int) {}
*/
    override fun onBackPressed() {
        super.onBackPressed()
        println("JESI LIIIIIIII PRAZAN $brojLayota")
        if(brojLayota==3)
            filtersLanguage.clear()
        else if (brojLayota==2)
            filtersPosition.clear()
        else
            filtersJob.clear()
    }
}










