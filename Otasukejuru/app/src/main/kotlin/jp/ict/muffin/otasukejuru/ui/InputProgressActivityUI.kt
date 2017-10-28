package jp.ict.muffin.otasukejuru.ui

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.View
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.activity.InputProgressActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class InputProgressActivityUI(private val index: Int) : AnkoComponent<InputProgressActivity> {
    override fun createView(ui: AnkoContext<InputProgressActivity>): View = with(ui) {
        relativeLayout {
            val progressBar = progressBar {
                id = R.id.inputProgressBar
                progress = GlobalValue.taskInfoArrayList[index].progress
            }.lparams {
                width = wrapContent
                height = wrapContent
                centerHorizontally()
                centerVertically()
            }
            
            button("確定") {
                id = R.id.nextButton
                backgroundColor = Color.argb(0, 0, 0, 0)
                textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                textSize = 20f
                onClick {
                    GlobalValue.taskInfoArrayList[index].progress = progressBar.progress
                }
            }.lparams {
                margin = 30
                alignParentBottom()
                alignParentRight()
            }
        }
        
    }
}