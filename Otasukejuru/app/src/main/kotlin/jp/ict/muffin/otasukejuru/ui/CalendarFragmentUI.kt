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
            relativeLayout {
                textView("今日のタスク") {
                    id = R.id.todayTaskText
                    textSize = 25f
                }.lparams {
                    width = wrapContent
                    height = wrapContent
                    margin = dip(10)
                }
                
                scrollView {
                    textView("Hoge") {
                    }.lparams {
                        
                        margin = dip(10)
                    }
                }.lparams {
                    width = matchParent
                    height = 150
                    translationZ = 3f
                    below(R.id.todayTaskText)
                    backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.frame_shape)
                }
            }.lparams {
                margin = dip(30)
                below(R.id.ankoToolbar)
            }
            
            scrollView {
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    textView("") {
                    }.lparams {
                        height = 250
                    }
                    (0..24).forEach {
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
                                height = dip(1)
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
                below(R.id.ankoToolbar)
            }
        }
    }
    
}