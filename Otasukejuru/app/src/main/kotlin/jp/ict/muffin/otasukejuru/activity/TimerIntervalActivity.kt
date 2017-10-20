package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.os.Bundle
import jp.ict.muffin.otasukejuru.ui.TimerIntervalActivityUI
import org.jetbrains.anko.setContentView

class TimerIntervalActivity : Activity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TimerIntervalActivityUI(intent.getLongExtra("time", 0)).setContentView(this)
    }
}
