package com.widget.covid19_.ui.activites

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.widget.covid19_.R
import com.widget.covid19_.ui.activites.intro.IntroActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(Intent(this, IntroActivity::class.java))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "차후 공개될 예정입니다.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
}