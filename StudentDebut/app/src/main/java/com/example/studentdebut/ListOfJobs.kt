package com.example.studentdebut


import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentdebut.Adapter.FeedAdapter
import com.example.studentdebut.Common.HTTPDataHandler
import com.example.studentdebut.Model.Item
import com.example.studentdebut.Model.RSSObject
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_list_of_jobs.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.lang.StringBuilder


class ListOfJobs : AppCompatActivity()  {


    private val RSS_to_JSON_API = "https://api.rss2json.com/v1/api.json?rss_url="

    val rsslinks = mutableListOf("https://startit.rs/poslovi/feed/",  "https://startit.rs/poslovi/feed/?paged=2",
        "https://startit.rs/poslovi/feed/?paged=3","https://startit.rs/poslovi/feed/?paged=4",
        "https://startit.rs/poslovi/feed/?paged=5",
        "https://www.helloworld.rs/rss/",
        "http://oglasi.matf.bg.ac.rs/?feed=rss2", "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2%26paged=2",
        "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2%26paged=3", "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2%26paged=4",
        "http://oglasi.matf.bg.ac.rs/?tag=poslovi%26feed=rss2%26paged=5",
        "http://www.sljaka.com/rss/itposlovi/",
        "https://fonis.rs/category/posao/feed/", "https://fonis.rs/category/praksa/feed/",
        "https://fonis.rs/category/praksa/feed/?paged=2",
        "http://www.itposlovi.info/rss/programeri/", "http://www.itposlovi.info/rss/dizajneri/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_jobs)

        toolbar_listofjobs.title="NEWS"
        setSupportActionBar(toolbar_listofjobs)


        //postavlja RecyclerView
        val linearLayoutManager=LinearLayoutManager(baseContext,LinearLayoutManager.VERTICAL,false)
        RecyclerView.layoutManager = linearLayoutManager


        val uiScope = CoroutineScope(Dispatchers.IO)

        uiScope.launch {



            var rssObject:RSSObject

            val listOfItems= mutableListOf<Item>()


            rsslinks.forEach{

                val url_get_data=StringBuilder(RSS_to_JSON_API)
                url_get_data.append(it)


                val result=async{
                    makeConnection(url_get_data.toString())

                }.await()

                rssObject = async{
                    loadRSS(result)
                }.await()

                listOfItems.addAll(rssObject.items)
                d("predKorutinu", "pre poziva")
                showResult(listOfItems, rssObject.feed.url)



            }

        }


    }


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

    private suspend fun showResult(rssObject: MutableList<Item>, url : String ) {
        withContext(Main) {
            val adapter = FeedAdapter(rssObject, baseContext, url)
            RecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }
}





