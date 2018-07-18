package jp.ict.muffin.otasukejuru.ui

import android.graphics.Color
import android.view.View
import jp.ict.muffin.otasukejuru.R
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.activity.InputProgressActivity
import jp.ict.muffin.otasukejuru.communication.UpdateTaskInfoAsync
import kotlinx.android.synthetic.main.activity_selection.view.textView
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.seekBar
import org.jetbrains.anko.margin
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.textView
import org.jetbrains.anko.button
import org.jetbrains.anko.textColor
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.centerHorizontally
import org.jetbrains.anko.centerVertically
import org.jetbrains.anko.wrapContent
import org.jetbrains.anko.below
import org.jetbrains.anko.dip
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.alignParentBottom
import org.jetbrains.anko.alignParentEnd
import org.jetbrains.anko.centerHorizontally

class InputProgressActivityUI(private val index: Int) : AnkoComponent<InputProgressActivity> {
    private lateinit var progressSeekBar: SeekBar

    override fun createView(ui: AnkoContext<InputProgressActivity>): View = with(ui) {
        relativeLayout {
            relativeLayout {
                progressSeekBar = seekBar {
                    id = R.id.inputProgressSeek
                    progress = GlobalValue.taskInfoArrayList[index].progress
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    margin = dip(100)
                }

                textView(GlobalValue.taskInfoArrayList[index].progress.toString()) {
                    id = R.id.progressTextView
                    text = progressSeekBar.progress.toString()
                    textSize = 20f
                }.lparams {
                    below(progressSeekBar)
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
                backgroundColor = Color.argb(
                        0,
                        0,
                        0,
                        0
                )
                textColor = ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                )
                textSize = 20f
                onClick {
                    GlobalValue.taskInfoArrayList[index].progress = progressSeekBar.progress
                    UpdateTaskInfoAsync().execute(GlobalValue.taskInfoArrayList[index])
                }
            }.lparams {
                margin = 30
                alignParentBottom()
                alignParentEnd()
            }
        }
    }
}