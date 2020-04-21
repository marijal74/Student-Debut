package com.example.studentdebut

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.studentdebut.Common.HTTPDataHandler
import com.example.studentdebut.Database.JobsViewModel
import com.example.studentdebut.Database.jobItem
import com.example.studentdebut.Model.RSSObject
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import com.example.studentdebut.MyApp.Companion.done
import com.example.studentdebut.MyApp.Companion.ListOfJobItems

class MainActivity() : AppCompatActivity() {




    //Lista za cuvanje reci po kojima filtriramo
    val filters = mutableListOf<CharSequence>()

    //Funkcija koja cuva u listi filters sve checkboxove koji su obelezeni
    fun addInFilters(niz: List<CheckBox>) {

        for (i in niz)
            if (i.isChecked)
                filters.add(i.text)
    }

    private val RSS_to_JSON_API = "https://api.rss2json.com/v1/api.json?rss_url="
    /* FUCKKKKKKKKKKKKKKKKKKKKKK YOUUUUUUUUUUUUUUUU I HATEEEEEEEEEEEE YOUUUU DIEEEEEEEEEEEEEEEEEE (shine) https://startit.rs/poslovi/feed/",
    "https://startit.rs/poslovi/feed/?paged=2",
    "https://startit.rs/poslovi/feed/?paged=3",
    "https://startit.rs/poslovi/feed/?paged=4",
    "https://startit.rs/poslovi/feed/?paged=5",
    "https://www.helloworld.rs/rss/"*/
    val rsslinks = mutableListOf(
        "https://startit.rs/poslovi/feed/?paged=2",
        "https://startit.rs/poslovi/feed/?paged=3",
        "https://startit.rs/poslovi/feed/?paged=4",
        "https://startit.rs/poslovi/feed/?paged=5",
        "http://oglasi.matf.bg.ac.rs/?feed=rss2",
        "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2%26paged=2",
        "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2%26paged=3",
        "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2%26paged=4",
        "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2%26paged=5",
        "http://www.sljaka.com/rss/itposlovi/",
        "https://fonis.rs/category/posao/feed/",
        "https://fonis.rs/category/praksa/feed/",
        "https://fonis.rs/category/praksa/feed/?paged=2",
        "http://www.itposlovi.info/rss/programeri/",
        "http://www.itposlovi.info/rss/dizajneri/"
    )

    //viewModel
    private lateinit var viewModel: JobsViewModel




    private fun makeConnection(link: String): String {

        val result: String
        val http = HTTPDataHandler()
        result = http.GetHTTPDataHandler(link).toString()

        return result
    }


    private suspend fun loadRSS(result: String): RSSObject {


        lateinit var rssObject: RSSObject

        withContext(Default) {

            rssObject = Gson().fromJson<RSSObject>(result, RSSObject::class.java)

        }
        return rssObject
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Posao_ili_praksa.visibility = View.VISIBLE
        AddFilters.setOnClickListener() {
            // UbaciFiltereJezici()
            val i = Intent(this, ListOfJobs::class.java)
            //d("Mainmojalista",ListOfJobItems.toString())
            i.putExtra("mojalista",ListOfJobItems)
            //i.putParcelableArrayListExtra("mojalista", ListOfJobItems)
            startActivity(i)
        }
        btn_Next_page1.setOnClickListener() {
            UbaciFilterePocetna()
        }
        btn_Next_pagePozicije.setOnClickListener() {
            UbaciFilterePozicija()
        }
        btn_Next_pageDestinacija.setOnClickListener() {

            UbaciFiltereMesto()
        }


        val uiScope = CoroutineScope(Dispatchers.IO)

        uiScope.launch {


            var rssObject: RSSObject


            rsslinks.forEach {

                val url_get_data = StringBuilder(RSS_to_JSON_API)
                url_get_data.append(it)
              //  d("url_get_data",url_get_data.toString())
                val result = async {
                    makeConnection(url_get_data.toString())
                }.await()


                rssObject = async {

                    loadRSS(result)

                }.await()


                //iz rss u job item
                withContext(Default) {
                    rssObject.items.forEach {
                        // TODO ova fja filterContent vadi tekst iz html-a
                        // samim tim ovaj kod pod komentarima u sledecem TODO-u vrvt nece biti potreban
                        // ako se odlucimo da fiksiramo duzinu bubble-a
                        it.filterContent()
                        val ajob = jobItem(
                            0,
                            it.title,
                            it.pubDate,
                            it.link,
                            it.description,
                            it.content,
                            "",
                            "",
                            "",
                            ""
                        )

                        // d("item", ajob.toString())
                        ListOfJobItems.add(ajob)

                    }

                }
            }
           done=true
        }
        d("done","DOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE")

    }


