package com.example.studentdebut


import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentdebut.Adapter.FeedAdapter
import com.example.studentdebut.Common.HTTPDataHandler
import com.example.studentdebut.Database.JobsViewModel
import com.example.studentdebut.Database.ViewModelFactory
import com.example.studentdebut.Database.jobItem
import com.example.studentdebut.Model.RSSObject
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_list_of_jobs.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import java.lang.StringBuilder


// uvodna pricica kako sve ovo funkcionise na : https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#0
// introduction deo

class ListOfJobs : AppCompatActivity() {


    private val RSS_to_JSON_API = "https://api.rss2json.com/v1/api.json?rss_url="

    val rsslinks = mutableListOf(
        "https://startit.rs/poslovi/feed/",
        "https://startit.rs/poslovi/feed/?paged=2",
        "https://startit.rs/poslovi/feed/?paged=3",
        "https://startit.rs/poslovi/feed/?paged=4",
        "https://startit.rs/poslovi/feed/?paged=5",
        "https://www.helloworld.rs/rss/",
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_jobs)

        toolbar_listofjobs.title = "NEWS"
        setSupportActionBar(toolbar_listofjobs)

        //postavlja RecyclerView
        // ovo nam je inicijalno radila showResult fja i msm da je zbg toga bio onaj bug da se recycler view
        //vraca na pocetak, jer se vise puta postavljao adapter u forEach petlji

        val recyclerView = findViewById<RecyclerView>(R.id.RecyclerView)
        val adapter = FeedAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //postavljamo viewmodel
        //ovaj viewModelFactory necete naci u ofc dokumentaciji, nema ga iz nekog razloga, a neophodan je da
        // bi se uopste napravila instanca ViewModel-a
        viewModel =
            ViewModelProvider(this, ViewModelFactory(application)).get(JobsViewModel::class.java)

        //pravimo observer koji obavestava UI da je doslo do izmena

        viewModel.allJobs.observe(this, Observer { jobs ->
            // Update the cached copy of the words in the adapter.
            jobs?.let { adapter.setJobs(it) }
        })
        val uiScope = CoroutineScope(Dispatchers.IO)

        uiScope.launch {


            var rssObject: RSSObject


            rsslinks.forEach {

                val url_get_data = StringBuilder(RSS_to_JSON_API)
                url_get_data.append(it)


                val result = async {
                    makeConnection(url_get_data.toString())

                }.await()

                rssObject = async {
                    loadRSS(result)

                }.await()

                withContext(Default) {
                    rssObject.items.forEach {
                        //TODO ovde cete azurirati polja za kategorije, tj samo proslediti ono sto je pokupljeno iz MainActivity

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

                        viewModel.insert(ajob)
                        d("linkic", it.link)
                        // moj kod koji je formatirao content
                        //TODO or not, ubaciti ga na odg mesto u buducnosti
                        /*if(it.link.contains("startit")){
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
                            }*/
                    }
                }

            }
        }


    }


}



    //sve ostalo isto nadalje
    //TODO prebaciti ovaj posao u MainActivity

    private  fun makeConnection(link: String):String{

        val result:String
        val http = HTTPDataHandler()
        result = http.GetHTTPDataHandler(link).toString()

        return result
    }

    private suspend fun loadRSS(result:String):RSSObject{


        lateinit var rssObject: RSSObject

        withContext(Default){
            rssObject=Gson().fromJson<RSSObject>(result, RSSObject::class.java)
        }
        return rssObject


    }




