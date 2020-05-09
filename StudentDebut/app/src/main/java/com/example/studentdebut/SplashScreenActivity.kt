package com.example.studentdebut

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.example.studentdebut.Common.HTTPDataHandler
import com.example.studentdebut.Database.JobsViewModel
import com.example.studentdebut.Database.jobItem
import com.example.studentdebut.Model.RSSObject
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_options.*
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlinx.coroutines.*

//prva aktivnost koja se pokrece, ucitavanje podataka sa interneta
class SplashScreenActivity : AppCompatActivity() {

    //stranice sa oglasima sa kojih uzimamo podatke
    private val RSS_to_JSON_API = "https://api.rss2json.com/v1/api.json?rss_url="

    /*


      "https://startit.rs/poslovi/feed/",
      "https://startit.rs/poslovi/feed/?paged=2",
      "https://startit.rs/poslovi/feed/?paged=3",
      "https://startit.rs/poslovi/feed/?paged=4",
      "https://startit.rs/poslovi/feed/?paged=5",
      "https://startit.rs/poslovi/feed/?paged=6",
      "https://startit.rs/poslovi/feed/?paged=7",
      "https://startit.rs/poslovi/feed/?paged=8"


      "https://www.helloworld.rs/rss/",
      "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2",
      "http://oglasi.matf.bg.ac.rs/?tag=praksa%26feed=rss2",
      "http://oglasi.matf.bg.ac.rs/?tag=stipendije%26feed=rss2",
      "http://www.sljaka.com/rss/itposlovi/",
      "http://www.itposlovi.info/rss/programeri/",
      "http://www.itposlovi.info/rss/dizajneri/"



      "http://oglasi.matf.bg.ac.rs/?feed=rss2",
        "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2%26paged=2",
        "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2%26paged=3",
        "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2%26paged=4",
        "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2%26paged=5",
        "http://oglasi.matf.bg.ac.rs/?tag=stipendije%26feed=rss2"

    */

    private val rsslinks = mutableListOf(


        "https://startit.rs/poslovi/feed/",
        "https://startit.rs/poslovi/feed/?paged=2",
        "https://startit.rs/poslovi/feed/?paged=3",
        "https://www.helloworld.rs/rss/",
        "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2",
        "http://oglasi.matf.bg.ac.rs/?tag=praksa%26feed=rss2",
        "http://oglasi.matf.bg.ac.rs/?tag=stipendije%26feed=rss2",
        "http://www.sljaka.com/rss/itposlovi/",
        "http://www.itposlovi.info/rss/programeri/",
        "http://www.itposlovi.info/rss/dizajneri/"
    )

    //povezivanje na internet
    private fun makeConnection(link: String): String {

        val result: String
        val http = HTTPDataHandler()
        result = http.GetHTTPDataHandler(link, this.applicationContext).toString()

        return result
    }

