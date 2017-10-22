package jp.ict.muffin.otasukejuru.ui

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.fragment.CalendarFragment2
import org.jetbrains.anko.*
import java.util.*

class CalendarFragmentUI : AnkoComponent<CalendarFragment2> {
    override fun createView(ui: AnkoContext<CalendarFragment2>): View = with(ui) {
        
        relativeLayout {
            linearLayout {
                id = R.id.taskLinear
                orientation = LinearLayout.HORIZONTAL
            }.lparams {
                width = matchParent
                height = dip(90)
                alignParentTop()
                leftMargin = dip(90)
            }
            scrollView {
                val calendar = Calendar.getInstance()
                var today = (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH)
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    (0 until 28).forEach {
                        relativeLayout {
                            textView(when (it % 4) {
                                1 -> "朝"
                                2 -> "昼"
                                3 -> "夜"
                                else -> "${today / 100}月${today % 100}日"
                            }) {
                                id = R.id.hourText
                            }.lparams {
                                width = wrapContent
                                height = wrapContent
                                alignParentStart()
                                centerVertically()
                                leftMargin = dip(when (it % 4) {
                                    0 -> 20
                                    else -> 70
                                })
                            }
                            
                            imageView {
                                backgroundColor = Color.GRAY
                            }.lparams {
                                width = matchParent
                                height = dip(if (it % 4 == 0) {
                                    2
                                } else {
                                    1
                                })
                                leftMargin = dip(90)
                                rightMargin = dip(20)
                                centerVertically()
                                alignParentEnd()
                                
                            }
                        }.lparams {
                            width = matchParent
                            height = dip(50)
                        }
                        today += when {
                            it % 4 == 0 -> 1
                            today == 30 -> 70
                            else -> 0
                        }
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                }
            }.lparams {
                width = matchParent
                height = matchParent
                backgroundColor = ContextCompat.getColor(context, R.color.back)
                below(R.id.taskLinear)
            }
        }
    }
    
}