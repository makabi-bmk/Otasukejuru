package jp.ict.muffin.otasukejuru.ui

import android.graphics.Color
import android.view.View
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.activity.TimerActivity
import jp.ict.muffin.otasukejuru.activity.TimerNotificationActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class TimerNotificationActivityUI(private val time: Long) :
        AnkoComponent<TimerNotificationActivity> {
    override fun createView(ui: AnkoContext<TimerNotificationActivity>): View = with(ui) {
        
        relativeLayout {
            backgroundColor = Color.argb(255, 251, 251, 240)
            
            textView("終了前の通知") {
                id = R.id.titleInterval
                textColor = Color.argb(255, 102, 183, 236)
                textSize = 30f
            }.lparams {
                topMargin = dip(10)
                leftMargin = dip(30)
                alignParentStart()
                alignParentTop()
            }
            
            textView("終了何分前に\n通知しますか？") {
                id = R.id.titleNotification
                textSize = 30f
            }.lparams {
                below(R.id.titleInterval)
                centerHorizontally()
                topMargin = dip(30)
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
                textColor = Color.argb(255, 102, 183, 236)
                textSize = 20f
                onClick {
                    startActivity<TimerActivity>("time" to time)
                }
            }.lparams {
                marginEnd = dip(30)
                bottomMargin = dip(20)
                alignParentBottom()
                alignParentRight()
            }
        }
        
    }
}