    //ucitavanje RSS feedova
    private suspend fun loadRSS(result: String): RSSObject?{


        var rssObject: RSSObject?=null

        withContext(Dispatchers.Default) {
            rssObject = Gson().fromJson<RSSObject>(result, RSSObject::class.java)

        }

        return rssObject
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //animacija koja treba da zamaskira vreme koje korisnik mora da ceka
        logo_text.alpha = 0f
        logo_text.animate().setDuration(3000).alpha(1f)

        logo_pic.alpha = 0f
        logo_pic.animate().setDuration(3000).alpha(1f).withEndAction{
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        val uiScope = CoroutineScope(Dispatchers.IO)

        uiScope.launch {


            var rssObject: RSSObject?


            rsslinks.forEach {

                val url_get_data = StringBuilder(RSS_to_JSON_API)
                url_get_data.append(it)
                Log.d("url_get_data", url_get_data.toString())

                val result = async {
                    makeConnection(url_get_data.toString())
                }.await()

                Log.d("resultic", result)

                rssObject = async {

                    loadRSS(result)

                }.await()
                val link = it
                //iz rss u job item
                if (rssObject != null) {
                    Log.d("done", MyApp.done.toString())
                    if (!rssObject!!.items.isEmpty()) {
                        withContext(Dispatchers.Default) {
                            rssObject!!.items.forEach {
                                Log.d("linkk", link)
                                //fja filterContent vadi tekst iz html-a
                                it.filterContent(link)
                                val positionf = addPosition(it.title + " " + it.content + " " + it.description)
                                val languagef = addLanguages(it.title + " " + it.content + " " + it.description+ " "+it.categories)
                                val jobf=addJob(link,it.title + " " + it.content + " " + it.description)
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
                                Log.d("jobic", ajob.toString())
                                println("COOOOOONTEEENT")
                                println("JOOOOOOOOOOOOOOB $jobf")

                                println("          POSITOOOOOOOOOON $positionf")
                                println("          LANGUAGGGGGGGGGG $languagef")

                                // d("item", ajob.toString())
                                MyApp.ListOfJobItems.add(ajob)
                                MyApp.ListToPopulateDB.add(ajob)

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

    //pomocna fja, prolazi kroz tekst i nalazi prvo pojavljivanje reci
    private fun containsWord(inputString: String, items: List<String?>): String {
        var thing:String=" "
        var firstposition=Int.MAX_VALUE
        var position:Int
        for (item in items) {
            position=inputString.indexOf(item!!,0,true)
            if(position>=0) {
                if (position < firstposition) {
                    firstposition = position
                    thing = item
                }
            }
        }
        return thing
    }

    //pomocna fja, prolazi kroz tekst i nalazi koje se tehnologije pojavljuju
    private fun containsWordsForLanguages(inputString: String, items: List<String?>): String {
        var found=false
        val things = StringBuilder()
        for (item in items) {

            if (inputString.contains(item!!,true)) {
                things.append(item)
                    .append("_")
                found=true
            }
        }
        if(found)
            return things.toString()
        else return " "

    }

    //dodaje u bazu za koju je poziciju izdat oglas
    private fun addPosition(input:String):String{

        var position="Druge pozicije"
        val designer=mutableListOf("Designer","Dizajner","Dizajneru","Dizajnera")
        val administrator=mutableListOf("Administrator","Administratora","Administratoru")
        val programmer= mutableListOf("Programmer","programer","Programeru","Programera")
        val tutor= mutableListOf("Tutor","Saradnika","Saradniku","Saradnik","Predavač","Predavaču","Predavača")

        val listofpositions= mutableListOf<String>("Developer","Analyst",
                                                                     "Technical lead", "Engineer", "Manager",
                                                                     "Marketing", "Scientist")
        listofpositions.addAll(designer)
        listofpositions.addAll(administrator)
        listofpositions.addAll(programmer)
        listofpositions.addAll(tutor)


        val found_value=containsWord(input,listofpositions)
        if(found_value!=" ") {
            if (designer.contains(found_value))
                position = "Designer"
            else if (administrator.contains(found_value))
                position = "Administrator"
            else if (programmer.contains(found_value))
                position = "Programmer"
            else if (tutor.contains(found_value))
                position = "Tutor"
            else if (found_value == "Scientist")
                position="Data/Research scientist"
            else position = found_value
        }
        return position
    }

    //dodaje u bazu koji se tehnologije traze u oglasu
    private fun addLanguages(input:String): String {
        var languages="Drugi jezici"
        val listoflanguages= mutableListOf<String>("JavaScript", ".NET", "Python",  "SQL",
                                                                     "Vue","jQuery","WordPress", "C#",
                                                                     "NodeJS" , "Kotlin", "HTML" , "Scala",
                                                                     "Java", "PHP", "XML", "Bash", "C++", "Adobe",
                                                                     "Matlab", "React")
        if(containsWordsForLanguages(input,listoflanguages)!=" ")
            languages=containsWordsForLanguages(input,listoflanguages)
        return languages
    }

    //dodaje u bazu da li je oglas posao/praksa/stipendija
    private fun addJob(link: String,input:String):String{

        var job:String
        if(link.contains("stipendije"))
            job="Stipendija"
        else
            job="Posao"
        val listofjobs= mutableListOf<String>("Praksa","Praksi","Praksu","Prakse","Internship")
        val jobfound=containsWord(input,listofjobs)
        if(jobfound!=" ")
            job=jobfound
        return job
    }
}
