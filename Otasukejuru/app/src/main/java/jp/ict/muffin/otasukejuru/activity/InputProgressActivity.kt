package jp.ict.muffin.otasukejuru.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.other.Utils
import jp.ict.muffin.otasukejuru.ui.InputProgressActivityUI
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.setContentView

class InputProgressActivity : AppCompatActivity() {

    companion object {
        fun start(
            context: Context?,
            index: Int = -1
        ) {

            val intent = Intent(
                    context,
                    InputProgressActivity::class.java
            )

            intent.putExtra(
                    "index",
                    index
            )

            context?.startActivity(intent)
        }
    }
    private var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        index = intent.getIntExtra(
                "index",
                -1
        )
        InputProgressActivityUI(index).setContentView(this)
    }

    override fun onResume() {
        super.onResume()
        val seekBar = find<SeekBar>(R.id.inputProgressSeek)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            // ツマミがドラッグされると呼ばれる
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                find<TextView>(R.id.progressTextView).text = seekBar.progress.toString()
                GlobalValue.taskInfoArrayList[index].progress = progress
            }

            // ツマミがタッチされた時に呼ばれる
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            // ツマミがリリースされた時に呼ばれる
            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        find<Button>(R.id.finishButton).onClick {
            Utils().saveTaskInfoList(this@InputProgressActivity)
            finish()
        }
    }
}
