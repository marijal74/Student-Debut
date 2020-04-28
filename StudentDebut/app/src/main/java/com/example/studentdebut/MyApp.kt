package com.example.studentdebut

import android.app.Application
import com.example.studentdebut.Database.jobItem

class MyApp: Application() {

    companion object {
        var done= false
        val ListOfJobItems = ArrayList<jobItem>()
        val filtersJob = mutableListOf<String>()
        val filtersPosition = mutableListOf<String>()
        val filtersLanguage = mutableListOf<String>()

    }



}