package jp.ict.muffin.otasukejuru.ui

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.activity.TimerActivity
import jp.ict.muffin.otasukejuru.fragment.TimerSetTimeFragment
import jp.ict.muffin.otasukejuru.view.CircleGraphView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class TimerSetTimeFragmentUI : AnkoComponent<TimerSetTimeFragment> {
    private lateinit var circleMini: FrameLayout
    private lateinit var circle: FrameLayout
    private lateinit var editTime: EditText
    private lateinit var remainingHourText: TextView
    
    override fun createView(ui: AnkoContext<TimerSetTimeFragment>): View = with(ui) {
        relativeLayout {
            backgroundColor = ContextCompat.getColor(context, R.color.back)
            
            lparams {
                height = matchParent
                width = matchParent
            }
    
            textView("タイマー") {
                id = R.id.titleInterval
                textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                textSize = 30f
            }.lparams {
                topMargin = dip(10)
                leftMargin = dip(30)
                alignParentStart()
                alignParentTop()
            }
    
            textView("タイマーを何分間セットしますか？") {
                id = R.id.titleNotification
                textSize = 25f
            }.lparams {
                below(R.id.titleInterval)
                topMargin = dip(10)
                leftMargin = dip(50)
                rightMargin = dip(50)
            }
            
            relativeLayout {
                
                editTime = editText {
                    inputType = InputType.TYPE_CLASS_NUMBER
                    id = R.id.setTimeEdit
                }.lparams {
                    height = wrapContent
                    width = matchParent
                }
                
                button("次へ") {
                    id = R.id.nextButton
//                    textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                    backgroundColor = Color.argb(0, 0, 0, 0)
                    isEnabled = false
                    textColor = Color.argb(0, 0, 0, 0)
                    textSize = 20f
                    onClick {
                        val time = editTime.text.toString().toLong()
                        editTime.apply {
                            text.clear()
                            editTime.clearFocus()
                        }
                        startActivity<TimerActivity>("time" to time)
                    }
                }.lparams {
                    below(editTime)
                    width = wrapContent
                    height = wrapContent
                    centerHorizontally()
                }
                
                
            }.lparams {
                width = matchParent
                height = wrapContent
                centerHorizontally()
                centerVertically()
            }
        }
    }
    
    private fun drawCircle(context: Context, time: Long) {
        val drawTime: Long = if (time % 60 == 0L) {
            60L
        } else {
            time % 60L
        }
        
        val backColor = ContextCompat.getColor(context, R.color.back)
        val redColor = ContextCompat.getColor(context, R.color.mostPriority)
        
        val params: ArrayList<HashMap<String, Int>> = java.util.ArrayList()
        val colors = arrayListOf(backColor, redColor)
        val drawCircleTime = arrayListOf(60 - drawTime, drawTime)
        (0 until 2).forEach {
            val mapSI = HashMap<String, Int>()
            mapSI.put("color", colors[it])
            mapSI.put("value", drawCircleTime[it].toInt())
            params.add(mapSI)
        }
        
        val circleGraphView = CircleGraphView(context, params, true)
        circle.addView(circleGraphView)
        circleGraphView.startAnimation()
        remainingHourText.text = ((time - 1) / 60).toString()
    }
}
