package com.widget.covid19_.ui.activites.intro

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color.green
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.rubengees.introduction.IntroductionBuilder
import com.rubengees.introduction.Slide
import com.widget.covid19_.R
import com.widget.covid19_.ui.activites.intro.slider.GuideSlide


class IntroActivity : AppCompatActivity() {
    var prefs : SharedPreferences ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        prefs = getSharedPreferences("Pref", MODE_PRIVATE);

        Handler().postDelayed(Runnable {
            if(checkFirstRun())
                IntroductionBuilder(this).withSlides(generateSlides()!!).introduceMyself()
            finish()

        }, 2500)
    }
    fun checkFirstRun() : Boolean {
        val isFirstRun: Boolean = prefs!!.getBoolean("isFirstRun", true)
        if (isFirstRun) {
            prefs?.edit()?.putBoolean("isFirstRun", false)?.apply()
            return true
        }
        return false
    }

    private fun generateSlides(): List<Slide>? {
        val result: MutableList<Slide> = ArrayList()
        result.add(Slide()
                .withTitleSize(18F)
                .withTitle("코로나 진행 상황을 그때 그때마다 확인하시나요?")
                .withDescription("코로나 상황을 매번 앱으로 혹은 웹으로 접속하는 건 번거롭죠.\n 그렇다면 휴대폰을 키면 바로 확인 가능한건 어떠신가요?")
                .withColorResource(R.color.gray)
                .withDescriptionSize(14F)
                .withImage(R.drawable.covid)
        )
        result.add(Slide()
                .withTitleSize(18F)
                .withDescriptionSize(14F)
                .withTitle("이제 편하게 이용하세요!")
                .withDescription("이제 홈 화면에서 간단하게 위젯을 추가해서, 확인해보세요!")
                .withColorResource(R.color.gray)
                .withImage(R.drawable.mask)
        )
        return result
    }
    override fun onBackPressed() {
        return;
    }
}