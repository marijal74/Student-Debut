package com.example.studentdebut.Model

import android.icu.text.Transliterator
import android.os.Build
import androidx.annotation.RequiresApi

//prebacuje cirilicu u latinicu zbog sajtova matfa
class Translator {
    private var SERBIAN_TO_LATIN = "Serbian-Latin/BGN"

    @RequiresApi(Build.VERSION_CODES.Q)
    fun trans(words: String):String{
        val serbianToLatin = Transliterator.getInstance(SERBIAN_TO_LATIN)
        return serbianToLatin.transliterate(words)
    }

}