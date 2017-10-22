package jp.ict.muffin.otasukejuru.ui

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.fragment.CalendarFragment2
import org.jetbrains.anko.*

class CalendarFragmentUI : AnkoComponent<CalendarFragment2> {
    override fun createView(ui: AnkoContext<CalendarFragment2>): View = with(ui) {
        
        relativeLayout {
            linearLayout {
                id = R.id.taskLinear
            }.lparams {
                width = matchParent
                height = dip(90)
                alignParentTop()
            }
            scrollView {
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    (0..28).forEach {
                        relativeLayout {
                            textView(it.toString()) {
                                id = R.id.hourText
                            }.lparams {
                                width = wrapContent
                                height = wrapContent
                                alignParentStart()
                                centerVertically()
                                leftMargin = dip(10)
                            }
                            
                            imageView {
                                backgroundColor = Color.GRAY
                            }.lparams {
                                width = matchParent
                                height = dip(if (it % 4 == 0) {
                                    3
                                } else {
                                    1
                                })
                                leftMargin = dip(30)
                                rightMargin = dip(20)
                                centerVertically()
                                alignParentEnd()
                                
                            }
                        }.lparams {
                            width = matchParent
                            height = dip(50)
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