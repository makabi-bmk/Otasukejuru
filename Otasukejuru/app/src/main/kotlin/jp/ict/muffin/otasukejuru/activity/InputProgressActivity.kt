package jp.ict.muffin.otasukejuru.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.communication.UpdateTaskInfoAsync
import jp.ict.muffin.otasukejuru.ui.InputProgressActivityUI
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.setContentView


class InputProgressActivity : AppCompatActivity() {
    private var index: Int = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        index = intent.getIntExtra("index", 0)
        InputProgressActivityUI(index).setContentView(this)
    }
    
    override fun onResume() {
        super.onResume()
        val seekBar = find<SeekBar>(R.id.inputProgressSeek)
        
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            //ツマミがドラッグされると呼ばれる
            override fun onProgressChanged(seekBar: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                find<TextView>(R.id.progressTextView).text = seekBar.progress.toString()
                GlobalValue.taskInfoArrayList[index].progress = progress
            }
            
            //ツマミがタッチされた時に呼ばれる
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            
            //ツマミがリリースされた時に呼ばれる
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            
        })
        
        find<Button>(R.id.finishButton).onClick {
            val update = UpdateTaskInfoAsync()
            update.execute(GlobalValue.taskInfoArrayList[index])
            finish()
        }
    }
}
