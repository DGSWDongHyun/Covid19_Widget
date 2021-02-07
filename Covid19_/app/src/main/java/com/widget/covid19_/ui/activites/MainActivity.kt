package com.widget.covid19_.ui.activites

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
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

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("종료하시겠습니까?")
            .setMessage("앱을 종료하더라도 위젯으로 코로나 현황을 확인 할 수 있습니다.")
            .setPositiveButton("네") { dialog: DialogInterface?, which: Int ->
                finish()
            }
            .setNegativeButton("아니오") { dialog: DialogInterface?, which: Int ->

            }.show()
    }
}