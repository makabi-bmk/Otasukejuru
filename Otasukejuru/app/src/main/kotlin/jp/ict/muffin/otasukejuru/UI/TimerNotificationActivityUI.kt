package jp.ict.muffin.otasukejuru

import android.graphics.Color
import android.view.View
import jp.ict.muffin.otasukejuru.Activity.TimerNotificationActivity
import org.jetbrains.anko.*


class TimerNotificationActivityUI : AnkoComponent<TimerNotificationActivity> {
    override fun createView(ui: AnkoContext<TimerNotificationActivity>): View = with(ui) {
        
        relativeLayout {
            backgroundColor = Color.argb(255, 251, 251, 240)
            button("次へ") {
                
            }.setOnClickListener {
                startActivity<TimerActivity>()
            }
        }
        
    }
}