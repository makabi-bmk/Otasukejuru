package jp.ict.muffin.otasukejuru.Activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import jp.ict.muffin.otasukejuru.TimerNotificationActivityUI
import org.jetbrains.anko.setContentView

class TimerNotificationActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TimerNotificationActivityUI().setContentView(this)
    }
}
