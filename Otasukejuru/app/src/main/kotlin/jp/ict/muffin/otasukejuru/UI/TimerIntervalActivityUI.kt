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
                    alignParentEnd()
                    centerVertically()
                    marginEnd = 60
                }
                
            }.lparams {
                alignParentStart()
                alignParentEnd()
                below(R.id.titleIntervalFocusTime)
                height = 100
            }
            
            textView {
                id = R.id.titleIntervalBreakTime
                text = "休憩する時間"
            }.lparams {
                below(R.id.focusTimeRelative)
                topMargin = 10
                alignStart(R.id.focusTimeRelative)
            }
            relativeLayout {
                id = R.id.intervalTimeRelative
                numberPicker {
                    id = R.id.intervalHourNumPick
                }.lparams {
                    alignParentStart()
                    centerVertically()
                    marginStart = 60
                }
                
                numberPicker {
                    id = R.id.intervalMinuteNumPick
                }.lparams {
                    alignParentEnd()
                    centerVertically()
                    marginEnd = 60
                }
                
            }.lparams {
                alignParentStart()
                alignParentEnd()
                below(R.id.titleIntervalBreakTime)
                height = 100
            }
            
            frameLayout {
                id = R.id.circleFrame
            }.lparams {
                width = 150
                height = 150
                below(R.id.intervalTimeRelative)
                centerHorizontally()
            }
            
            button {
                id = R.id.nextButton
                text = "Next"
                
            }.lparams {
                marginEnd = 45
                below(R.id.circleFrame)
                alignParentEnd()
            }
        }
    }
}