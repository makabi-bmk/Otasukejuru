package jp.ict.muffin.otasukejuru

import android.app.Activity
import android.os.CountDownTimer
import android.widget.TextView

/**
 * Created by mito on 2017/08/24.
 */
class CountDown(millisUntilFinished: Long, countDownInterval: Long, private val timerText: TextView) : CountDownTimer(millisUntilFinished, countDownInterval) {
    
    override fun onFinish() {
        timerText.text = Activity().getString(R.string.initTime)
    }
    
    override fun onTick(millisUntilFinished: Long) {
        val m = millisUntilFinished / 1000 / 60
        val s = millisUntilFinished / 1000 % 60
        val ms = millisUntilFinished - s * 1000 - m * 1000 * 60
        
        timerText.text = String.format("%1$02d:%2$02d.%3$03d", m, s, ms)
    }
}