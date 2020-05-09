package com.example.studentdebut

import android.content.Context
import android.content.Intent
import android.graphics.Path
import android.media.Image
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
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
import com.example.studentdebut.MyApp.Companion.ListToPopulateDB
import com.example.studentdebut.MyApp.Companion.ListOfJobItems

class MainActivity() : AppCompatActivity() {

    private val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlide(
                "Poslovi",
                "Ponude poslova iz vise oblasti informatike",
                R.drawable.job

            ),
            IntroSlide(
                "Prakse",
                "Prakse iz razlicitih oblasti informatike",
                R.drawable.internship
            ),
            IntroSlide(
                "Stipendije",
                "Ponuda stipendija iz oblasti informatike i matematike",
                R.drawable.scholarship
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        d("LISTTODB",ListToPopulateDB.toString())
        val TryAgain = findViewById<TextView>(R.id.try_again)
        if (!isNetworkAvailable) {

            TryAgain.visibility = View.VISIBLE
            textSkipIntro.visibility = View.GONE
            buttonNext.visibility = View.GONE
            Toast.makeText(this, "Niste povezani na internet", Toast.LENGTH_LONG).show()
            TryAgain.setOnClickListener { recreate() }

        } else {
            TryAgain.visibility = View.GONE

            introSliderViewPager.adapter = introSliderAdapter
            setupIndicators()
            setCurrentIndicator(0)
            introSliderViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicator(position)
                }

            })

            buttonNext.setOnClickListener {
                if (introSliderViewPager.currentItem + 1 < introSliderAdapter.itemCount) {
                    introSliderViewPager.currentItem += 1
                } else {
                    Intent(applicationContext, Options::class.java).also {
                        startActivity(it)

                    }
                }
            }
            textSkipIntro.setOnClickListener() {
                Intent(applicationContext, Options::class.java).also {
                    startActivity(it)

                }
            }
        }
    }


    private val isNetworkAvailable: Boolean
        get() {
            var result = false
            val connectivityManager = (applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }

                    }
                }
            }

            return result
        }

    private fun setupIndicators(){
        val indicators = arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for(i in indicators.indices){
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            indicatorsContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int){
        val childCount = indicatorsContainer.childCount
        for(i in 0 until childCount){
            val imageView = indicatorsContainer[i] as ImageView
            if(i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active
                    )
                )
            }else{

                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )

            }
        }
    }
}







