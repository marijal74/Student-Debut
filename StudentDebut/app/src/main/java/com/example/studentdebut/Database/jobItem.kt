package com.example.studentdebut.Database

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * instanca baze
 */
@Entity (tableName="jobs_table")
data class jobItem(

    @PrimaryKey(autoGenerate = true)
    var id:Int,

    val title: String,
    var pubDate:String,
    var link:String,
    var description: String,
    var content: String,
    var job: String?,
    var position:String?,
    var language: String?
){


    init{
        id=0

    }



}