package com.example.studentdebut.Interface

import android.view.View

interface ItemClickListener {
    fun onClick(
        view: View?,
        position: Int,
        isLongClick: Boolean
    )
}