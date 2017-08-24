package jp.ict.muffin.otasukejuru

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI


/**
 * Created by mito on 2017/08/23.
 */
class TimerFragment : Fragment() {
    lateinit var countDown: CountDown
    var isPushStartButton = false
    companion object {
        fun getInstance(): TimerFragment = TimerFragment()
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            UI {
                verticalLayout {
                    lparams {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    val timerText = textView {
                        text = getString(R.string.initTime)
                        textSize = 20f
                        gravity = Gravity.CENTER_HORIZONTAL
                        topPadding = dip(150)
                    }
                    button("start") {
                        height = wrapContent
                        width = 50
                        gravity = Gravity.CENTER_HORIZONTAL
                    }.setOnClickListener {
                        if (!isPushStartButton) {
                            countDown = CountDown(180000, 100, timerText)
                            countDown.start()
                            isPushStartButton = true
                        }
                    }
                    button("stop") {
                    }.setOnClickListener {
                        if (isPushStartButton) {
                            countDown.cancel()
                            timerText.text = getString(R.string.initTime)
                            isPushStartButton = false
                        }
                        
                    }
                }
            }.view
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
    }
    
    
}