package com.widget.covid19_.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.StrictMode
import android.util.Log
import android.widget.RemoteViews
import com.widget.covid19_.R
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


class WidgetProvider : AppWidgetProvider() {
    private val MY_ACTION = "android.action.MY_ACTION"

    //    위젯 갱신 주기에 따라 위젯을 갱신할때 호출
    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        appWidgetIds?.forEach { appWidgetId ->
            val views: RemoteViews = addViews(context)

            getCovidInfo(views, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        var action = intent?.action
        if (action == MY_ACTION) {
            // TODO
        }
    }

    //    위젯이 처음 생성될때 호출되며, 동일한 위젯의 경우 처음 호출
    override fun onEnabled(context: Context?) {

        super.onEnabled(context)
    }

    //    위젯의 마지막 인스턴스가 제거될때 호출
    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
    }

    //    위젯이 사용자에 의해 제거될때 호출
    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
    }


    private fun addViews(context: Context?): RemoteViews {
        val views = RemoteViews(context?.packageName, R.layout.covid_widget)
        return views
    }

    private fun getCovidInfo(remoteViews: RemoteViews, appWidgetManager: AppWidgetManager?, appWidgetId : Int?) {

        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val dateNow : Calendar = Calendar.getInstance()

        var s = ""
        var date = ""

        if(dateNow.get(Calendar.MONTH) < 10){
            if(dateNow.get(dateNow.get(Calendar.DAY_OF_WEEK)) < 10){
                date = "${dateNow.get(Calendar.YEAR)}0${dateNow.get(Calendar.MONTH) + 1}0${dateNow.get(Calendar.DAY_OF_MONTH) - 1}"
                Log.d("TAG_date", date.toString())
            }else{
                date = "${dateNow.get(Calendar.YEAR)}0${dateNow.get(Calendar.MONTH) + 1}${dateNow.get(Calendar.DAY_OF_MONTH)}"
                Log.d("TAG_date", date.toString())
            }
        }else{
            if(dateNow.get(dateNow.get(Calendar.DAY_OF_WEEK)) < 10){
                date = "${dateNow.get(Calendar.YEAR)}${dateNow.get(Calendar.MONTH) + 1}0${dateNow.get(Calendar.DAY_OF_MONTH)}"
                Log.d("TAG_date", date.toString())
            }else{
                date = "${dateNow.get(Calendar.YEAR)}${dateNow.get(Calendar.MONTH) + 1}${dateNow.get(Calendar.DAY_OF_MONTH)}"
                Log.d("TAG_date", date.toString())
            }
        }


        val url = URL("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?serviceKey=2rZMtRbbzucWFYHemWkq182%2BW6Qf50DoMSZjz1oMxuPz0lqlnONNBJjcL6Q1T5ZksybLtLypASosXggxUNvs0g%3D%3D&pageNo=1&numOfRows=10&startCreateDt=${date}&endCreateDt=${date}")
        val dbf: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val db: DocumentBuilder = dbf.newDocumentBuilder()
        val doc = db.parse(InputSource(url.openStream()))
        doc.documentElement.normalize()

        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                val nodeList: NodeList = doc.getElementsByTagName("item")

                for (i in 0 until nodeList.length) {
                    val node: Node = nodeList.item(i)
                    val fstElmnt: Element = node as Element

                    val gubun: NodeList = fstElmnt.getElementsByTagName("gubun")
                    val incDec: NodeList = fstElmnt.getElementsByTagName("incDec")

                    if (gubun.item(0).childNodes.item(0).nodeValue == "합계") {
                        s += incDec.item(0).childNodes.item(0).nodeValue.toString()
                        remoteViews.setTextViewText(R.id.getIncText, s)

                        appWidgetManager?.updateAppWidget(appWidgetId!!, remoteViews)
                    }


                }
            }
        }
    }

}