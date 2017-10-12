package jp.ict.muffin.otasukejuru

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.setContentView

class TimerIntervalActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TimerIntervalActivityUI().setContentView(this)
    }
    
    
}
