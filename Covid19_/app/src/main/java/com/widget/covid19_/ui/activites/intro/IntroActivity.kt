package com.widget.covid19_.ui.activites.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.widget.covid19_.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        Handler().postDelayed(Runnable {
            finish()
        }, 2500)
    }
}