package com.example.studentdebut

import android.app.Application
import com.example.studentdebut.Database.jobItem

class MyApp: Application() {

    companion object {
        var done= false
        val ListOfJobItems = ArrayList<jobItem>()
        val filtersJob = mutableListOf<CharSequence>()
        val filtersPosition = mutableListOf<CharSequence>()
        val filtersLanguage = mutableListOf<CharSequence>()

    }



}