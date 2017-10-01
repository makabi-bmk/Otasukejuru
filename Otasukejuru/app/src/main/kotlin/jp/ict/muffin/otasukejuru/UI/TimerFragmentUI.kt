package jp.ict.muffin.otasukejuru

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.view.View
import android.widget.FrameLayout
import jp.ict.muffin.otasukejuru.Object.GlobalValue
import jp.ict.muffin.otasukejuru.View.CircleGraphView
import org.jetbrains.anko.*

class TimerFragmentUI : AnkoComponent<TimerFragment> {
    private var isPushStartButton = false
    
    override fun createView(ui: AnkoContext<TimerFragment>): View = with(ui) {
        relativeLayout {
            lparams {
                height = matchParent
                width = matchParent
            }
            val circle = frameLayout {
                backgroundColor = Color.argb(0, 0, 0, 0)
                id = 1
            }.lparams {
                height = GlobalValue.displayWidth - 30
                width = GlobalValue.displayWidth - 30
                topMargin = 30
                leftMargin = 30
            }
            val circleMini = frameLayout {
            }.lparams {
                translationZ = 2F
                height = GlobalValue.displayWidth / 3
                width = GlobalValue.displayWidth / 3
                topMargin = GlobalValue.displayWidth / 3 + 15
                leftMargin = GlobalValue.displayWidth / 3 + 15
//                centerHorizontally()
                
                textView {
                    text = "1"
                    textSize = 40F
                }.lparams {
                    translationZ = 2F
                    width = wrapContent
                    height = wrapContent
                    topMargin = GlobalValue.displayWidth / 2 - 40
//                leftMargin = GlobalValue.displayWidth / 2 - 10
                    centerHorizontally()
//                    centerVertically()
                }
            }
            val editTime = editText {
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
                if (!isPushStartButton && editTime.text.toString() != "") {
                    val time = editTime.text.toString().toLong()
                    var totalTime = time
                    editTime.text.clear()
                    editTime.clearFocus()
    
                    val circleGraphView = CircleGraphView(context, Color.argb(255, 251, 251, 240), 60, true)
                    circleMini.addView(circleGraphView)
                    circleGraphView.startAnimation()
                    
                    while (0L < totalTime) {
                        val drawTime = if (totalTime % 60 == 0L) {
                            60L
                        } else {
                            totalTime % 60L
                        }
                        drawCircle(context, circle, drawTime)
                        totalTime -= drawTime
                        while (!GlobalValue.timerFlag) {
                        }
                        GlobalValue.timerFlag = false
                    }
                    isPushStartButton = true
                }
            }
        }
    }
}

private fun drawCircle(context: Context, circle: FrameLayout, time: Long) {
    val circleGraphView = CircleGraphView(context, Color.argb(255, 255, 0, 0), time, true)
    circle.addView(circleGraphView)
    circleGraphView.startAnimation()
    
    GlobalValue.timerFlag = false
    
    val circleGraphView1 = CircleGraphView(context, Color.argb(255, 251, 251, 240), time, false)
    circle.addView(circleGraphView1)
    circleGraphView1.startAnimation()
}
