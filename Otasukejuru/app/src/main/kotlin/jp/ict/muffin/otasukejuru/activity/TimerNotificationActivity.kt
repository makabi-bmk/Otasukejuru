package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.os.Bundle
import jp.ict.muffin.otasukejuru.ui.TimerNotificationActivityUI
import org.jetbrains.anko.setContentView

class TimerNotificationActivity : Activity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TimerNotificationActivityUI(intent.getLongExtra("time", 0)).setContentView(this)
    }
}
