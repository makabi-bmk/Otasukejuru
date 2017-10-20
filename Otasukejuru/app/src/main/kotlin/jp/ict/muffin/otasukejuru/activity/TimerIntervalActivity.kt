package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.os.Bundle
import android.widget.ImageButton
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.ui.TimerIntervalActivityUI
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView

class TimerIntervalActivity : Activity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TimerIntervalActivityUI(intent.getLongExtra("time", 0)).setContentView(this)
    
        find<ImageButton>(R.id.ankoBack).setOnClickListener {
            finish()
        }
    }
}
