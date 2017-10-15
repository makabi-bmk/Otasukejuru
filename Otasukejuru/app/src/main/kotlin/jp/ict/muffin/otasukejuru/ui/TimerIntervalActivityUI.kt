package jp.ict.muffin.otasukejuru.ui

import android.graphics.Color
import android.view.View
import android.widget.NumberPicker
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.activity.TimerIntervalActivity
import jp.ict.muffin.otasukejuru.activity.TimerNotificationActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class TimerIntervalActivityUI(private val time: Long) : AnkoComponent<TimerIntervalActivity> {
    private lateinit var focusHourNumPick: NumberPicker
    private lateinit var focusMinuteNumPick: NumberPicker
    private lateinit var intervalHourNumPick: NumberPicker
    private lateinit var intervalMinuteNumPick: NumberPicker
    
    
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
                
                focusHourNumPick = numberPicker {
                    id = R.id.focusHourNumPick
                    minValue = 0
                    maxValue = (time / 60).toInt()
                }.lparams {
                    alignParentStart()
                    centerVertically()
                    marginStart = GlobalValue.displayWidth / 5
                }
                
                textView("時") {
                }.lparams {
                    rightOf(R.id.focusHourNumPick)
                    centerVertically()
                }
                
                focusMinuteNumPick = numberPicker {
                    id = R.id.focusMinuteNumPick
                    minValue = 1
                    maxValue = if (0 < time / 60) {
                        59
                    } else {
                        time.toInt() % 60
                    }
                    setFormatter { value -> String.format("%02d", value) }
                    setOnValueChangedListener { _, _, _ ->
                        val remindingTime: Int = (time - (intervalHourNumPick.value * 60 + intervalMinuteNumPick.value)).toInt()

//                        intervalHourNumPick.maxValue = (remindingTime - 1) / 60
                        intervalMinuteNumPick.maxValue = when {
                            60 <= remindingTime -> 59
                            0 <= remindingTime -> remindingTime
                            else -> 0
                        }
                    }
                }.lparams {
                    leftOf(R.id.minuteTextView)
                }
                
                textView("分") {
                    id = R.id.minuteTextView
                }.lparams {
                    centerVertically()
                    marginEnd = GlobalValue.displayWidth / 5
                    alignParentEnd()
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
                
                intervalHourNumPick = numberPicker {
                    id = R.id.intervalHourNumPick
                    minValue = 0
                    maxValue = (time / 60).toInt()
                }.lparams {
                    alignParentStart()
                    centerVertically()
                    marginStart = GlobalValue.displayWidth / 5
                }
                
                textView("時") {
                }.lparams {
                    rightOf(R.id.intervalHourNumPick)
                    centerVertically()
                }
                
                intervalMinuteNumPick = numberPicker {
                    id = R.id.intervalMinuteNumPick
                    minValue = 0
                    maxValue = if (0 < time / 60) {
                        59
                    } else {
                        time.toInt() % 60
                    }
                    setFormatter { value -> String.format("%02d", value) }
                    setOnValueChangedListener { _, _, _ ->
                        val remindingTime: Int = (time - (focusHourNumPick.value * 60 + focusMinuteNumPick.value)).toInt()

//                        focusHourNumPick.maxValue = (remindingTime - 1) / 60
                        focusMinuteNumPick.maxValue = when {
                            60 <= remindingTime -> 59
                            0 <= remindingTime -> remindingTime
                            else -> 0
                        }
                    }
                }.lparams {
                    leftOf(R.id.minuteTextView)
                }
                textView("分") {
                    id = R.id.minuteTextView
                }.lparams {
                    centerVertically()
                    marginEnd = GlobalValue.displayWidth / 5
                    alignParentEnd()
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
                onClick {
                    startActivity<TimerNotificationActivity>("time" to time)
                }
            }.lparams {
                marginEnd = dip(30)
                bottomMargin = dip(20)
                below(R.id.circleFrame)
                alignParentEnd()
            }
        }
    }
}
