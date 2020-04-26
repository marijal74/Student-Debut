package com.example.studentdebut.Model

import android.icu.text.Transliterator
import android.os.Build
import android.util.Log.d
import androidx.annotation.RequiresApi

class Translator {
    var SERBAIN_TO_LATIN = "Serbian-Latin/BGN"

    @RequiresApi(Build.VERSION_CODES.Q)
    fun trans(words: String):String{
        val bulgarianToLatin = Transliterator.getInstance(SERBAIN_TO_LATIN)
        val result = bulgarianToLatin.transliterate(words)
        return result
        d("SERBAIN:",result)
    }
}