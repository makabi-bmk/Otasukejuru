package jp.ict.muffin.otasukejuru

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import org.jetbrains.anko.*


class TimerActivityUI(private val time: Long) : AnkoComponent<TimerActivity> {
    private var isPushStartButton = false
    private lateinit var circleMini: FrameLayout
    private lateinit var circle: FrameLayout
    private lateinit var remainingHourText: TextView
    
    override fun createView(ui: AnkoContext<TimerActivity>): View = with(ui) {
        linearLayout {
            backgroundColor = Color.argb(255, 251, 251, 240)
            relativeLayout {
                lparams {
                    height = matchParent
                    width = matchParent
                }
                
                circle = frameLayout {
                    backgroundColor = Color.argb(0, 0, 0, 0)
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
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.circleFrame)
                }.setOnClickListener {
                    startButtonClickListener(context)
                }
            }
        }
    }
    
    
    private fun startButtonClickListener(context: Context) {
        if (!isPushStartButton) {
            
            val circleGraphView = CircleGraphView(context,
                    Color.argb(255, 251, 251, 240), 60, true)
            circleMini.addView(circleGraphView)
            circleGraphView.startAnimation()
            
            startTimer(context, time)
            
            isPushStartButton = true
        }
    }
    
    private fun drawCircle(context: Context, circle: FrameLayout, time: Long) {
        val colors = arrayListOf(Color.argb(255, 255, 0, 0), Color.argb(255, 251, 251, 240))
        val init = arrayListOf(true, false)
        (0 until 2).forEach {
            val circleGraphView = CircleGraphView(context, colors[it], time, init[it])
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