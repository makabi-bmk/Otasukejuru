package jp.ict.muffin.otasukejuru.ui.timer.timer

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue.notificationContent
import jp.ict.muffin.otasukejuru.`object`.GlobalValue.notificationId
import jp.ict.muffin.otasukejuru.other.AlarmReceiver
import jp.ict.muffin.otasukejuru.ui.timer.interval.TimerIntervalActivityUI
import jp.ict.muffin.otasukejuru.view.CircleGraphView
import org.jetbrains.anko.ctx
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.arrayListOf
import kotlin.collections.forEach

class TimerActivity : Activity() {
  private var time: Long = 0
  private var index: Int = -1
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    time = intent.getLongExtra(
      "time",
      0L
    )
    index = intent.getIntExtra(
      "index",
      -1
    )

    setInterval()
  }

  private fun setInterval() {
    TimerIntervalActivityUI(time).setContentView(this)
    find<ImageButton>(R.id.ankoBack).setOnClickListener {
      finish()
    }

    find<Button>(R.id.nextButton).setOnClickListener {
      startTimer()
    }

    val params: ArrayList<HashMap<String, Int>> = arrayListOf(HashMap<String, Int>().apply {
      put(
        "color",
        ContextCompat.getColor(
          ctx,
          R.color.mostPriority
        )
      )
      put(
        "value",
        60
      )
    })

    val circleGraphView = CircleGraphView(
      ctx,
      params,
      true
    )
    find<FrameLayout>(R.id.circleFrame).addView(circleGraphView)
    circleGraphView.startAnimation()
  }

  private fun startTimer() {
    TimerActivityUI(time).setContentView(this)
    (1..3).forEach {
      val calendar = Calendar.getInstance()
      calendar.apply {
        timeInMillis = System.currentTimeMillis()
        add(
          Calendar.SECOND,
          time.time.toInt() - 5
        )
      }
      scheduleNotification(
        "終了${it * 5}分前です",
        calendar,
        it
      )
    }
  }

  private fun scheduleNotification(
    content: String,
    calendar: Calendar,
    id: Int
  ) {
    val notificationIntent = Intent(
      this,
      AlarmReceiver::class.java
    )
    notificationIntent.apply {
      putExtra(
        notificationId,
        id
      )
      putExtra(
        notificationContent,
        content
      )
    }
    val pendingIntent = PendingIntent.getBroadcast(
      this,
      0,
      notificationIntent,
      PendingIntent.FLAG_UPDATE_CURRENT
    )

    (getSystemService(Context.ALARM_SERVICE) as AlarmManager).setExact(
      AlarmManager.RTC_WAKEUP,
      calendar.timeInMillis,
      pendingIntent
    )
  }
}
