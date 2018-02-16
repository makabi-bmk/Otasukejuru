package jp.ict.muffin.otasukejuru.ui

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.activity.InputProgressActivity
import jp.ict.muffin.otasukejuru.activity.TimerActivity
import jp.ict.muffin.otasukejuru.view.CircleGraphView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class TimerActivityUI(private val time: Long, private val index: Int = -1) :
        AnkoComponent<TimerActivity> {
    private var isPushStartButton = false
    private lateinit var circleMini: FrameLayout
    private lateinit var circle: FrameLayout
    private lateinit var remainingHourText: TextView
    private var countClick = 0
    
    override fun createView(ui: AnkoContext<TimerActivity>): View = with(ui) {
        relativeLayout {
            backgroundColor = ContextCompat.getColor(context, R.color.back)
            lparams {
                height = matchParent
                width = matchParent
            }
            
            toolbar {
                id = R.id.ankoToolbar
                backgroundColor = ContextCompat.getColor(context, R.color.colorPrimary)
            }.lparams {
                width = matchParent
                height = wrapContent
            }
            
            circle = frameLayout {
                id = R.id.circleFrame
            }.lparams {
                height = GlobalValue.displayWidth - 30
                width = GlobalValue.displayWidth - 30
                topMargin = 30
                leftMargin = 30
                below(R.id.ankoToolbar)
            }
            
            circleMini = frameLayout {
            }.lparams {
                translationZ = 2F
                height = GlobalValue.displayWidth / 3
                width = GlobalValue.displayWidth / 3
                topMargin = GlobalValue.displayWidth / 3 + 15
                leftMargin = GlobalValue.displayWidth / 3 + 15
                below(R.id.ankoToolbar)
                
                remainingHourText = textView {
                    text = ""
                    textSize = 40F
                }.lparams {
                    translationZ = 2F
                    width = wrapContent
                    height = wrapContent
                    topMargin = GlobalValue.displayWidth / 2 - 40
                    centerHorizontally()
                    below(R.id.ankoToolbar)
                }
            }
            
            
            button("一時停止") {
                textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                textSize = 30f
                backgroundColor = Color.argb(0, 0, 0, 0)
                onClick {
                    countClick++
                    text = if (countClick % 2 == 0) {
                        "一時停止"
                    } else {
                        "再開"
                    }
                    startTimerInit(getContext())
                }
            }.lparams {
                below(R.id.circleFrame)
                width = wrapContent
                height = wrapContent
                centerHorizontally()
            }
            
            startTimerInit(context)
        }
    }
    
    
    private fun startTimerInit(context: Context) {
        val params: ArrayList<HashMap<String, Int>> = java.util.ArrayList()
        val map = HashMap<String, Int>()
        val back = ContextCompat.getColor(context, R.color.back)
        
        map.also {
            it["color"] = back
            it["value"] = 60
        }
        params.add(map)
        val circleGraphView = CircleGraphView(context, params, true)
        circleMini.addView(circleGraphView)
        circleGraphView.startAnimation()
        
        startTimer(context, time, index)
        isPushStartButton = true
    }
    
    private fun drawCircle(context: Context, circle: FrameLayout, time: Long) {
        if (time == 0L) {
            return
        }
        
        val drawTime: Long = if (time % 60 == 0L) {
            60L
        } else {
            time % 60L
        }
        
        val init = arrayListOf(true, false)
        (0 until 2).forEach {
            val params: ArrayList<HashMap<String, Int>> = java.util.ArrayList()
            val backColor = ContextCompat.getColor(context, R.color.back)
            val redColor = ContextCompat.getColor(context, R.color.mostPriority)
            val intervalColor = ContextCompat.getColor(context, R.color.colorPrimary)
            
            val colors: ArrayList<Int> = arrayListOf(backColor)
            val drawCircleTime: ArrayList<Long> = arrayListOf(60 - drawTime)
            if (it == 0) {
                val focusTime = GlobalValue.focusTimeG
                val intervalTime = GlobalValue.intervalTimeG
                
                var tmpTime = 0L
                while (true) {
                    if (drawTime <= tmpTime + focusTime) {
                        drawCircleTime.add(drawTime - tmpTime)
                        colors.add(redColor)
                        break
                    } else if (focusTime != 0L) {
                        drawCircleTime.add(focusTime)
                        colors.add(redColor)
                    } else {
                    }
                    tmpTime += focusTime
                    
                    if (drawTime <= tmpTime + intervalTime) {
                        drawCircleTime.add(drawTime - tmpTime)
                        colors.add(intervalColor)
                        break
                    } else if (intervalTime != 0L) {
                        drawCircleTime.add(intervalTime)
                        colors.add(intervalColor)
                    } else {
                    }
                    tmpTime += intervalTime
                }
            } else {
                drawCircleTime.add(drawTime)
                colors.add(backColor)
            }
            
            (0 until colors.size).forEach { i ->
                val mapSI = HashMap<String, Int>()
                mapSI["color"] = colors[i]
                mapSI["value"] = drawCircleTime[i].toInt()
                params.add(mapSI)
            }
            
            val circleGraphView = CircleGraphView(context, params, init[it])
            circle.addView(circleGraphView)
            circleGraphView.startAnimation()
        }
    }
    
    private fun startTimer(context: Context, totalTime: Long, index: Int = -1) {
        if (totalTime == 0L) {
            return
        }
        
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
        } else if (index != -1) {
            context.startActivity<InputProgressActivity>("index" to index)
        }
    }
}