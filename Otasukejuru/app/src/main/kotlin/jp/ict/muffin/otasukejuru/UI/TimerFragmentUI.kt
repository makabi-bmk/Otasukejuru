package jp.ict.muffin.otasukejuru

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import jp.ict.muffin.otasukejuru.View.CircleGraphView
import org.jetbrains.anko.*

/**
 * Created by mito on 2017/08/24.
 */
class TimerFragmentUI : AnkoComponent<TimerFragment>{
    lateinit var countDown: CountDown
    var isPushStartButton = false
    
    override fun createView(ui: AnkoContext<TimerFragment>): View = with(ui) {
        verticalLayout {
            lparams {
                gravity = Gravity.CENTER_HORIZONTAL
            }
            val circle = frameLayout {
                gravity = Gravity.CENTER_HORIZONTAL
                backgroundColor = Color.argb(0, 0, 0, 0)
            }.lparams(height = 700, width = 700)
            val editTime = editText {
                gravity = Gravity.CENTER_HORIZONTAL
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            button("start") {
                height = wrapContent
                width = 50
                gravity = Gravity.CENTER_HORIZONTAL
            }.setOnClickListener {
                if (!isPushStartButton) {
                    val time = editTime.text.toString().toLong()
                    if (time <= 60L) {
                        drawCircle(context, circle, time)
                    } else {
                        context.toast("over")
                    }
                    editTime.text.clear()
                    editTime.clearFocus()
                    isPushStartButton = true
                }
            }
        }
    }
    
    private fun drawCircle(context: Context, circle: FrameLayout, time: Long) {
        val circleGraphView = CircleGraphView(context, Color.argb(255, 255, 0, 0), time, true)
        circle.addView(circleGraphView)
        circleGraphView.startAnimation()
    
        val circleGraphView1 = CircleGraphView(context, Color.argb(255, 255, 255, 255), time, false)
        circle.addView(circleGraphView1)
        circleGraphView1.startAnimation()
    }
}
