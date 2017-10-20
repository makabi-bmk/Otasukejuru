package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.os.Bundle
import android.widget.ImageButton
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.ui.DateActivityUI
import org.jetbrains.anko.find
import org.jetbrains.anko.findOptional
import org.jetbrains.anko.setContentView

class DateActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DateActivityUI().setContentView(this)
        setActionBar(findOptional(R.id.ankoToolbar))
        
        find<ImageButton>(R.id.ankoBack).setOnClickListener {
            finish()
        }
    }
}
