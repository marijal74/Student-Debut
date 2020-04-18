package com.example.studentdebut.Database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.studentdebut.Model.Item

@Entity (tableName="jobs_table")
data class Entity (@PrimaryKey(autoGenerate = true)
                    var id:Integer,
                    /*@Embedded(prefix = "item_")
                   var item: Item,*/
                   var job: String,
                   var stationed: String,
                   var position:String,
                   var language:String){

    init {
        //item=item
        job = ""
        stationed = ""
        position = ""
        language = ""
    }
}