package jp.ict.muffin.otasukejuru.ui

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.view.View
import android.widget.EditText
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.activity.TimerActivity
import jp.ict.muffin.otasukejuru.fragment.TimerSetTimeFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class TimerSetTimeFragmentUI : AnkoComponent<TimerSetTimeFragment> {
    private lateinit var editTime: EditText
    
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
                    backgroundColor = Color.argb(0, 0, 0, 0)
                    isEnabled = false
                    textColor = Color.argb(0, 0, 0, 0)
                    textSize = 20f
                    onClick {
                        val time = editTime.text.toString().toLong()
                        editTime.also {
                            it.text.clear()
                            it.clearFocus()
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
}
