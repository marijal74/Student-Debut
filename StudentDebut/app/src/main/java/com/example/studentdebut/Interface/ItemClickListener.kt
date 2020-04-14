package com.example.studentdebut.Interface

import android.view.View

interface ItemClickListener {
    fun onClick(
        view: View?,
        position: Int,
        //TODO: izbrisati, nije nam potrebno
        isLongClick: Boolean
    )
}