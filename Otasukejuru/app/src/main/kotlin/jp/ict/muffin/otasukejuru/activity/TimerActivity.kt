package jp.ict.muffin.otasukejuru.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import jp.ict.muffin.otasukejuru.ui.TimerActivityUI
import org.jetbrains.anko.setContentView

class TimerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TimerActivityUI(intent.getLongExtra("time", 0)).setContentView(this)
    }
}
