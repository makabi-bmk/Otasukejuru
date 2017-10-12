package jp.ict.muffin.otasukejuru

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.setContentView

class TimerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TimerActivityUI(intent.getLongExtra("time", 0)).setContentView(this)
    }
}
