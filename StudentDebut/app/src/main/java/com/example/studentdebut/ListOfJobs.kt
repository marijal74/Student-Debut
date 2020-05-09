package com.example.studentdebut

import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentdebut.Adapter.FeedAdapter
import com.example.studentdebut.MyApp.Companion.ListOfJobItems
import com.example.studentdebut.MyApp.Companion.filtersJob
import com.example.studentdebut.MyApp.Companion.filtersLanguage
import com.example.studentdebut.MyApp.Companion.filtersPosition
import kotlinx.android.synthetic.main.activity_list_of_jobs.*

//prikaz poslova na osnovu filtera koje je korisnik odabrao
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
        val recyclerView = findViewById<RecyclerView>(R.id.RecyclerView)
        val adapter = FeedAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        brojLayota=broj

        println("JESI LIIIIIIII PRAZAN JOB $filtersJob")
        println("JESI LIIIIIIII PRAZAN POS $filtersPosition")
        println("JESI LIIIIIIII PRAZAN LANG $filtersLanguage")

        adapter.setJobs(ListOfJobItems)
        adapter.notifyDataSetChanged()
        if(adapter.itemCount==0)
            Toast.makeText(this,"Ne postoje trazeni zahtevi", Toast.LENGTH_LONG).show()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        println("JESI LIIIIIIII PRAZAN $brojLayota")
        when (brojLayota) {
            3 -> filtersLanguage.clear()
            2 -> filtersPosition.clear()
            else -> filtersJob.clear()
        }
    }
}










