package jp.ict.muffin.otasukejuru

import android.graphics.Color
import android.view.View
import jp.ict.muffin.otasukejuru.Activity.TimerNotificationActivity
import org.jetbrains.anko.*


class TimerNotificationActivityUI : AnkoComponent<TimerNotificationActivity> {
    override fun createView(ui: AnkoContext<TimerNotificationActivity>): View = with(ui) {
        
        relativeLayout {
            backgroundColor = Color.argb(255, 251, 251, 240)
            
            textView("終了前の通知") {
                id = R.id.titleInterval
                textColor = Color.argb(255, 102, 183, 236)
                textSize = 30f
            }.lparams {
                topMargin = dip(10)
                alignParentStart()
                alignParentTop()
            }
            
            textView("終了何分前に\n通知しますか？") {
                id = R.id.titleNotification
                textSize = 30f
            }.lparams {
                centerHorizontally()
                topMargin = dip(50)
            }
            
            relativeLayout {
                
                numberPicker {
                    id = R.id.notificationNumPick
                    maxValue = 30
                    minValue = 5
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
            }.lparams {
                marginEnd = dip(30)
                bottomMargin = dip(20)
                alignParentBottom()
                alignParentRight()
            }.setOnClickListener {
                startActivity<TimerActivity>()
            }
        }
        
    }
}