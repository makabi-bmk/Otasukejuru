package jp.ict.muffin.otasukejuru.ui

import android.graphics.Color
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.activity.TimerActivity
import jp.ict.muffin.otasukejuru.fragment.TimerSetTimeFragment
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textView
import org.jetbrains.anko.editText
import org.jetbrains.anko.rightOf
import org.jetbrains.anko.button
import org.jetbrains.anko.textColor
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.centerHorizontally
import org.jetbrains.anko.centerVertically
import org.jetbrains.anko.wrapContent
import org.jetbrains.anko.below
import org.jetbrains.anko.dip
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.alignParentStart
import org.jetbrains.anko.alignParentTop

import org.jetbrains.anko.centerHorizontally

class TimerSetTimeFragmentUI : AnkoComponent<TimerSetTimeFragment> {
    private lateinit var editTime: EditText

    override fun createView(ui: AnkoContext<TimerSetTimeFragment>): View = with(ui) {
        relativeLayout {
            backgroundColor = ContextCompat.getColor(
                    context,
                    R.color.back
            )

            lparams {
                height = matchParent
                width = matchParent
            }

            textView("タイマー") {
                id = R.id.titleInterval
                textColor = ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                )
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
                    backgroundColor = Color.argb(
                            0,
                            0,
                            0,
                            0
                    )
                    isEnabled = false
                    textColor = Color.argb(
                            0,
                            0,
                            0,
                            0
                    )
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
}
