package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.os.Bundle
import android.widget.ImageButton
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.ui.TimerActivityUI
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView

class TimerActivity : Activity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TimerActivityUI(intent.getLongExtra("time", 0)).setContentView(this)
        
        find<ImageButton>(R.id.ankoBack).setOnClickListener {
            finish()
        }
    }
}
