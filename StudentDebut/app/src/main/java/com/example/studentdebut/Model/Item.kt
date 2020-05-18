package com.example.studentdebut.Model

import android.os.Build
import android.util.Log.d
import androidx.annotation.RequiresApi
import org.jsoup.Jsoup

/**
 *klasa za jedan item u okviru rss feeda
 * title description i content promenjeni na var da bi bili mutabilni
 */
data class Item (var title:String, val pubDate:String, var link:String, val guid:String, val author: String,
                 val thumbnail: String, var description: String, var content: String, val enclosure:Any, val categories:List<String>){

    @RequiresApi(Build.VERSION_CODES.Q)
    fun filterContent(url:String){
        val tra=Translator()
        val des:String
        val conte:String
        val name:String
        val jsoup = Jsoup.parse(this.content)
        val jsoupd= Jsoup.parse(this.description)

        if(url.contains("matf")){
            conte=tra.trans(jsoup.text())
            des=tra.trans(jsoupd.text())
            name=tra.trans(this.title)
            d("matf","HelloFromMatf")
            }
        else {
            conte = jsoup.text()
            des=jsoupd.text()
            name=this.title
        }

        title=name
        content = conte
        description=des

    }
}