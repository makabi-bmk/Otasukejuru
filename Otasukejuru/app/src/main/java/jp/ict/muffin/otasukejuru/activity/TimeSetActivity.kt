package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.ui.TimeSetActivityUI
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView

class TimeSetActivity : AppCompatActivity(), TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
    
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
    
    override fun afterTextChanged(p0: Editable?) {
        findViewById<Button>(R.id.nextButton)?.let {
            if (p0.toString() != "") {
                it.setTextColor(ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                ))
                it.isEnabled = true
            } else {
                
                it.setTextColor(Color.argb(
                        0,
                        0,
                        0,
                        0
                ))
                it.isEnabled = false
                
            }
        }
    }
    
    companion object {
        fun start(
            context: Context?,
            index: Int = -1
        ) {
            val intent = Intent(
                    context,
                    TimeSetActivity::class.java
            )

            intent.putExtra(
                    "index",
                    index
            )

            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val index = intent.getIntExtra(
                "index",
                -1
        )
        TimeSetActivityUI(index).setContentView(this)

        find<ImageButton>(R.id.ankoBack).setOnClickListener {
            finish()
        }
        
        find<EditText>(R.id.setTimeEdit).addTextChangedListener(this)
    }
}
