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
import com.example.studentdebut.Common.HTTPDataHandler
import com.example.studentdebut.Database.JobsViewModel
import com.example.studentdebut.Database.jobItem
import com.example.studentdebut.Model.RSSObject
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_options.*
import kotlinx.coroutines.*
import com.example.studentdebut.MyApp.Companion.filtersJob
import com.example.studentdebut.MyApp.Companion.filtersLanguage
import com.example.studentdebut.MyApp.Companion.filtersPosition
import kotlinx.android.synthetic.main.activity_options.view.*


class Options() : AppCompatActivity() {







    private val RSS_to_JSON_API = "https://api.rss2json.com/v1/api.json?rss_url="
    /*


    "https://startit.rs/poslovi/feed/",
    "https://startit.rs/poslovi/feed/?paged=2",
      "https://startit.rs/poslovi/feed/?paged=2",
        "https://startit.rs/poslovi/feed/?paged=3",
        "https://startit.rs/poslovi/feed/?paged=4",
        "https://startit.rs/poslovi/feed/?paged=5",

        "https://www.helloworld.rs/rss/",

        "http://oglasi.matf.bg.ac.rs/?feed=rss2", -- ovo nam nije potrebno

        http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2
        "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2%26paged=2",
        "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2%26paged=3",
        "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2%26paged=4",
        "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2%26paged=5",

        -------------zaboravile smo linkove za prakse!!!!-----------------------
        "http://oglasi.matf.bg.ac.rs/?tag=praksa%26feed=rss2"
        "http://oglasi.matf.bg.ac.rs/?tag=praksa%26feed=rss2%26paged=2",
        "http://oglasi.matf.bg.ac.rs/?tag=praksa%26feed=rss2%26paged=3",
        "http://oglasi.matf.bg.ac.rs/?tag=praksa%26feed=rss2%26paged=4",
        "http://oglasi.matf.bg.ac.rs/?tag=praksa%26feed=rss2%26paged=5",
        ------------------------------------------------------------------------
        "http://oglasi.matf.bg.ac.rs/?tag=stipendije%26feed=rss2",

        "http://www.sljaka.com/rss/itposlovi/",

        "https://fonis.rs/category/posao/feed/",
        "https://fonis.rs/category/praksa/feed/",
        "https://fonis.rs/category/praksa/feed/?paged=2",

        "http://www.itposlovi.info/rss/programeri/",
        "http://www.itposlovi.info/rss/dizajneri/"

     */

   val rsslinks = mutableListOf(

       "http://oglasi.matf.bg.ac.rs/?tag=praksa%26feed=rss2",
       "http://oglasi.matf.bg.ac.rs/?tag=praksa%26feed=rss2%26paged=2",
       "http://oglasi.matf.bg.ac.rs/?tag=praksa%26feed=rss2%26paged=3",
       "http://oglasi.matf.bg.ac.rs/?tag=praksa%26feed=rss2%26paged=4",
       "http://oglasi.matf.bg.ac.rs/?tag=praksa%26feed=rss2%26paged=5"
   )

    //viewModel
    private lateinit var viewModel: JobsViewModel
    private fun makeConnection(link: String): String {

        val result: String
        val http = HTTPDataHandler()
        result = http.GetHTTPDataHandler(link, this.applicationContext).toString()

        return result
    }


    private suspend fun loadRSS(result: String): RSSObject?{


        var rssObject: RSSObject?=null

        withContext(Dispatchers.Default) {
                rssObject = Gson().fromJson<RSSObject>(result, RSSObject::class.java)

        }

        return rssObject
    }

    lateinit var view : View

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
        val mStipendija: CheckBox = findViewById(R.id.cb_stipendija)
        val mSledece: TextView = findViewById(R.id.btn_Next_page1)
        val mHolder : TextView = findViewById(R.id.holder)

