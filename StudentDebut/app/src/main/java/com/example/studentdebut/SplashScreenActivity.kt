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

class SplashScreenActivity : AppCompatActivity() {

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

    val rsslinks = mutableListOf(

        "https://startit.rs/poslovi/feed/",
        "https://startit.rs/poslovi/feed/?paged=2",
        "https://startit.rs/poslovi/feed/?paged=3",
        "https://startit.rs/poslovi/feed/?paged=4",
        "https://startit.rs/poslovi/feed/?paged=5",
        "https://startit.rs/poslovi/feed/?paged=6",
        "https://startit.rs/poslovi/feed/?paged=7",
        "https://startit.rs/poslovi/feed/?paged=8",
        "https://www.helloworld.rs/rss/",
        "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2",
        "http://oglasi.matf.bg.ac.rs/?tag=praksa%26feed=rss2",
        "http://oglasi.matf.bg.ac.rs/?tag=stipendije%26feed=rss2",
        "http://www.sljaka.com/rss/itposlovi/",
        "http://www.itposlovi.info/rss/programeri/",
        "http://www.itposlovi.info/rss/dizajneri/"
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
        setContentView(R.layout.activity_splash_screen)

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
                var result = async {
                    makeConnection(url_get_data.toString())
                }.await()

                Log.d("resultic", result)

                rssObject = async {

                    loadRSS(result)

                }.await()
                var link = it
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

   /* fun containsWord(inputString: String, items: List<String?>): String {
        var thing:String=" "
        for (item in items) {
            inputString.indexOf(item,0,true)
            if (inputString.contains(item!!,true)) {
                thing=item
                break
            }
        }
        return thing
    }*/
    fun containsWord(inputString: String, items: List<String?>): String {
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
    fun containsWordsForLanguages(inputString: String, items: List<String?>): String {
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