    private fun UbaciFiltereMesto() {

        Destinacija.visibility = View.INVISIBLE

        val layout: ConstraintLayout = findViewById(R.id.Pozicija)
        layout.visibility = View.VISIBLE
        val check_boxes = listOf(
            findViewById<CheckBox>(R.id.cb_Srbija),
            findViewById<CheckBox>(R.id.cb_inostranstvo)
        )

        addInFilters(check_boxes)
        // d("filteri", filters.toString())
    }
    private fun UbaciFilterePocetna() {

        Posao_ili_praksa.visibility = View.INVISIBLE

        val layout: ConstraintLayout = findViewById(R.id.Destinacija)
        layout.visibility = View.VISIBLE
        val check_boxes = listOf(
            findViewById<CheckBox>(R.id.cb_praksa),
            findViewById<CheckBox>(R.id.cb_posao)
        )
        addInFilters(check_boxes)
       // d("filteri", filters.toString())
    }
    private fun UbaciFilterePozicija() {


        Pozicija.visibility = View.INVISIBLE

        val layout: ConstraintLayout = findViewById(R.id.Jezici)
        layout.visibility = View.VISIBLE
        val check_boxes = listOf(
            findViewById<CheckBox>(R.id.cb_senior_developer),
            findViewById<CheckBox>(R.id.cb_junior_developer),
            findViewById<CheckBox>(R.id.cd_developer),
            findViewById<CheckBox>(R.id.cb_analyst),
            findViewById<CheckBox>(R.id.graphic_designer),
            findViewById<CheckBox>(R.id.web_designer),
            findViewById<CheckBox>(R.id.tutor),
            findViewById<CheckBox>(R.id.technical_lead),
            findViewById<CheckBox>(R.id.backend_engineer),
            findViewById<CheckBox>(R.id.frontend_engineer),
            findViewById<CheckBox>(R.id.mobile_engineer),
            findViewById<CheckBox>(R.id.software_engineer),
            findViewById<CheckBox>(R.id.marketing),
            findViewById<CheckBox>(R.id.cb_ostalo)
        )
        addInFilters(check_boxes)
      //  d("filteri", filters.toString())


    }


    private fun UbaciFiltereJezici() {
        Jezici.visibility = View.INVISIBLE


        val check_boxes = listOf(
            findViewById<CheckBox>(R.id.cb_javascript),
            findViewById<CheckBox>(R.id.cb_net),
            findViewById<CheckBox>(R.id.cb_python),
            findViewById<CheckBox>(R.id.cb_mysql),
            findViewById<CheckBox>(R.id.cb_angular),
            findViewById<CheckBox>(R.id.cb_vue),
            findViewById<CheckBox>(R.id.cb_jquery),
            findViewById<CheckBox>(R.id.cb_wordpress),
            findViewById<CheckBox>(R.id.cb_c),
            findViewById<CheckBox>(R.id.cb_csharp),
            findViewById<CheckBox>(R.id.cb_nodejs),
            findViewById<CheckBox>(R.id.cb_kotlin),
            findViewById<CheckBox>(R.id.cb_htmlcss),
            findViewById<CheckBox>(R.id.cb_scala),
            findViewById<CheckBox>(R.id.cb_java),
            findViewById<CheckBox>(R.id.cb_php),
            findViewById<CheckBox>(R.id.cb_XML),
            findViewById<CheckBox>(R.id.cb_bash),
            findViewById<CheckBox>(R.id.cb_ostalo2)
        )
        addInFilters(check_boxes)


    }


}
/*
    if(it.link.contains("startit")){
        it.startitContent()
    }
    else if(it.link.contains("matf")){
        it.matfContent()
    }
    else if(it.link.contains("sljaka")){
        it.sljakaContent()
    }
    else if(it.link.contains("fonis")){
        it.fonisContent()
        d("fonis" , it.content)
    }
    else if(it.link.contains("itposlovi")){
        it.itposloviContent()
        d("ispis" , it.content)
    }
*/




