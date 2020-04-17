package com.example.studentdebut

import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.util.Log.d
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    //Lista za cuvanje reci po kojima filtriramo
    val filters = mutableListOf<CharSequence>()

    //Funkcija koja cuva u listi filters sve checkboxove koji su obelezeni
    fun addInFilters(niz:List<CheckBox>){

        for (i in  niz)
            if(i.isChecked)
                   filters.add(i.text)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Posao_ili_praksa.visibility=View.VISIBLE



        AddFilters.setOnClickListener(){
            Jezici.visibility= View.INVISIBLE


            val check_boxes = listOf(findViewById<CheckBox>(R.id.cb_javascript),findViewById<CheckBox>(R.id.cb_net), findViewById<CheckBox>(R.id.cb_python), findViewById<CheckBox>(R.id.cb_mysql),
                findViewById<CheckBox>(R.id.cb_angular), findViewById<CheckBox>(R.id.cb_vue), findViewById<CheckBox>(R.id.cb_jquery), findViewById<CheckBox>(R.id.cb_wordpress),
                findViewById<CheckBox>(R.id.cb_c), findViewById<CheckBox>(R.id.cb_csharp), findViewById<CheckBox>(R.id.cb_nodejs), findViewById<CheckBox>(R.id.cb_kotlin),
                findViewById<CheckBox>(R.id.cb_htmlcss), findViewById<CheckBox>(R.id.cb_scala), findViewById<CheckBox>(R.id.cb_java), findViewById<CheckBox>(R.id.cb_php), findViewById<CheckBox>(R.id.cb_XML),
                findViewById<CheckBox>(R.id.cb_bash), findViewById<CheckBox>(R.id.cb_ostalo2)
            )
            addInFilters(check_boxes)
            d("filteri",filters.toString() )


            d("AddFilters Button", "Clicked. New Activity started")
            startActivity(Intent(this, ListOfJobs::class.java))
        }
       btn_Next_page1.setOnClickListener(){

           Posao_ili_praksa.visibility= View.INVISIBLE

           val layout:ConstraintLayout=findViewById(R.id.Destinacija)
           layout.visibility=View.VISIBLE
           val check_boxes = listOf(findViewById<CheckBox>(R.id.cb_praksa), findViewById<CheckBox>(R.id.cb_posao))
           addInFilters(check_boxes)
               d("filteri",filters.toString() )
       }
        btn_Next_pagePozicije.setOnClickListener(){

            Pozicija.visibility= View.INVISIBLE

            val layout:ConstraintLayout=findViewById(R.id.Jezici)
            layout.visibility=View.VISIBLE
            val check_boxes = listOf(findViewById<CheckBox>(R.id.cb_senior_developer), findViewById<CheckBox>(R.id.cb_junior_developer),
                findViewById<CheckBox>(R.id.cd_developer), findViewById<CheckBox>(R.id.cb_analyst),findViewById<CheckBox>(R.id.graphic_designer),
                findViewById<CheckBox>(R.id.web_designer), findViewById<CheckBox>(R.id.tutor),findViewById<CheckBox>(R.id.technical_lead),
                findViewById<CheckBox>(R.id.backend_engineer), findViewById<CheckBox>(R.id.frontend_engineer),findViewById<CheckBox>(R.id.mobile_engineer),
                findViewById<CheckBox>(R.id.software_engineer),findViewById<CheckBox>(R.id.marketing), findViewById<CheckBox>(R.id.cb_ostalo)
                )
            addInFilters(check_boxes)
            d("filteri",filters.toString() )
        }
        btn_Next_pageDestinacija.setOnClickListener(){

            Destinacija.visibility= View.INVISIBLE

            val layout:ConstraintLayout=findViewById(R.id.Pozicija)
            layout.visibility=View.VISIBLE
            val check_boxes = listOf(findViewById<CheckBox>(R.id.cb_Srbija), findViewById<CheckBox>(R.id.cb_inostranstvo)
            )

            addInFilters(check_boxes)
            d("filteri",filters.toString() )
        }




    }



}
