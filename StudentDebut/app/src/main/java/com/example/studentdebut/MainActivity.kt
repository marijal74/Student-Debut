package com.example.studentdebut

import android.content.Context
import android.content.Intent
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
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.example.studentdebut.MyApp.Companion.ListToPopulateDB

class MainActivity : AppCompatActivity() {

    //popunjavanje IntroSlidera
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
        val tryAgain = findViewById<TextView>(R.id.try_again)

        //obavestenje korisniku da nije povezan na internet kada udje u aplikaciju
        if (!isNetworkAvailable) {

            tryAgain.visibility = View.VISIBLE
            textSkipIntro.visibility = View.GONE
            buttonNext.visibility = View.GONE
            Toast.makeText(this, "Niste povezani na internet", Toast.LENGTH_LONG).show()
            tryAgain.setOnClickListener { recreate() }

        }
        //inace se prikazuju IntroSlideri koji korisnika upoznaju sa aplikacijom
        else {
            tryAgain.visibility = View.GONE

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

            textSkipIntro.setOnClickListener {
                Intent(applicationContext, Options::class.java).also {
                    startActivity(it)

                }
            }
        }
    }

    //fja koja proverava da li korisnik ima aktivnu vezu sa internetom
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
            }
            //zbog ranijih verzija androida ostavljam ovaj else iako su ove stvari depricated
            //kod u if u kome nisu depricated ne radi za starije verzije androida
            else {
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

    //indikatori za IntroSlidere koji govore do koje slike je korisnik stigao(tri tackice ispod slika)
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







