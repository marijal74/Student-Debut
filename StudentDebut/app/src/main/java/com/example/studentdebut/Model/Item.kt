package com.example.studentdebut.Model

//description i content promenjeni na var da bi bili mutabilni
data class Item (val title:String, val pubDate:String, var link:String, val guid:String, val author: String,
                 val thumbnail: String, var description: String, var content: String, val enclosure:Object, val categories:List<String>){

}