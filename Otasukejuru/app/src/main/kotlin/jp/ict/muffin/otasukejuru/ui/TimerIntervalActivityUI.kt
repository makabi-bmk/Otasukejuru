package jp.ict.muffin.otasukejuru.ui

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.NumberPicker
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.activity.TimerActivity
import org.jetbrains.anko.*


class TimerIntervalActivityUI(private val time: Long) : AnkoComponent<TimerActivity> {
    private lateinit var focusHourNumPick: NumberPicker
    private lateinit var focusMinuteNumPick: NumberPicker
    private lateinit var intervalHourNumPick: NumberPicker
    private lateinit var intervalMinuteNumPick: NumberPicker
    
    
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
                textView("集中時間と休憩時間") {
                    id = R.id.titleInterval
                    textColor = ContextCompat.getColor(context, R.color.colorPrimary)
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
                }
                
                relativeLayout {
                    id = R.id.focusTimeRelative
                    
                    focusHourNumPick = numberPicker {
                        id = R.id.focusHourNumPick
                        minValue = 0
                        maxValue = (time / 60).toInt()
                        setOnValueChangedListener { _, _, newValue ->
                            val remindingTime: Int = time.toInt() -
                                    (newValue * 60 + focusMinuteNumPick.value)
                            intervalHourNumPick.maxValue = (remindingTime - 1) / 60
                            intervalMinuteNumPick.maxValue = if (60 <= remindingTime) {
                                59
                            } else {
                                remindingTime
                            }
                            
                            val minuteTime = time.toInt() -
                                    (newValue * 60 + intervalHourNumPick.value * 60 +
                                            intervalMinuteNumPick.value)
                            focusMinuteNumPick.maxValue = if (60 <= minuteTime) {
                                59
                            } else {
                                minuteTime
                            }
                            
                        }
                    }.lparams {
                        centerVertically()
                    }
                    
                    textView("時") {
                        id = R.id.hourTextView
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
                        setOnValueChangedListener { _, _, newValue ->
                            val remindingTime: Int = time.toInt() -
                                    (focusHourNumPick.value * 60 + newValue)
                            
                            intervalMinuteNumPick.maxValue = when {
                                60 <= remindingTime -> 59
                                0 <= remindingTime -> remindingTime
                                else -> 0
                            }
                            
                        }
                    }.lparams {
                        rightOf(R.id.hourTextView)
                    }
                    
                    textView("分") {
                        id = R.id.minuteTextView
                    }.lparams {
                        rightOf(R.id.focusMinuteNumPick)
                        centerVertically()
                    }
                    
                }.lparams {
                    centerHorizontally()
                    below(R.id.titleIntervalFocusTime)
                    height = wrapContent
                    width = wrapContent
                }
                
                textView("休憩する時間") {
                    id = R.id.titleIntervalBreakTime
                }.lparams {
                    below(R.id.focusTimeRelative)
                    leftMargin = dip(50)
                }
                
                relativeLayout {
                    id = R.id.intervalTimeRelative
                    
                    intervalHourNumPick = numberPicker {
                        id = R.id.intervalHourNumPick
                        minValue = 0
                        maxValue = (time / 60).toInt()
                        setOnValueChangedListener { _, _, newValue ->
                            val remindingTime: Int = time.toInt() -
                                    (newValue * 60 + focusMinuteNumPick.value)
                            focusHourNumPick.maxValue = (remindingTime - 1) / 60
                            focusMinuteNumPick.maxValue = if (60 <= remindingTime) {
                                59
                            } else {
                                remindingTime
                            }
                            
                            val minuteTime = time.toInt() -
                                    (newValue * 60 + focusHourNumPick.value * 60 +
                                            focusMinuteNumPick.value)
                            intervalMinuteNumPick.maxValue = if (60 <= minuteTime) {
                                59
                            } else {
                                minuteTime
                            }
                        }
                    }.lparams {
                        centerVertically()
                    }
                    
                    textView("時") {
                        id = R.id.hourTextView
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
                        setOnValueChangedListener { _, _, newValue ->
                            val remindingTime: Int = time.toInt() -
                                    (intervalHourNumPick.value * 60 + newValue)
                            
                            focusMinuteNumPick.maxValue = when {
                                60 <= remindingTime -> 59
                                0 <= remindingTime -> remindingTime
                                else -> 0
                            }
                        }
                    }.lparams {
                        rightOf(R.id.hourTextView)
                    }
                    textView("分") {
                        id = R.id.minuteTextView
                    }.lparams {
                        rightOf(R.id.intervalMinuteNumPick)
                        centerVertically()
                    }
                    
                }.lparams {
                    centerHorizontally()
                    centerVertically()
                    below(R.id.titleIntervalBreakTime)
                    width = wrapContent
                    height = wrapContent
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
                    textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                    textSize = 20f
//                    onClick {
//                        startActivity<TimerNotificationActivity>("time" to time)
//                    }
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
