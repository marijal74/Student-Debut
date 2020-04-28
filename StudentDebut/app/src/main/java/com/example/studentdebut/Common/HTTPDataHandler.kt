package com.example.studentdebut.Common

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log.d
import android.widget.Toast
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.security.AccessController.getContext

class HTTPDataHandler {
    fun GetHTTPDataHandler(urlString: String?, c : Context): String? {
        try {
            val url = URL(urlString)
            val urlConnection =
                url.openConnection() as HttpURLConnection

           /* if (urlConnection.responseCode == -1){
                Handler(Looper.getMainLooper()).post(object : Runnable {
                    override fun run() {
                        Toast.makeText(c, "Nevalidan HTTP", Toast.LENGTH_LONG).show()
                    }
                })
            }*/
            if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {


               Handler(Looper.getMainLooper()).post(object : Runnable {
                    override fun run() {
                        Toast.makeText(c, "Uspesno dohvatanje stranice", Toast.LENGTH_LONG).show()
                    }
                })

                val inputStream: InputStream = BufferedInputStream(urlConnection.inputStream)
                val r = BufferedReader(InputStreamReader(inputStream))

                val sb = StringBuilder()
                //line sluzi kao akumulator za also
                var line: String? = ""


                // also je fja viseg reda (u Kotlinu: scope fja)
                //procitaj liniju takodje ono sto je procitano upisi u line
                while (r.readLine().also { line = it } != null)
                    sb.append(line)


                stream = sb.toString()

                urlConnection.disconnect()
            }
            else{
                Handler(Looper.getMainLooper()).post(object : Runnable {
                    override fun run() {
                        Toast.makeText(c, "Neuspesno dohvatanje stranice", Toast.LENGTH_LONG).show()
                    }
                })
            }
        } catch (e: Exception) {
            return null
        }
        return stream
    }

    //singlton vezan samo za ovu klasu
    companion object {
        var stream = ""
    }
}