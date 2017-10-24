package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.ui.TimerActivityUI
import jp.ict.muffin.otasukejuru.ui.TimerIntervalActivityUI
import jp.ict.muffin.otasukejuru.ui.TimerNotificationActivityUI
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView

class TimerActivity : Activity() {
    private var time: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        time = intent.getLongExtra("time", 0L)
        
        setInterval()
    }
    
    private fun setInterval() {
        TimerIntervalActivityUI(time).setContentView(this)
        find<ImageButton>(R.id.ankoBack).setOnClickListener {
            finish()
        }
        
        find<Button>(R.id.nextButton).setOnClickListener {
            setNotificationTime()
        }
    }
    
    private fun setNotificationTime() {
        TimerNotificationActivityUI(time).setContentView(this)
        
        find<ImageButton>(R.id.ankoBack).setOnClickListener {
           setInterval()
        }
        
        find<Button>(R.id.nextButton).setOnClickListener {
            startTimer()
        }
        
    }
    
    private fun startTimer() {
        TimerActivityUI(time).setContentView(this)
        
        find<ImageButton>(R.id.ankoBack).setOnClickListener {
            setNotificationTime()
        }
        
    }
}
