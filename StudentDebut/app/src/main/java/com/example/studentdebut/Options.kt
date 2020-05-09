package com.example.studentdebut

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.CheckBox
import androidx.annotation.RequiresApi
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.studentdebut.Database.JobsViewModel
import com.example.studentdebut.Database.ViewModelFactory
import com.example.studentdebut.MyApp.Companion.ListOfJobItems
import com.example.studentdebut.MyApp.Companion.done
import kotlinx.android.synthetic.main.activity_options.*
import com.example.studentdebut.MyApp.Companion.filtersJob
import com.example.studentdebut.MyApp.Companion.filtersLanguage
import com.example.studentdebut.MyApp.Companion.filtersPosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class Options() : AppCompatActivity() {


    lateinit var view : View
    private lateinit var viewModel: JobsViewModel

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
        val mStipendija: CheckBox = findViewById(R.id.cb_stipendija)
        val mSledece: TextView = findViewById(R.id.btn_Next_page1)
        val mHolder : TextView = findViewById(R.id.holder)

        viewModel =
            ViewModelProvider(this, ViewModelFactory(application)).get(JobsViewModel::class.java)

        viewModel.startDB()
        d("OPTIONNNS", ListOfJobItems.toString())





        view = findViewById(R.id.myProgressButton)
        //Posao_ili_praksa.visibility = View.VISIBLE
        mHolder.visibility = View.GONE


        mStipendija.setOnClickListener(View.OnClickListener { if(mStipendija.isChecked){
            mSledece.visibility = View.GONE
            mHolder.visibility = View.VISIBLE}
        else{
            mSledece.visibility = View.VISIBLE
            mHolder.visibility = View.GONE

        }})



       val check_boxesJobs = mutableListOf(
            findViewById<CheckBox>(R.id.cb_praksa),
            findViewById<CheckBox>(R.id.cb_posao),
            findViewById<CheckBox>(R.id.cb_stipendija)

        )

        val check_boxesPositions = mutableListOf<CheckBox>(

            findViewById(R.id.cb_developer),
            findViewById(R.id.cb_analyst),
            findViewById(R.id.cb_menager),
            findViewById(R.id.cb_designer),
            findViewById(R.id.cb_tutor),
            findViewById(R.id.cb_technical_lead),
            findViewById(R.id.cb_engineer),
            findViewById(R.id.cb_programmer),
            findViewById(R.id.cb_scientist),
            findViewById(R.id.cb_administrator),
            findViewById(R.id.cb_marketing),
            findViewById(R.id.cb_ostalo)
        )

        val check_boxesLanguages = mutableListOf<CheckBox>(
            findViewById(R.id.cb_javascript),
            findViewById(R.id.cb_net),
            findViewById(R.id.cb_python),
            findViewById(R.id.cb_sql),
            findViewById(R.id.cb_angular),
            findViewById(R.id.cb_vue),
            findViewById(R.id.cb_adobe),
            findViewById(R.id.cb_jquery),
            findViewById(R.id.cb_cpp),
            findViewById(R.id.cb_wordpress),
            findViewById(R.id.cb_csharp),
            findViewById(R.id.cb_nodejs),
            findViewById(R.id.cb_kotlin),
            findViewById(R.id.cb_htmlcss),
            findViewById(R.id.cb_scala),
            findViewById(R.id.cb_java),
            findViewById(R.id.cb_react),
            findViewById(R.id.cb_matlab),
            findViewById(R.id.cb_php),
            findViewById(R.id.cb_XML),
            findViewById(R.id.cb_bash),
            findViewById(R.id.cb_ostalo2)
        )


        allChecked(check_boxesPositions)
        allChecked(check_boxesLanguages)
        for(c in check_boxesPositions)
        c.setOnClickListener() {
            cb_prikazi_sve_pagePozicije.isChecked = allChecked(check_boxesPositions)
        }
        for(c in check_boxesLanguages)
            c.setOnClickListener() {
                cb_prikazi_sve_pageJezici.isChecked = allChecked(check_boxesLanguages)
        }


        cb_prikazi_sve_pagePozicije.setOnClickListener(){

            if(cb_prikazi_sve_pagePozicije.isChecked){
                for(c in check_boxesPositions)
                    c.isChecked=true
            }
            else{
                for(c in check_boxesPositions)
                    c.isChecked=false
            }

        }

        cb_prikazi_sve_pageJezici.setOnClickListener(){



            if(cb_prikazi_sve_pageJezici.isChecked){
                for(c in check_boxesLanguages)
                    c.isChecked=true
            }
            else{
                for(c in check_boxesLanguages)
                    c.isChecked=false
            }


        }


        view.setOnClickListener() {
            addFIltersForJobs(check_boxesJobs)
            addInFilters(filtersPosition,check_boxesPositions,cb_prikazi_sve_pagePozicije)
            addInFilters(filtersLanguage,check_boxesLanguages,cb_prikazi_sve_pageJezici)
            val progressButton = ProgressButton(this@Options, view)
            progressButton.buttonActivated()
            val handler = Handler()
            handler.postDelayed({
                progressButton.buttonFinished()
                    val intent = Intent(this@Options, ListOfJobs::class.java)
                    val pp=findViewById<ConstraintLayout>(R.id.Posao_ili_praksa)
                    val po=findViewById<ConstraintLayout>(R.id.Pozicija)
                    val la=findViewById<ConstraintLayout>(R.id.Jezici)
                    var visi:Int=3
                    if(po.visibility==View.VISIBLE)
                        visi=2
                    if(pp.visibility==View.VISIBLE)
                        visi=1

                    intent.putExtra("Visibilty",visi)
                    //STIZE DA UBACI FILTERE
                if(done){

                 CoroutineScope(Dispatchers.IO).launch {


                     viewModel.allJobs= async{
                         viewModel.repo.filterThroughLanguages()//initializeDb()

                     }.await()
                     d("ALLLLLLLLLLLLLL", viewModel.allJobs.toString())
                     ListOfJobItems = ArrayList(viewModel.allJobs)
                     println("LISTAAAAA" + ListOfJobItems.toString())
                 }
            }
                startActivity(intent)
            },6000)

        }



        btn_Next_page1.setOnClickListener() {
            UbaciFilterePocetna(check_boxesJobs)
        }
        btn_Next_pagePozicije.setOnClickListener() {
            UbaciFilterePozicija(check_boxesPositions)
        }

        btn_Previous_pageJezici.setOnClickListener(){
            val nolayout: ConstraintLayout = findViewById(R.id.Jezici)
            filtersPosition.clear()
            nolayout.visibility = View.INVISIBLE
            val layout: ConstraintLayout = findViewById(R.id.Pozicija)
            layout.visibility = View.VISIBLE
        }
        btn_Previous_pagePozicije.setOnClickListener(){
            val nolayout: ConstraintLayout = findViewById(R.id.Pozicija)
            filtersJob.clear()
            nolayout.visibility = View.INVISIBLE
            val layout: ConstraintLayout = findViewById(R.id.Posao_ili_praksa)
            layout.visibility = View.VISIBLE
        }

    }

    fun allChecked(check_boxes:MutableList<CheckBox>):Boolean{


        var all=true
        for(c in check_boxes) {
            if (!c.isChecked)
                all=false
        }
        return all

    }
    /*fun anyChecked(check_boxes:MutableList<CheckBox>,cb:CheckBox):Boolean{
        var any=false
        check_boxes.add(cb)
            for (c in check_boxes)
                if (c.isChecked) {
                    any = true
                    break
                }

        return any
    }*/

    fun addInFilters(list:MutableList<String>, check_boxoes: MutableList<CheckBox>, c:CheckBox) {

        if(list.isEmpty()) {
            if (!c.isChecked) {
                for (i in check_boxoes)
                    if (i.isChecked)
                        list.add(i.text.toString())
            }
        }
    }
    fun addFIltersForJobs(check_boxes: MutableList<CheckBox>){

        if(filtersJob.isEmpty()) {
            for (i in check_boxes)
                if (i.isChecked) {
                    if (i.text.toString().equals("Praksa", true)) {
                        filtersJob.add("Praksi")
                        filtersJob.add("Praksu")
                        filtersJob.add("Prakse")
                        filtersJob.add("Internship")
                    }
                    filtersJob.add(i.text.toString())
                }
        }

    }

    private fun UbaciFilterePocetna(check_boxes:MutableList<CheckBox>) {

        Posao_ili_praksa.visibility = View.INVISIBLE

        val layout: ConstraintLayout = findViewById(R.id.Pozicija)
        layout.visibility = View.VISIBLE



    }
    private fun UbaciFilterePozicija(check_boxes:MutableList<CheckBox>) {


        Pozicija.visibility = View.INVISIBLE

        val layout: ConstraintLayout = findViewById(R.id.Jezici)
        layout.visibility = View.VISIBLE


        //  d("filteri", filters.toString())


    }
}
/*  for(c in check_boxesLanguages) {
       c.setOnClickListener() {
          val any=anyChecked(check_boxesLanguages,cb_prikazi_sve_pageJezici)
           if(any)
               view.visibility=View.VISIBLE
           else
               view.visibility=View.INVISIBLE
       }
   }
   cb_prikazi_sve_pageJezici.setOnClickListener() {
       val any=anyChecked(check_boxesLanguages,cb_prikazi_sve_pageJezici)
       if(any)
           view.visibility=View.VISIBLE
       else
           view.visibility=View.INVISIBLE
   }
*/

