package jp.ict.muffin.otasukejuru.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import jp.ict.muffin.otasukejuru.ui.InputProgressActivityUI
import org.jetbrains.anko.setContentView

class InputProgressActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val index = intent.getIntExtra("index", 0)
        InputProgressActivityUI(index).setContentView(this)
    }
}
