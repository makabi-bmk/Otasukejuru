package jp.ict.muffin.otasukejuru

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import jp.ict.muffin.otasukejuru.View.CircleGraphView
import org.jetbrains.anko.*

class TimerFragmentUI : AnkoComponent<TimerFragment> {
    var isPushStartButton = false
    
    override fun createView(ui: AnkoContext<TimerFragment>): View = with(ui) {
        verticalLayout {
            lparams {
                gravity = Gravity.CENTER_HORIZONTAL
            }
            val circle = frameLayout {
                gravity = Gravity.CENTER_HORIZONTAL
                backgroundColor = Color.argb(0, 0, 0, 0)
            }.lparams(height = GlobalValues.getDisplayWidth(), width = GlobalValues.getDisplayWidth())
            val editTime = editText {
                gravity = Gravity.CENTER_HORIZONTAL
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            button("start") {
                height = wrapContent
                width = 50
                gravity = Gravity.CENTER_HORIZONTAL
            }.setOnClickListener {
                if (!isPushStartButton && !editTime.text.toString().equals("")) {
                    val time = editTime.text.toString().toLong()
                    var totalTime = time
                    editTime.text.clear()
                    editTime.clearFocus()
                    while (0L < totalTime) {
                        val drawTime = if (totalTime % 60 == 0L) {
                            60L
                        } else {
                            totalTime % 60L
                        }
                        drawCircle(context, circle, drawTime)
                        totalTime -= drawTime
                        while (!GlobalValues.isTimerFlag()) {
                        }
                        GlobalValues.setTimerFlag(false)
                    }
                    isPushStartButton = true
                }
            }
        }
    }
    
    private fun drawCircle(context: Context, circle: FrameLayout, time: Long) {
        val circleGraphView = CircleGraphView(context, Color.argb(255, 255, 0, 0), time, true)
        circle.addView(circleGraphView)
        circleGraphView.startAnimation()
        
        GlobalValues.setTimerFlag(false)
        
        val circleGraphView1 = CircleGraphView(context, Color.argb(255, 255, 255, 255), time, false)
        circle.addView(circleGraphView1)
        circleGraphView1.startAnimation()
    }
}
