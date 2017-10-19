package jp.ict.muffin.otasukejuru.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import jp.ict.muffin.otasukejuru.ui.DateActivityUI
import org.jetbrains.anko.setContentView

class DateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DateActivityUI().setContentView(this)
    }
}
