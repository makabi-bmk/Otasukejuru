package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.ui.TimerActivityUI
import jp.ict.muffin.otasukejuru.ui.TimerIntervalActivityUI
import jp.ict.muffin.otasukejuru.view.CircleGraphView
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView

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
    }
}
