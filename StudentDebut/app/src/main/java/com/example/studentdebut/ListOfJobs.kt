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
import java.lang.StringBuilder


class ListOfJobs : AppCompatActivity()  {

    private val RSS_link =  "https://startit.rs/poslovi/feed/"
    private val RSS_to_JSON_API = "https://api.rss2json.com/v1/api.json?rss_url="

    val rsslinks = mutableListOf("https://startit.rs/poslovi/feed/", "https://fonis.rs/category/praksa/feed/",
                                 "http://oglasi.matf.bg.ac.rs/?feed=rss2", "http://www.sljaka.com/rss/itposlovi/",
                                 "https://www.helloworld.rs/rss/", "http://www.itposlovi.info/rss/all/")

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

            //var url_get_data=StringBuilder(RSS_to_JSON_API)

            var rssObject:RSSObject

            var listOfItems= mutableListOf<Item>()


            rsslinks.forEach{

                var url_get_data=StringBuilder(RSS_to_JSON_API)
                url_get_data.append(it)

                d("dib", url_get_data.toString())

                val result=async{
                    makeConnection(url_get_data.toString())
                }.await()
                d("pechu", result)
                rssObject = async{
                    loadRSS(result)
                }.await()
                d("pechu", rssObject.toString())
                listOfItems.addAll(rssObject.items)
                //d("istina", t.toString())
                //d("ispis", rssObject.items.toString())
                //d("listica", listOfItems.toString())
                //rssObject.items = listOfItems
                showResult(listOfItems)
                d("rssi", rssObject.items.size.toString())
            }

        }


    }


    private suspend fun makeConnection(link: String):String{

        val result:String
        val http = HTTPDataHandler()
        result = http.GetHTTPDataHandler(link).toString()
        println("debug: $result")
        return result
    }

    private suspend fun loadRSS(result:String):RSSObject{


        lateinit var rssObject: RSSObject

        withContext(Default){
            rssObject=Gson().fromJson<RSSObject>(result, RSSObject::class.java)
        }
        return rssObject




    }

    private suspend fun showResult(rssObject: MutableList<Item>) {
        withContext(Main) {
            val adapter = FeedAdapter(rssObject, baseContext)
            RecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }
}





