package com.widget.covid19_.ui.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.widget.covid19_.R
import com.widget.covid19_.data.key.KeyData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.net.URL
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateText = view.findViewById<TextView>(R.id.getDataTimeTextView)
        val incText = view.findViewById<TextView>(R.id.incTextView)
        val incPlaceText = view.findViewById<TextView>(R.id.incPlaceTextView)

        loadWebView(view)
        getCovidInfo(incText, incPlaceText, dateText)

    }
    private fun getCovidInfo(incText : TextView, incPlaceText : TextView, dateText : TextView) {

        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val dateNow : Calendar = Calendar.getInstance()
        var date = ""

        if(onCheckTime(dateNow)){
            dateText.text = "${dateNow.get(Calendar.MONTH) + 1}월 ${dateNow.get(Calendar.DAY_OF_MONTH)}일 오전 11시 기준으로 수집된 데이터입니다."
            if(dateNow.get(Calendar.MONTH) < 10){
                if(dateNow.get(dateNow.get(Calendar.DAY_OF_MONTH)) < 10){
                    date = "${dateNow.get(Calendar.YEAR)}0${dateNow.get(Calendar.MONTH) + 1}0${dateNow.get(
                        Calendar.DAY_OF_MONTH)}"
                    Log.d("TAG_date", date.toString())
                }else{
                    date = "${dateNow.get(Calendar.YEAR)}0${dateNow.get(Calendar.MONTH) + 1}${dateNow.get(
                        Calendar.DAY_OF_MONTH)}"
                    Log.d("TAG_date", date.toString())
                }
            }else{
                if(dateNow.get(dateNow.get(Calendar.DAY_OF_MONTH)) < 10){
                    date = "${dateNow.get(Calendar.YEAR)}${dateNow.get(Calendar.MONTH) + 1}0${dateNow.get(
                        Calendar.DAY_OF_MONTH)}"
                    Log.d("TAG_date", date.toString())
                }else{
                    date = "${dateNow.get(Calendar.YEAR)}${dateNow.get(Calendar.MONTH) + 1}${dateNow.get(
                        Calendar.DAY_OF_MONTH)}"
                    Log.d("TAG_date", date.toString())
                }
            }
        }else{
            dateText.text = "${dateNow.get(Calendar.MONTH) + 1}월 ${dateNow.get(Calendar.DAY_OF_MONTH) - 1}일 오전 11시 기준으로 수집된 데이터입니다."
            if(dateNow.get(Calendar.MONTH) < 10){
                if(dateNow.get(dateNow.get(Calendar.DAY_OF_MONTH)) < 10){
                    date = "${dateNow.get(Calendar.YEAR)}0${dateNow.get(Calendar.MONTH) + 1}0${dateNow.get(
                        Calendar.DAY_OF_MONTH) - 1}"
                    Log.d("TAG_date", date.toString())
                }else{
                    date = "${dateNow.get(Calendar.YEAR)}0${dateNow.get(Calendar.MONTH) + 1}${dateNow.get(
                        Calendar.DAY_OF_MONTH) - 1}"
                    Log.d("TAG_date", date.toString())
                }
            }else{
                if(dateNow.get(dateNow.get(Calendar.DAY_OF_MONTH)) < 10){
                    date = "${dateNow.get(Calendar.YEAR)}${dateNow.get(Calendar.MONTH) + 1}0${dateNow.get(
                        Calendar.DAY_OF_MONTH) - 1}"
                    Log.d("TAG_date", date.toString())
                }else{
                    date = "${dateNow.get(Calendar.YEAR)}${dateNow.get(Calendar.MONTH) + 1}${dateNow.get(
                        Calendar.DAY_OF_MONTH) - 1}"
                    Log.d("TAG_date", date.toString())
                }
            }
        }


        val url = URL("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?serviceKey=2rZMtRbbzucWFYHemWkq182%2BW6Qf50DoMSZjz1oMxuPz0lqlnONNBJjcL6Q1T5ZksybLtLypASosXggxUNvs0g%3D%3D&pageNo=1&numOfRows=10&startCreateDt=${date}&endCreateDt=${date}")
        val dbf: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val db: DocumentBuilder = dbf.newDocumentBuilder()
        val doc = db.parse(InputSource(url.openStream()))
        doc.documentElement.normalize()

        val arrayData : ArrayList<KeyData> = ArrayList()

        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                val nodeList: NodeList = doc.getElementsByTagName("item")

                for (i in 0 until nodeList.length) {
                    val node: Node = nodeList.item(i)
                    val fstElmnt: Element = node as Element

                    val gubun: NodeList = fstElmnt.getElementsByTagName("gubun")
                    val incDec: NodeList = fstElmnt.getElementsByTagName("incDec")

                    if (gubun.item(0).childNodes.item(0).nodeValue == "합계") {
                        incText.text = incDec.item(0).childNodes.item(0).nodeValue.toString()
                    } else {
                        arrayData.add(KeyData(gubun.item(0).childNodes.item(0).nodeValue.toString()!!, incDec.item(0).childNodes.item(0).nodeValue.toString()))
                        Log.d("TAG", arrayData.toString())
                    }
                }
            }
        }
        tickClock(incText, incPlaceText , dateText, arrayData, 1)


    }

    private fun loadWebView(view : View){
        val mWebView = view.findViewById(R.id.loadView) as WebView
        mWebView.webViewClient = WebViewClient() // 클릭시 새창 안뜨게

        val mWebSettings = mWebView.settings //세부 세팅 등록
        mWebSettings.javaScriptEnabled = true // 웹페이지 자바스클비트 허용 여부
        mWebSettings.setSupportMultipleWindows(false) // 새창 띄우기 허용 여부
        mWebSettings.javaScriptCanOpenWindowsAutomatically = false // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSettings.loadWithOverviewMode = true // 메타태그 허용 여부
        mWebSettings.useWideViewPort = true // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(false) // 화면 줌 허용 여부
        mWebSettings.builtInZoomControls = false // 화면 확대 축소 허용 여부
        mWebSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN // 컨텐츠 사이즈 맞추기
        mWebSettings.cacheMode = WebSettings.LOAD_NO_CACHE // 브라우저 캐시 허용 여부
        mWebSettings.domStorageEnabled = true // 로컬저장소 허용 여부


        mWebView.loadUrl("https://search.naver.com/search.naver?where=news&sm=tab_jum&query=%EC%BD%94%EB%A1%9C%EB%82%98")
    }

    private fun tickClock(incText : TextView, incPlaceText : TextView, dateText : TextView, arrayData : ArrayList<KeyData>, index : Int?) {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                var indexData = index!!

                incPlaceText.text = arrayData[indexData - 1].key + " : " + arrayData[indexData - 1].increase

                indexData ++

                if(arrayData.size == indexData)
                    tickClock(incText, incPlaceText, dateText, arrayData, 1)
                else
                    tickClock(incText, incPlaceText, dateText, arrayData, indexData)

                cancel()
            }
        }.start()

    }

    private fun onCheckTime(cal : Calendar) : Boolean{
        if(cal.get(Calendar.HOUR_OF_DAY) >= 11) {
            return true
        }
        return false
    }
}