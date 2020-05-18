package com.example.studentdebut

import android.app.Application
import com.example.studentdebut.Database.jobItem

/**
 * globalne promenljive za liste korisnikovih izbora prilikom biranja filtera po svakom pitanju
 */
class MyApp: Application() {

    companion object {
        var done= false
        var ListOfJobItems = ArrayList<jobItem>()
        var ListToPopulateDB = ArrayList<jobItem>()
        var filtersJob = mutableListOf<String>()
        var filtersPosition = mutableListOf<String>()
        var filtersLanguage = mutableListOf<String>()
    }
}