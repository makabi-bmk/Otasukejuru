package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.os.Bundle
import jp.ict.muffin.otasukejuru.ui.TimerActivityUI
import org.jetbrains.anko.setContentView

class TimerActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TimerActivityUI(intent.getLongExtra("time", 0)).setContentView(this)
    }
}
