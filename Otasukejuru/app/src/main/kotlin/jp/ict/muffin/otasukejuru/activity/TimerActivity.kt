package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.other.AlarmReceiver
import jp.ict.muffin.otasukejuru.ui.TimerActivityUI
import jp.ict.muffin.otasukejuru.ui.TimerIntervalActivityUI
import jp.ict.muffin.otasukejuru.view.CircleGraphView
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView
import java.util.*

class TimerActivity : Activity() {
    private var time: Long = 0
    private var index: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        time = intent.getLongExtra("time", 0L)
        index = intent.getIntExtra("index", -1)
        
        setInterval()
    }
    
    private fun setInterval() {
        TimerIntervalActivityUI(time).setContentView(this)
        find<ImageButton>(R.id.ankoBack).setOnClickListener {
            finish()
        }
        
        find<Button>(R.id.nextButton).setOnClickListener {
            //            setNotificationTime()
            startTimer()
        }
        
        val params: ArrayList<HashMap<String, Int>> = java.util.ArrayList()
        val mapSI = HashMap<String, Int>()
        mapSI.put("color", ContextCompat.getColor(this, R.color.mostPriority))
        mapSI.put("value", 60)
        params.add(mapSI)
        
        val circleGraphView = CircleGraphView(this, params, true)
        find<FrameLayout>(R.id.circleFrame).addView(circleGraphView)
        circleGraphView.startAnimation()
        
    }

//    private fun setNotificationTime() {
//        TimerNotificationActivityUI(time).setContentView(this)
//
//        find<ImageButton>(R.id.ankoBack).setOnClickListener {
//            setInterval()
//        }
//
//        find<Button>(R.id.nextButton).setOnClickListener {
//            startTimer()
//        }
//
//    }
    
    private fun startTimer() {
        TimerActivityUI(time).setContentView(this)
        (1..3).forEach {
            val calendar = Calendar.getInstance()
            calendar.apply {
                timeInMillis = System.currentTimeMillis()
                add(Calendar.SECOND, time.time.toInt() - 5)
            }
            scheduleNotification("終了${it * 5}分前です", calendar)
        }
    }
    
    private fun scheduleNotification(content: String, calendar: Calendar) {
        val notificationIntent = Intent(this, AlarmReceiver::class.java)
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, 1)
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_CONTENT, content)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}
