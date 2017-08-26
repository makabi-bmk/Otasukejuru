package jp.ict.muffin.otasukejuru

import android.view.Gravity
import android.view.View
import org.jetbrains.anko.*

/**
 * Created by mito on 2017/08/24.
 */
class TimerFragmentUI : AnkoComponent<TimerFragment>{
    lateinit var countDown: CountDown
    var isPushStartButton = false
    
    override fun createView(ui: AnkoContext<TimerFragment>): View = with(ui) {
        
        verticalLayout {
            lparams {
                gravity = Gravity.CENTER_HORIZONTAL
            }
            frameLayout {
                id = 1
            }.lparams(height = 750, width = 750)
            val timerText = textView {
                text = context.getString(R.string.initTime)
                textSize = 20f
                gravity = Gravity.CENTER_HORIZONTAL
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
            button("cancel") {
            }.setOnClickListener {
                if (isPushStartButton) {
                    countDown.cancel()
                    timerText.text = context.getString(R.string.initTime)
                    isPushStartButton = false
                }
            }
        }
    }
}
