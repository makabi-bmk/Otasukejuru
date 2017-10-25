package jp.ict.muffin.otasukejuru.ui

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
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
                remainingHourText = textView("") {
                    textSize = 40F
                }.lparams {
                    translationZ = 2F
                    width = wrapContent
                    height = wrapContent
                    topMargin = GlobalValue.displayWidth / 2 - 40
                    centerHorizontally()
                }
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
                    textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                    backgroundColor = Color.argb(0, 0, 0, 0)
                    onClick {
                        startButtonClickListener(getContext())
                    }
                }.lparams {
                    below(editTime)
                    width = wrapContent
                    height = wrapContent
                    centerHorizontally()
                }
                
                
            }.lparams {
                below(R.id.circleFrame)
                width = matchParent
                height = wrapContent
            }
        }
    }
    
    private fun startButtonClickListener(context: Context) {
        if (editTime.text.toString() != "") {
            val time = editTime.text.toString().toLong()
            editTime.apply {
                text.clear()
                editTime.clearFocus()
            }
            
            val params: ArrayList<HashMap<String, Int>> = java.util.ArrayList()
            
            val map = HashMap<String, Int>()
            map.put("color", ContextCompat.getColor(context, R.color.back))
            map.put("value", 60)
            params.add(map)
            
            val circleGraphView = CircleGraphView(context,
                    params, true)
            circleMini.addView(circleGraphView)
            circleGraphView.startAnimation()
            
            drawCircle(context, time)
            Handler(Looper.getMainLooper()).postDelayed({
                context.startActivity<TimerActivity>("time" to time)
            }, 2 * 1000)
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