        //mStipendija = findViewById(R.id.cb_stipendija)
        //mSledece = findViewById(R.id.btn_Next_page1)

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
            findViewById(R.id.cb_senior_developer),
            findViewById(R.id.cb_junior_developer),
            findViewById(R.id.cb_developer),
            findViewById(R.id.cb_analyst),
            findViewById(R.id.cb_menager),
            findViewById(R.id.cb_designer),
            findViewById(R.id.cb_web_designer),
            findViewById(R.id.cb_tutor),
            findViewById(R.id.cb_technical_lead),
            findViewById(R.id.cb_engineer),
            findViewById(R.id.cb_backend_engineer),
            findViewById(R.id.cb_frontend_engineer),
            findViewById(R.id.cb_mobile_engineer),
            findViewById(R.id.cb_software_engineer),
            findViewById(R.id.cb_marketing),
            findViewById(R.id.cb_ostalo)
        )

        val check_boxesLanguages = mutableListOf<CheckBox>(
            findViewById(R.id.cb_javascript),
            findViewById(R.id.cb_net),
            findViewById(R.id.cb_python),
            findViewById(R.id.cb_mysql),
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
               // val handler1 = Handler()
               // handler1.postDelayed({
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
                    startActivity(intent)

              //  },0)
            },6000)

            //val i = Intent(this, ListOfJobs::class.java)
            //startActivity(i)

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


        val uiScope = CoroutineScope(Dispatchers.IO)

        uiScope.launch {


            var rssObject: RSSObject?


            rsslinks.forEach {

                val url_get_data = StringBuilder(RSS_to_JSON_API)
                url_get_data.append(it)
                d("url_get_data",url_get_data.toString())
                var result = async {
                    makeConnection(url_get_data.toString())
                }.await()

                d("resultic",result)

                rssObject = async {

                    loadRSS(result)

                }.await()

                var link = it
                //iz rss u job item
                if (rssObject != null) {
                    d("done", MyApp.done.toString())
                    if (!rssObject!!.items.isEmpty()) {
                        withContext(Dispatchers.Default) {
                            rssObject!!.items.forEach {



                                d("linkk", link)
                                //fja filterContent vadi tekst iz html-a
                                it.filterContent(link)
                                val positionf = addPosition(it.title + " " + it.content + " " + it.description)
                                val languagef = addLanguages(it.title + " " + it.content + " " + it.description)
                                var jobf=addJob(link,it.title + " " + it.content + " " + it.description)
                                val ajob = jobItem(
                                    0,
                                    it.title,
                                    it.pubDate,
                                    it.link,
                                    it.description,
                                    it.content,
                                    jobf,
                                    positionf,
                                    languagef
                                )
                                d("jobic", ajob.toString())

                                println("JOOOOOOOOOOOOOOB $jobf")

                                println("          POSITOOOOOOOOOON $positionf")
                                println("          LANGUAGGGGGGGGGG $languagef")

                                // d("item", ajob.toString())
                                MyApp.ListOfJobItems.add(ajob)

                            }

                        }
                    }
                }
            }

            }
            MyApp.done=true

        Log.d(
            "done",
            "DOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE"
        )

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

    fun containsWord(inputString: String, items: List<String?>): String {
        var thing:String=" "
        for (item in items) {
            if (inputString.contains(item!!,true)) {
                thing=item
                break
            }
        }
        return thing
    }
    fun containsWordsForLanguages(inputString: String, items: List<String?>): String {
        var found=false
        val things = StringBuilder()
        for (item in items) {
            if (inputString.contains(item!!)) {
                things.append(item)
                      .append("_")
                found=true
            }
        }
        if(found)
           return things.toString()
        else return " "

    }
    private fun addPosition(input:String):String{

        var position="Druge pozicije"
        val listofpositions= mutableListOf<String>(cb_senior_developer.text.toString(),cb_junior_developer.text.toString(),cb_developer.text.toString(),cb_analyst.text.toString(),
                                                    cb_designer.text.toString(),cb_web_designer.text.toString(),cb_tutor.text.toString(),cb_technical_lead.text.toString(),
                                                  cb_engineer.text.toString(), cb_backend_engineer.text.toString(),cb_frontend_engineer.text.toString(),cb_mobile_engineer.text.toString(),
                                                 cb_menager.text.toString(),  cb_marketing.text.toString()
            )
        if(containsWord(input,listofpositions)!=" ")
            position=containsWord(input,listofpositions)
      return position
    }
    private fun addLanguages(input:String): String {
        var languages="Drugi jezici"
        val listoflanguages= mutableListOf<String>(cb_javascript.text.toString(),cb_net.text.toString(),cb_python.text.toString(),  cb_mysql.text.toString(),
            cb_vue.text.toString(),cb_jquery.text.toString(),cb_wordpress.text.toString(), cb_csharp.text.toString(),
            cb_nodejs.text.toString() , cb_kotlin.text.toString(), cb_htmlcss.text.toString() , cb_scala.text.toString(), cb_java.text.toString(),
            cb_php.text.toString(), cb_XML.text.toString(), cb_bash.text.toString(), cb_cpp.text.toString(),cb_adobe.text.toString()
        )
        if(containsWordsForLanguages(input,listoflanguages)!=" ")
            languages=containsWordsForLanguages(input,listoflanguages)
        return languages
    }
    private fun addJob(link: String,input:String):String{

        var job:String
        if(link.contains("stipendije"))
            job="Stipendija"
         else
            job="Posao"
        val listofjobs= mutableListOf<String>(cb_praksa.text.toString(),"Praksi","Praksu","Internship")
        val jobfound=containsWord(input,listofjobs)
        if(jobfound!=" ")
            job=jobfound
        return job
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

