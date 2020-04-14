package com.example.studentdebut.Common

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HTTPDataHandler {
    fun GetHTTPDataHandler(urlString: String?): String? {
        try {
            val url = URL(urlString)
            val urlConnection =
                url.openConnection() as HttpURLConnection
            if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {

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