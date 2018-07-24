package jp.ict.muffin.otasukejuru.ui

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.fragment.ScheduleFragment
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.alignParentEnd
import org.jetbrains.anko.alignParentStart
import org.jetbrains.anko.alignParentTop
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.centerVertically
import org.jetbrains.anko.dip
import org.jetbrains.anko.imageView
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.scrollView
import org.jetbrains.anko.textView
import org.jetbrains.anko.wrapContent
import java.util.Calendar

class ScheduleFragmentUI : AnkoComponent<ScheduleFragment> {

    override fun createView(ui: AnkoContext<ScheduleFragment>): View = with(ui) {
        relativeLayout {
            linearLayout {
                id = R.id.taskLinear
                orientation = LinearLayout.HORIZONTAL
            }.lparams {
                width = matchParent
                height = wrapContent
                alignParentTop()
                leftMargin = dip(80)
            }
            scrollView {
                val calendar = Calendar.getInstance()
                var today = (calendar.get(Calendar.MONTH) + 1) * 100 +
                        calendar.get(Calendar.DAY_OF_MONTH)
                relativeLayout {
                    id = R.id.taskRelative
                    (0 until 28).forEach {
                        relativeLayout {
                            val showText: String = when (it % 4) {
                                1 -> "朝"
                                2 -> "昼"
                                3 -> "夕"
                                else -> "${today / 100}月${today % 100}日"
                            }
                            textView(showText) {
                                id = R.id.hourText
                            }.lparams {
                                width = wrapContent
                                height = wrapContent
                                alignParentStart()
                                centerVertically()
                                leftMargin = dip(when (it % 4) {
                                    0 -> 10
                                    else -> 60
                                })
                            }

                            imageView {
                                backgroundColor = Color.GRAY
                            }.lparams {
                                width = matchParent
                                height = dip(if (it % 4 == 0) {
                                    3
                                } else {
                                    2
                                })
                                leftMargin = dip(80)
                                rightMargin = dip(20)
                                centerVertically()
                                alignParentEnd()
                            }
                        }.lparams {
                            width = matchParent
                            height = dip(50)
                            topMargin = dip(it * 50)
                        }
                        today += when {
                            it % 4 == 0 -> 1
                            today == 1032 -> 69
                            else -> 0
                        }
                    }
                    relativeLayout {
                        id = R.id.refreshRelative
                    }.lparams {
                        width = matchParent
                        height = matchParent
                    }
                }.lparams {
                    width = matchParent
                    height = matchParent
                }
            }.lparams {
                width = matchParent
                height = matchParent
                backgroundColor = ContextCompat.getColor(
                        context,
                        R.color.back
                )
                topMargin = dip(90)
            }
        }
    }
}
