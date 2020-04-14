package com.example.studentdebut

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.content.AsyncTaskLoader
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentdebut.Adapter.FeedAdapter
import com.example.studentdebut.Common.HTTPDataHandler
import com.example.studentdebut.Model.RSSObject
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_list_of_jobs.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder


class ListOfJobs : AppCompatActivity()  {

    private val RSS_link =  "https://startit.rs/poslovi/feed/"
    private val RSS_to_JSON_API = "https://api.rss2json.com/v1/api.json?rss_url="

     lateinit var result:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_jobs)

        toolbar.title="NEWS"
        //setSupportActionBar(toolbar)


        //postavlja RecyclerView
        val linearLayoutManager=LinearLayoutManager(baseContext,LinearLayoutManager.VERTICAL,false)
        RecyclerView.layoutManager = linearLayoutManager

        val uiScope = CoroutineScope(Dispatchers.IO)

        uiScope.launch {
            loadRSS()
        }

        getText()
    }

    /*private fun loadRSS() {
       val loadRSSAsync = object:  AsyncTask<String,String,String>(){

          internal var mDialog= Dialog(this@ListOfJobs);


           // mozda i ovde tip Main
           override fun onPostExecute(result: String?) {
               mDialog.dismiss()
               var rssObject:RSSObject
               rssObject= Gson().fromJson<RSSObject>(result,RSSObject::class.java!!)
               val adapter = FeedAdapter(rssObject,baseContext)
               RecyclerView.adapter=adapter
               adapter.notifyDataSetChanged()
           }

           //postaviti Coroutine ovde tip IO mislim da nece biti problema
           //jer ne menja UI
           override fun doInBackground(vararg params: String?): String {
             val result:String
             val http = HTTPDataHandler()
               result= http.GetHTTPDataHandler(params[0]).toString()
               return result
           }

           //TODO: obrisati, nije nam potrebna nikakva priprema pre dohvatanja sadrzaja
           override fun onPreExecute() {
             //  mDialog.
           }
       }*/

    private suspend fun loadRSS() {

        withContext(Dispatchers.IO) {
            // Make network call
           // val result:String
            val http = HTTPDataHandler()
            result = http.GetHTTPDataHandler("https://startit.rs").toString()

            //return@withContext result
            // parsing JSON is CPU intensive process, so run it on the Default Dispatcher
            /*withContext(Dispatchers.Default){
                val rssObject:RSSObject = Gson().fromJson<RSSObject>(result, RSSObject::class.java)
                withContext(Dispatchers.Main) {
                    val adapter = FeedAdapter(rssObject, baseContext)
                    RecyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }*/
        }



      //val url_get_data=StringBuilder(RSS_to_JSON_API)
        //url_get_data.append(RSS_link)
        //loadRSSAsync.execute(url_get_data.toString())

    }

   private fun getText(){

       //withContext(Dispatchers.Main) {
           val rssObject: RSSObject = Gson().fromJson<RSSObject>(result, RSSObject::class.java)
           val adapter = FeedAdapter(rssObject, baseContext)
           RecyclerView.adapter = adapter
           adapter.notifyDataSetChanged()
       }
   }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    //TODO: videti sta sa menijem (vrvt izbrisati?)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true

    }*/

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
     if(item.itemId == R.id.menu_refresh)
          loadRSS()
       return true
    }*/


