package jp.ict.muffin.otasukejuru

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import org.jetbrains.anko.*

class TimerFragmentUI : AnkoComponent<TimerFragment> {
    private var isPushStartButton = false
    private lateinit var circleMini: FrameLayout
    private lateinit var circle: FrameLayout
    private lateinit var editTime: EditText
    private lateinit var remainingHourText: TextView
    
    override fun createView(ui: AnkoContext<TimerFragment>): View = with(ui) {
        relativeLayout {
            lparams {
                height = matchParent
                width = matchParent
            }
            circle = frameLayout {
                backgroundColor = Color.argb(0, 0, 0, 0)
                id = 1
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
            editTime = editText {
                inputType = InputType.TYPE_CLASS_NUMBER
                id = 2
            }.lparams {
                height = wrapContent
                width = matchParent
                below(circle)
            }
            button("start") {
                height = wrapContent
                width = matchParent
            }.lparams {
                below(editTime)
            }.setOnClickListener {
                startButtonClickListener(context)
            }
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
                Log.d("startTimer", drawTime.toString())
            }, drawTime * 60 * 1000)
        }
        
    }
    
    private fun startButtonClickListener(context: Context) {
        if (!isPushStartButton && editTime.text.toString() != "") {
            val time = editTime.text.toString().toLong()
            editTime.text.clear()
            editTime.clearFocus()
            
            val circleGraphView = CircleGraphView(context, Color.argb(255, 251, 251, 240), 60, true)
            circleMini.addView(circleGraphView)
            circleGraphView.startAnimation()
            
            startTimer(context, time)
            
            isPushStartButton = true
        }
    }
    
    private fun drawCircle(context: Context, circle: FrameLayout, time: Long) {
        val circleGraphView = CircleGraphView(context, Color.argb(255, 255, 0, 0), time, true)
        circle.addView(circleGraphView)
        circleGraphView.startAnimation()
        
        val circleGraphView1 = CircleGraphView(context, Color.argb(255, 251, 251, 240), time, false)
        circle.addView(circleGraphView1)
        circleGraphView1.startAnimation()
    }
}
