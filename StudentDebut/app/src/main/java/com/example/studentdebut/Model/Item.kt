package com.example.studentdebut.Model

import android.icu.text.Transliterator
import android.icu.text.UnicodeSet
import android.os.Build
import android.util.Log.d
import androidx.annotation.RequiresApi
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

//title description i content promenjeni na var da bi bili mutabilni
data class Item (var title:String, val pubDate:String, var link:String, val guid:String, val author: String,
                 val thumbnail: String, var description: String, var content: String, val enclosure:kotlin.Any, val categories:List<String>){


    //TODO smanjiti br fja i hendlovati spec slucajeve

    @RequiresApi(Build.VERSION_CODES.Q)
    fun filterContent(url:String){
        var con=Translator()
        var des:String
        var name:String
        val jsoup = Jsoup.parse(this.content)
        if(url.contains("matf")){
            des=con.trans(jsoup.text())
            name=con.trans(this.title)
            d("matf","HelloFromMatf")
            }
        else {
             des = jsoup.text()
            name=this.title
        }
        title=name
        content = des
      //  Log.d("tekst", jsoup.text())

    }
    fun startitContent(){
        val jsoup = Jsoup.parse(this.content)
        //Log.d("tekst", this.description)

        val links : List<Element> = jsoup.select("p")

        var desc: String = ""
        if(links.isNotEmpty()) {

            //da li da ispisemo neki podrazumevani tekst za startit?
            desc = links.first().text()
        }
        d("startit","HelloFromStartIt")
        content = desc
    }

    fun matfContent(){
        val jsoup = Jsoup.parse(this.content)
        //Log.d("tekst", this.description)

        val links : List<Element> = jsoup.select("p")

        var desc: String = ""
        if(links.isNotEmpty()) {

            if(links.size>1){
                desc=links.first().text()
            }
        }


        content = desc
    }
    fun sljakaContent(){
        val jsoup = Jsoup.parse(this.content)
        val elements:List<Element> = jsoup.select("p")
        var sb:StringBuilder = StringBuilder()
        sb.append(elements[0].text())
        sb.append("\n")
        sb.append(elements[1].text())
        this.content=sb.toString()
        d("sljaka","HelloFromsljaka")

    }
    fun fonisContent(){
        val jsoup = Jsoup.parse(this.content)
        //Log.d("tekst", this.description)

        val links : List<Element> = jsoup.select("p")

        var desc: String = ""
        if(links.isNotEmpty()) {

            desc = links.first().text()
        }
        d("fon","HelloFromfon")
        content = desc
    }
    fun itposloviContent(){
        val jsoup = Jsoup.parse(this.content)
        val elements:List<Element> = jsoup.select("p")
        var sb:StringBuilder = StringBuilder()
        if(elements.isNotEmpty()){
            if(elements.size>1){
                sb.append(elements[0].text())
                sb.append("\n")
                sb.append(elements[1].text())
            }
            else{
                sb.append(elements[0].text())
            }
        }
        d("itposlovi","HelloFromitposlovi")
        this.content=sb.toString()
    }


    /*private  fun presentContent( item: Item, url:String) : Item{

        val jsoup = Jsoup.parse(item.content)
        Log.d("tekst", item.description)

        val links : List<Element> = jsoup.select("p")






        //TODO parsirati u zavisnosti od sajta (argument url)
        //TODO naziv firme, desc i see more



        if(links.isNotEmpty()){


            val sb = StringBuilder()

            var desc:String=""

            if(url.contains("startit")){

                //da li da ispisemo neki podrazumevani tekst za startit?
                desc=links.first().text()

            }
            else if(url.contains("matf")){
                if(links.size>1){
                    desc=links.first().text()
                }
            }
            else if(url.contains("helloworld")){
                d("ispis", "usao sam")
                d("ispis" , item.content)
            }
            else if(url.contains("sljaka")){
                d("ispis" , item.content)
                d("ispis", "usao sam")
            }
            else if(url.contains("fonis")){
                d("ispis" , item.content)
                d("ispis", "usao sam")
            }
            else if(url.contains("itposlovi")){
                d("ispis" , item.content)
                d("ispis", "usao sam")
            }



            sb.append(desc)
            d("desc", sb.toString())
            item.content = sb.toString()
        }


        return item


    }*/
}