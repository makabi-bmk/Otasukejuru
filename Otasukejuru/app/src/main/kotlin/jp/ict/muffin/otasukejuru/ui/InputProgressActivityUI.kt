package jp.ict.muffin.otasukejuru.ui

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.SeekBar
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.activity.InputProgressActivity
import jp.ict.muffin.otasukejuru.communication.UpdateTaskInfoAsync
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class InputProgressActivityUI(private val index: Int) : AnkoComponent<InputProgressActivity> {
    private lateinit var seekBar: SeekBar
    override fun createView(ui: AnkoContext<InputProgressActivity>): View = with(ui) {
        relativeLayout {
            relativeLayout {
                seekBar = seekBar {
                    id = R.id.inputProgressSeek
                    progress = GlobalValue.taskInfoArrayList[index].progress
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    margin = dip(100)
                }
                
                textView(GlobalValue.taskInfoArrayList[index].progress.toString()) {
                    id = R.id.progressTextView
                    text = seekBar.progress.toString()
                    textSize = 20f
                }.lparams {
                    below(seekBar)
                    centerHorizontally()
                }
            }.lparams {
                width = wrapContent
                height = wrapContent
                centerVertically()
                centerHorizontally()
            }
            
            button("確定") {
                id = R.id.finishButton
                backgroundColor = Color.argb(0, 0, 0, 0)
                textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                textSize = 20f
                onClick {
                    GlobalValue.taskInfoArrayList[index].progress = seekBar.progress
                    Log.d("progress", seekBar.progress.toString())
                    val update = UpdateTaskInfoAsync()
                    update.execute(GlobalValue.taskInfoArrayList[index])
                }
            }.lparams {
                margin = 30
                alignParentBottom()
                alignParentRight()
            }
        }
    }
}