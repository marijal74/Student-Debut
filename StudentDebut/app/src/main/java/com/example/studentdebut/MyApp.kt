package com.example.studentdebut

import android.app.Application
import com.example.studentdebut.Database.jobItem

class MyApp: Application() {

    companion object {
        var done= false
        val ListOfJobItems = ArrayList<jobItem>()
        var filtersJob = mutableListOf<String>()
        var filtersPosition = mutableListOf<String>()
        var filtersLanguage = mutableListOf<String>()

    }



}