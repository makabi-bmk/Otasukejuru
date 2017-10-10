package jp.ict.muffin.otasukejuru

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import org.jetbrains.anko.*

class TimerSetTimeFragmentUI : AnkoComponent<TimerSetTimeFragment> {
    private var isPushStartButton = false
    private lateinit var circleMini: FrameLayout
    private lateinit var circle: FrameLayout
    private lateinit var editTime: EditText
    private lateinit var remainingHourText: TextView
    private var time = 0L
    
    override fun createView(ui: AnkoContext<TimerSetTimeFragment>): View = with(ui) {
        relativeLayout {
            lparams {
                height = matchParent
                width = matchParent
            }
            circle = frameLayout {
                backgroundColor = Color.argb(0, 0, 0, 0)
                id = R.id.circleFrame
            }.lparams {
                height = dip(GlobalValue.displayWidth / 2 - 30)
                width = dip(GlobalValue.displayWidth / 2 - 30)
                topMargin = dip(30)
                alignParentTop()
                centerHorizontally()
            }
            circleMini = frameLayout {
            }.lparams {
                translationZ = 2F
                height = dip(GlobalValue.displayWidth / 5)
                width = dip(GlobalValue.displayWidth / 5)
                topMargin = dip(GlobalValue.displayWidth / 5 - 20)
                centerHorizontally()
                alignParentTop()
                remainingHourText = textView {
                    text = ""
                    textSize = 40F
                }.lparams {
                    translationZ = 2F
                    width = wrapContent
                    height = wrapContent
                    topMargin = dip(GlobalValue.displayWidth / 4 - 20)
                    centerHorizontally()
                    alignParentTop()
                }
            }
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                button("start") {
                }.lparams {
                    width = wrapContent
                    height = wrapContent
                }.setOnClickListener {
                    startButtonClickListener(context)
                }
                editTime = editText {
                    inputType = InputType.TYPE_CLASS_NUMBER
                }.lparams {
                    height = wrapContent
                    width = dip(300)
                }
            }.lparams {
                below(R.id.circleFrame)
            }
        }
    }
    
    private fun startButtonClickListener(context: Context) {
        if (!isPushStartButton && editTime.text.toString() != "") {
            time = editTime.text.toString().toLong()
            editTime.apply {
                text.clear()
                editTime.clearFocus()
            }
            
            val circleGraphView = CircleGraphView(context,
                    Color.argb(255, 251, 251, 240), 60, true)
            circleMini.addView(circleGraphView)
            circleGraphView.startAnimation()
            
            isPushStartButton = true
            drawCircle(context, time)
        } else {
            context.startActivity<TimerActivity>("time" to time)
        }
    }
    
    private fun drawCircle(context: Context, time: Long) {
        val colors = arrayListOf(Color.argb(255, 255, 0, 0), Color.argb(255, 251, 251, 240))
        val init = arrayListOf(true, false)
        (0 until 2).forEach {
            val circleGraphView = CircleGraphView(context, colors[it], time, init[it])
            circle.addView(circleGraphView)
            circleGraphView.startAnimation()
        }
    }
}
