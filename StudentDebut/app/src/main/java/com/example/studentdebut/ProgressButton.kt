package com.example.studentdebut

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

//nakon pritiska na dugme Primeni filtere obavestavamo korisnika
//da saceka par sekundi dok se ne pripreme podaci za njega
internal class ProgressButton(private val ct: Context, view: View) {

    private val layout: ConstraintLayout = view.findViewById(R.id.constraint_layout)
    private val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
    private val textView: TextView = view.findViewById(R.id.textView)

    fun buttonActivated() {
        progressBar.visibility = View.VISIBLE
        textView.text = "Molimo sacekajte..."
    }

    fun buttonFinished() {
        //layout.setBackgroundColor(cardView.resources.getColor(R.color.colorPrimary))
        layout.setBackgroundColor(ContextCompat.getColor(ct, R.color.colorPrimary))
        progressBar.visibility = View.GONE
        //textView.text = "Gotovo"
        textView.text = "Primeni filtere"
    }

}