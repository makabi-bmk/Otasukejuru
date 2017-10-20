package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.os.Bundle
import android.widget.ImageButton
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.ui.TimerNotificationActivityUI
import org.jetbrains.anko.find
import org.jetbrains.anko.findOptional
import org.jetbrains.anko.setContentView

class TimerNotificationActivity : Activity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TimerNotificationActivityUI(intent.getLongExtra("time", 0)).setContentView(this)
    
        find<ImageButton>(R.id.ankoBack).setOnClickListener {
            finish()
        }
    }
}
