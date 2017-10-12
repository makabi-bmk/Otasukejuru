package jp.ict.muffin.otasukejuru

import android.graphics.Color
import android.view.View
import org.jetbrains.anko.*


class TimerIntervalActivityUI(private val time: Long) : AnkoComponent<TimerIntervalActivity> {
    override fun createView(ui: AnkoContext<TimerIntervalActivity>): View = with(ui) {
        
        relativeLayout {
            backgroundColor = Color.argb(255, 251, 251, 240)
            
            textView("集中時間と休憩時間") {
                id = R.id.titleInterval
                textColor = Color.argb(255, 102, 183, 236)
                textSize = 30f
            }.lparams {
                topMargin = dip(10)
                leftMargin = dip(30)
                alignParentStart()
                alignParentTop()
            }
            
            textView("集中する時間") {
                id = R.id.titleIntervalFocusTime
            }.lparams {
                below(R.id.titleInterval)
                topMargin = dip(10)
                leftMargin = dip(50)
                alignStart(R.id.focusTimeRelative)
            }
            
            relativeLayout {
                id = R.id.focusTimeRelative
                
                numberPicker {
                    id = R.id.focusHourNumPick
                    minValue = 0
                    maxValue = 24
                    setFormatter { value -> String.format("%02d", value) }
                }.lparams {
                    alignParentStart()
                    centerVertically()
                    marginStart = dip(95)
                }
                
                numberPicker {
                    id = R.id.focusMinuteNumPick
                    minValue = 0
                    maxValue = 59
                    setFormatter { value -> String.format("%02d", value) }
                }.lparams {
                    alignParentEnd()
                    centerVertically()
                    marginEnd = dip(95)
                }
                
            }.lparams {
                alignParentStart()
                alignParentEnd()
                below(R.id.titleIntervalFocusTime)
                height = dip(100)
                width = matchParent
            }
            
            textView("休憩する時間") {
                id = R.id.titleIntervalBreakTime
            }.lparams {
                below(R.id.focusTimeRelative)
                leftMargin = dip(50)
                alignStart(R.id.focusTimeRelative)
            }
            
            relativeLayout {
                id = R.id.intervalTimeRelative
                
                numberPicker {
                    id = R.id.intervalHourNumPick
                    minValue = 0
                    maxValue = 24
                    setFormatter { value -> String.format("%02d", value) }
                }.lparams {
                    alignParentStart()
                    centerVertically()
                    marginStart = dip(95)
                }
                
                numberPicker {
                    id = R.id.intervalMinuteNumPick
                    minValue = 0
                    maxValue = 59
                    setFormatter { value -> String.format("%02d", value) }
                }.lparams {
                    alignParentEnd()
                    centerVertically()
                    marginEnd = dip(95)
                }
                
            }.lparams {
                alignParentStart()
                alignParentEnd()
                below(R.id.titleIntervalBreakTime)
                height = dip(100)
            }
            
            frameLayout {
                id = R.id.circleFrame
            }.lparams {
                width = dip(150)
                height = dip(150)
                below(R.id.intervalTimeRelative)
                centerHorizontally()
            }
            
            button("次へ") {
                id = R.id.nextButton
                backgroundColor = Color.argb(0, 0, 0, 0)
                textColor = Color.argb(255, 102, 183, 236)
                textSize = 20f
            }.lparams {
                marginEnd = dip(30)
                bottomMargin = dip(20)
                below(R.id.circleFrame)
                alignParentEnd()
            }.setOnClickListener {
                startActivity<TimerNotificationActivity>("time" to time)
            }
        }
    }
}
