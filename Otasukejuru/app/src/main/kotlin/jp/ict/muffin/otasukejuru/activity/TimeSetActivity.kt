package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.os.Bundle
import jp.ict.muffin.otasukejuru.ui.TimeSetActivityUI
import org.jetbrains.anko.setContentView


class TimeSetActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        TimeSetActivityUI().setContentView(this)
    }
}