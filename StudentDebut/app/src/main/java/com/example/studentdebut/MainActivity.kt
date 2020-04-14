package com.example.studentdebut

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AddFilters.setOnClickListener(){
            d("AddFilters Button", "Clicked. New Activity started")
            startActivity(Intent(this, ListOfJobs::class.java))
        }
    }


}
