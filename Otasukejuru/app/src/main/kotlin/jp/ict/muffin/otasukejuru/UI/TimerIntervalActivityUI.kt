package jp.ict.muffin.otasukejuru

import android.graphics.Color
import android.view.View
import jp.ict.muffin.otasukejuru.Activity.TimerIntervalActivity
import org.jetbrains.anko.*


class TimerIntervalActivityUI : AnkoComponent<TimerIntervalActivity> {
    override fun createView(ui: AnkoContext<TimerIntervalActivity>): View = with(ui) {
        
        relativeLayout {
            textView {
                id = R.id.titleInterval
                text = "集中時間と休憩時間"
                textColor = Color.argb(255, 102, 183, 236)
                textSize = 30f
            }.lparams {
                topMargin = 10
                alignParentStart()
                alignParentTop()
            }
            
            textView {
                id = R.id.titleIntervalFocusTime
                text = "集中する時間"
            }.lparams {
                below(R.id.titleInterval)
                topMargin = 10
                alignStart(R.id.focusTimeRelative)
            }
            
            relativeLayout {
                id = R.id.focusTimeRelative
                numberPicker {
                    id = R.id.focusHourNumPick
                }.lparams {
                    alignParentStart()
                    centerVertically()
                    marginStart = 60
                }
    
                numberPicker {
                    id = R.id.focusMinuteNumPick
                }.lparams {
                    alignParentStart()
                    centerVertically()
                    marginStart = 120
                }
                
            }.lparams {
                alignEnd(R.id.nextButton)
                below(R.id.titleIntervalFocusTime)
            }
        }
        
    }
}