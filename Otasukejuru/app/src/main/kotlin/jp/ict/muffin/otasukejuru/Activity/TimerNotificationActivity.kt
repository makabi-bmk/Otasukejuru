package jp.ict.muffin.otasukejuru

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.setContentView

class TimerNotificationActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TimerNotificationActivityUI(intent.getLongExtra("time", 0)).setContentView(this)
    }
}
