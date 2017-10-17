package jp.ict.muffin.otasukejuru.ui

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.activity.TimerActivity
import jp.ict.muffin.otasukejuru.view.CircleGraphView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class TimerActivityUI(private val time: Long) : AnkoComponent<TimerActivity> {
    private var isPushStartButton = false
    private lateinit var circleMini: FrameLayout
    private lateinit var circle: FrameLayout
    private lateinit var remainingHourText: TextView
    
    override fun createView(ui: AnkoContext<TimerActivity>): View = with(ui) {
        relativeLayout {
            backgroundColor = Color.argb(255, 251, 251, 240)
            lparams {
                height = matchParent
                width = matchParent
            }
            
            circle = frameLayout {
                id = R.id.circleFrame
            }.lparams {
                height = GlobalValue.displayWidth - 30
                width = GlobalValue.displayWidth - 30
                topMargin = 30
                leftMargin = 30
            }
            
            circleMini = frameLayout {
            }.lparams {
                translationZ = 2F
                height = GlobalValue.displayWidth / 3
                width = GlobalValue.displayWidth / 3
                topMargin = GlobalValue.displayWidth / 3 + 15
                leftMargin = GlobalValue.displayWidth / 3 + 15
                remainingHourText = textView {
                    text = ""
                    textSize = 40F
                }.lparams {
                    translationZ = 2F
                    width = wrapContent
                    height = wrapContent
                    topMargin = GlobalValue.displayWidth / 2 - 40
                    centerHorizontally()
                }
            }
            
            button("start") {
                onClick {
                    if (!isPushStartButton) {
                        startButtonClickListener(getContext())
                        isPushStartButton = true
                        
                    }
                }
            }.lparams {
                width = matchParent
                height = wrapContent
                below(R.id.circleFrame)
            }
        }
    }
    
    
    private fun startButtonClickListener(context: Context) {
        val params: ArrayList<HashMap<String, Int>> = java.util.ArrayList()
        
        val map = HashMap<String, Int>()
        map.put("color", Color.argb(255, 251, 251, 240))
        map.put("value", 60)
        params.add(map)
        val circleGraphView = CircleGraphView(context,
                params, true)
        circleMini.addView(circleGraphView)
        circleGraphView.startAnimation()
        
        startTimer(context, time)
        
        isPushStartButton = true
    }
    
    private fun drawCircle(context: Context, circle: FrameLayout, time: Long) {
        val drawTime: Long = if (time % 60 == 0L) {
            60L
        } else {
            time % 60L
        }
    
        val init = arrayListOf(true, false)
        val drawCircleTime = arrayListOf(60 - drawTime, drawTime)
        (0 until 2).forEach { i ->
            val params: ArrayList<HashMap<String, Int>> = java.util.ArrayList()
            val colors = if (i == 0) {
                arrayListOf(Color.argb(255, 251, 251, 240), Color.argb(255, 255, 0, 0))
            } else {
                arrayListOf(Color.argb(255, 251, 251, 240), Color.argb(255, 251, 251, 240))
            }
    
            (0 until 2).forEach { j ->
                val mapSI = HashMap<String, Int>()
                mapSI.put("color", colors[j])
                mapSI.put("value", drawCircleTime[j].toInt())
                params.add(mapSI)
            }
            
            val circleGraphView = CircleGraphView(context, params, init[i])
            circle.addView(circleGraphView)
            circleGraphView.startAnimation()
        }
    }
    
    private fun startTimer(context: Context, totalTime: Long) {
        val drawTime: Long = if (totalTime % 60 == 0L) {
            60L
        } else {
            totalTime % 60L
        }
        remainingHourText.text = ((totalTime - 1) / 60).toString()
        drawCircle(context, circle, drawTime)
        if (totalTime - drawTime != 0L) {
            Handler(Looper.getMainLooper()).postDelayed({
                startTimer(context, totalTime - drawTime)
            }, drawTime * 60 * 1000)
        }
    }
}