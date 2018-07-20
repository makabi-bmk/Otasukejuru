package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.ui.TimeSetActivityUI
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView

class TimeSetActivity : Activity() {

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
    }
}