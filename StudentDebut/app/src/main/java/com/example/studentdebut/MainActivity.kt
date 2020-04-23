package com.example.studentdebut

import android.content.Intent
import android.graphics.Path
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.studentdebut.Common.HTTPDataHandler
import com.example.studentdebut.Database.JobsViewModel
import com.example.studentdebut.Database.jobItem
import com.example.studentdebut.Model.RSSObject
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import com.example.studentdebut.MyApp.Companion.done
import com.example.studentdebut.MyApp.Companion.ListOfJobItems
import kotlinx.android.synthetic.main.activity_options.*

class MainActivity() : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textSkipIntro.setOnClickListener() {
            val i = Intent(this, Options::class.java)
            startActivity(i)
        }
    }


}




