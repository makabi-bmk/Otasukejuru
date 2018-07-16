package jp.ict.muffin.otasukejuru.ui

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import jp.ict.muffin.otasukejuru.activity.TimerActivity
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext

class TimerNotificationActivityUI(private val time: Long) :
        AnkoComponent<TimerActivity> {

    override fun createView(ui: AnkoContext<TimerActivity>): View = with(ui) {
        relativeLayout {
            backgroundColor = ContextCompat.getColor(context, R.color.back)

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

            relativeLayout {
                textView("終了前の通知") {
                    id = R.id.titleInterval
                    textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                    textSize = 30f
                }.lparams {
                    topMargin = dip(10)
                    leftMargin = dip(30)
                    alignParentStart()
                    alignParentTop()
                }

                textView("終了何分前に通知しますか？") {
                    id = R.id.titleNotification
                    textSize = 25f
                }.lparams {
                    below(R.id.titleInterval)
                    topMargin = dip(10)
                    leftMargin = dip(50)
                    rightMargin = dip(50)
                }

                relativeLayout {
                    numberPicker {
                        id = R.id.notificationNumPick
                        minValue = 1
                        maxValue = 6
                        setFormatter { value -> String.format("%d", value * 5) }
                    }.lparams {
                        centerVertically()
                        alignParentStart()
                    }

                    textView("分前") {
                        id = R.id.notificationText
                        textSize = 30f
                    }.lparams {
                        centerVertically()
                        rightOf(R.id.notificationNumPick)
                    }
                }.lparams {
                    centerHorizontally()
                    centerVertically()
                }

                button("次へ") {
                    id = R.id.nextButton
                    backgroundColor = Color.argb(0, 0, 0, 0)
                    textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                    textSize = 20f
                    onClick {
                        startActivity<TimerActivity>("time" to time)
                    }
                }.lparams {
                    margin = 30
                    alignParentBottom()
                    alignParentRight()
                }
            }.lparams {
                below(R.id.ankoToolbar)
            }
        }
    }
}