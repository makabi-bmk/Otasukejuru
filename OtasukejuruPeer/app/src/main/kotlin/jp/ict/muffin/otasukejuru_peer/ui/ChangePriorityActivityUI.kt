package jp.ict.muffin.otasukejuru_peer.ui

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.View
import jp.ict.muffin.otasukejuru_peer.R
import jp.ict.muffin.otasukejuru_peer.activity.AdditionActivity
import org.jetbrains.anko.*


class ChangePriorityActivityUI : AnkoComponent<AdditionActivity> {
    override fun createView(ui: AnkoContext<AdditionActivity>): View = with(ui) {
        linearLayout {
            toolbar {
                id = R.id.ankoToolbar
                backgroundColor = ContextCompat.getColor(context, R.color.colorPrimary)
            }.lparams {
                width = matchParent
                height = wrapContent
            }
            
            relativeLayout {
                relativeLayout {
                    textView("優先度変更") {
                        id = R.id.title
                        textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                        textSize = sp(30).toFloat()
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        topMargin = dip(10)
                        leftMargin = dip(30)
                    }
                    textView("優先度を決めてください") {
                        textSize = sp(25).toFloat()
                    }.lparams {
                        below(R.id.title)
                        width = wrapContent
                        height = wrapContent
                        topMargin = dip(10)
                        leftMargin = dip(50)
                        rightMargin = dip(50)
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                }
                
                radioGroup {
                    id = R.id.changePriorityRadioGroup
                    radioButton {
                        id = R.id.radioPriority1
                        text = context.getString(R.string.mostPriority)
                        textSize = sp(25).toFloat()
                        check(true)
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        margin = dip(5)
                    }
                    radioButton {
                        id = R.id.radioPriority2
                        text = context.getString(R.string.highPriority)
                        textSize = sp(25).toFloat()
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        margin = dip(5)
                    }
                    radioButton {
                        id = R.id.radioPriority3
                        text = context.getString(R.string.middlePriority)
                        textSize = sp(25).toFloat()
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        margin = dip(5)
                    }
                    radioButton {
                        id = R.id.radioPriority4
                        text = context.getString(R.string.lowPriority)
                        textSize = sp(25).toFloat()
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        margin = dip(5)
                    }
                }.lparams {
                    width = wrapContent
                    height = wrapContent
                    centerHorizontally()
                    centerVertically()
                }
                
                button("次へ") {
                    id = R.id.nextButton
                    backgroundColor = Color.argb(0, 0, 0, 0)
                    textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                    textSize = 20f
                }.lparams {
                    margin = 30
                    alignParentBottom()
                    alignParentRight()
                }
            }.lparams {
                width = matchParent
                height = matchParent
            }
        }
        
    }
}