package jp.ict.muffin.otasukejuru.ui

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.view.View
import android.widget.EditText
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.activity.TimeSetActivity
import jp.ict.muffin.otasukejuru.activity.TimerActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class TimeSetActivityUI(private val index: Int = -1) : AnkoComponent<TimeSetActivity> {
    private lateinit var editTime: EditText
    
    override fun createView(ui: AnkoContext<TimeSetActivity>): View = with(ui) {
        relativeLayout {
            backgroundColor = ContextCompat.getColor(context, R.color.back)
        
            lparams {
                height = matchParent
                width = matchParent
            }
    
            toolbar {
                id = R.id.ankoToolbar
                backgroundColor = ContextCompat.getColor(context, R.color.colorPrimary)
        
                imageButton {
                    id = R.id.ankoBack
                    backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_back_white_48dp)
                }.lparams {
                    width = wrapContent
                    height = wrapContent
                }
            }.lparams {
                width = matchParent
                height = wrapContent
            }
        
            textView("タイマー") {
                id = R.id.titleInterval
                textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                textSize = 30f
            }.lparams {
                below(R.id.ankoToolbar)
                topMargin = dip(10)
                leftMargin = dip(30)
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
                    width = dip(200)
                    centerHorizontally()
                }
                textView("分間") {
                    textSize = 20f
                }.lparams {
                    width = wrapContent
                    height = wrapContent
                    rightOf(editTime)
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
                        startActivity<TimerActivity>("time" to time, "index" to index)
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
    
}