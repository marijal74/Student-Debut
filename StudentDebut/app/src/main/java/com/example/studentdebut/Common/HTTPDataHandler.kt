package com.example.studentdebut.Common

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * klasa za povezivanje na internet
 */
class HTTPDataHandler {

    //promenljiva koja sluzi da korisnika obavesti ukoliko nijedna stranica nije dohvacena
    private var found = false

    /**
     * povezivanje na internet koriscenjem HttpURLConnection
     */
    fun GetHTTPDataHandler(urlString: String?, c : Context): String? {
        try {

            val url = URL(urlString)
            val urlConnection =
                url.openConnection() as HttpURLConnection

            //uspesno dohvacena stranica
            if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {

                found = true

                val inputStream: InputStream = BufferedInputStream(urlConnection.inputStream)
                val r = BufferedReader(InputStreamReader(inputStream))

                val sb = StringBuilder()
                //line sluzi kao akumulator za also
                var line: String?


                // also je fja viseg reda (u Kotlinu: scope fja)
                //procitaj liniju takodje ono sto je procitano upisi u line
                while (r.readLine().also { line = it } != null)
                    sb.append(line)


                stream = sb.toString()

                urlConnection.disconnect()
            }
        } catch (e: Exception) {
            return null
        }

        //obavestenje korisnika da nista nije dohvaceno
        if(!found)
            Handler(Looper.getMainLooper()).post { Toast.makeText(c, "Neuspesno dohvatanje stranica", Toast.LENGTH_LONG).show() }

        return stream
    }



    //singlton vezan samo za ovu klasu
    companion object {
        var stream = ""
    }
}