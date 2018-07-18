package jp.ict.muffin.otasukejuru.ui

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import android.widget.NumberPicker
import androidx.core.content.ContextCompat
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.activity.TimerActivity
import jp.ict.muffin.otasukejuru.view.CircleGraphView
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.alignParentBottom
import org.jetbrains.anko.alignParentEnd
import org.jetbrains.anko.alignParentStart
import org.jetbrains.anko.alignParentTop
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.below
import org.jetbrains.anko.button
import org.jetbrains.anko.centerHorizontally
import org.jetbrains.anko.centerVertically
import org.jetbrains.anko.dip
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.imageButton
import org.jetbrains.anko.margin
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.numberPicker
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.rightOf
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView
import org.jetbrains.anko.toolbar
import org.jetbrains.anko.wrapContent

class TimerIntervalActivityUI(private val time: Long) : AnkoComponent<TimerActivity> {
    private lateinit var focusHourNumPick: NumberPicker
    private lateinit var focusMinuteNumPick: NumberPicker
    private lateinit var intervalHourNumPick: NumberPicker
    private lateinit var intervalMinuteNumPick: NumberPicker
    private lateinit var timerFrame: FrameLayout
    private var focusTime: Int = 1
    private var intervalTime: Int = 0

    override fun createView(ui: AnkoContext<TimerActivity>): View = with(ui) {
        relativeLayout {
            backgroundColor = ContextCompat.getColor(
                    context,
                    R.color.back
            )

            toolbar {
                id = R.id.ankoToolbar
                backgroundColor = ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                )

                imageButton {
                    id = R.id.ankoBack
                    backgroundDrawable = ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_arrow_back_white_48dp
                    )
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
                    textColor = ContextCompat.getColor(
                            context,
                            R.color.colorPrimary
                    )
                    textSize = 30f
                    requestFocus()
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
                        setOnValueChangedListener { _, oldValue, newValue ->
                            var remindingTime: Int = time.toInt() -
                                    (newValue * 60 + focusMinuteNumPick.value)
                            if (remindingTime < 0) {
                                remindingTime = 0
                            }
                            intervalHourNumPick.maxValue = (remindingTime - 1) / 60
                            intervalMinuteNumPick.maxValue = if (60 <= remindingTime) {
                                59
                            } else {
                                remindingTime
                            }

                            val minuteTime = time.toInt() -
                                    (newValue * 60 +
                                            intervalHourNumPick.value * 60 +
                                            intervalMinuteNumPick.value)
                            focusMinuteNumPick.maxValue = if (60 <= minuteTime) {
                                59
                            } else {
                                minuteTime
                            }

                            focusTime += 60 * (newValue - oldValue)
                            drawCircle(context)
                        }
                    }.lparams {
                        width = wrapContent
                        height = dip(100)
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

                        setFormatter { value ->
                            String.format(
                                    "%02d",
                                    value
                            )
                        }
                        setOnValueChangedListener { _, oldValue, newValue ->
                            var remindingTime: Int = time.toInt() -
                                    (focusHourNumPick.value * 60 + newValue)
                            if (remindingTime < 0) {
                                remindingTime = 0
                            }

                            intervalMinuteNumPick.maxValue = when {
                                60 <= remindingTime -> 59
                                0 <= remindingTime -> remindingTime
                                else -> 0
                            }

                            focusTime += newValue - oldValue
                            drawCircle(context)
                        }
                    }.lparams {
                        width = wrapContent
                        height = dip(100)
                        rightOf(R.id.hourTextView)
                    }

                    textView("分間") {
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
                        setOnValueChangedListener { _, oldValue, newValue ->
                            var remindingTime: Int = time.toInt() -
                                    (newValue * 60 + focusMinuteNumPick.value)
                            if (remindingTime < 0) {
                                remindingTime = 0
                            }

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

                            intervalTime += 60 * (newValue - oldValue)
                            drawCircle(context)
                        }
                    }.lparams {
                        width = wrapContent
                        height = dip(100)
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

                        setFormatter { value ->
                            String.format(
                                    "%02d",
                                    value
                            )
                        }
                        setOnValueChangedListener { _, oldValue, newValue ->
                            var remindingTime: Int = time.toInt() -
                                    (intervalHourNumPick.value * 60 + newValue)
                            if (remindingTime < 0) {
                                remindingTime = 0
                            }
                            focusMinuteNumPick.maxValue = when {
                                60 <= remindingTime -> 59
                                0 <= remindingTime -> remindingTime
                                else -> 0
                            }

                            intervalTime += newValue - oldValue
                            drawCircle(context)
                        }
                    }.lparams {
                        width = wrapContent
                        height = dip(100)
                        rightOf(R.id.hourTextView)
                    }

                    textView("分間") {
                        id = R.id.minuteTextView
                    }.lparams {
                        rightOf(R.id.intervalMinuteNumPick)
                        centerVertically()
                    }
                }.lparams {
                    centerHorizontally()
                    below(R.id.titleIntervalBreakTime)
                    width = wrapContent
                    height = wrapContent
                }

                timerFrame = frameLayout {
                    id = R.id.circleFrame
                }.lparams {
                    width = dip(150)
                    height = dip(150)
                    below(R.id.intervalTimeRelative)
                    centerHorizontally()
                }

                button("次へ") {
                    id = R.id.nextButton
                    backgroundColor = Color.argb(
                            0,
                            0,
                            0,
                            0
                    )
                    textColor = ContextCompat.getColor(
                            context,
                            R.color.colorPrimary
                    )
                    textSize = 20f
                }.lparams {
                    margin = 30
                    alignParentBottom()
                    alignParentEnd()
                }
            }.lparams {
                below(R.id.ankoToolbar)
            }
        }
    }

    private fun drawCircle(context: Context) {
        GlobalValue.apply {
            focusTimeG = focusTime.toLong()
            intervalTimeG = intervalTime.toLong()
        }

        val intervalColor = ContextCompat.getColor(
                context,
                R.color.colorPrimaryDark
        )
        val redColor = ContextCompat.getColor(
                context,
                R.color.mostPriority
        )

        val params: ArrayList<HashMap<String, Int>> = java.util.ArrayList()
        var time = 0
        val colors = arrayListOf<Int>()
        val drawCircleTime = arrayListOf<Int>()

        while (true) {
            if (60 <= time + focusTime) {
                drawCircleTime.add(60 - time)
                colors.add(redColor)
                break
            } else if (focusTime != 0) {
                drawCircleTime.add(focusTime)
                colors.add(redColor)
            } else {
            }
            time += focusTime

            if (60 <= time + intervalTime) {
                drawCircleTime.add(60 - time)
                colors.add(intervalColor)
                break
            } else if (intervalTime != 0) {
                drawCircleTime.add(intervalTime)
                colors.add(intervalColor)
            } else {
            }
            time += intervalTime
        }

        (0 until colors.size).forEach {
            val mapSI = HashMap<String, Int>()
            mapSI["color"] = colors[it]
            mapSI["value"] = drawCircleTime[it]
            params.add(mapSI)
        }

        val circleGraphView = CircleGraphView(
                context,
                params,
                true
        )
        timerFrame.addView(circleGraphView)
        circleGraphView.startAnimation()
    }
}
