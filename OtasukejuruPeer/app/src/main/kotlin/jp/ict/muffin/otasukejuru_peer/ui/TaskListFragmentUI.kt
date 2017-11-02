package jp.ict.muffin.otasukejuru_peer.ui

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import jp.ict.muffin.otasukejuru_peer.R
import jp.ict.muffin.otasukejuru_peer.fragment.TaskListFragment
import org.jetbrains.anko.*


class TaskListFragmentUI : AnkoComponent<TaskListFragment> {
    override fun createView(ui: AnkoContext<TaskListFragment>): View = with(ui) {
        
        relativeLayout {
            lparams {
                width = matchParent
                height = matchParent
            }
            scrollView {
                
                relativeLayout {
                    textView(context.getString(R.string.mostPriority)) {
                        id = R.id.mostPriorityTextView
                        textSize = 20f
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        alignParentStart()
                        alignParentTop()
                        padding = dip(10)
                    }
                    
                    imageView {
                        id = R.id.mostPriorityLine
                        backgroundColor = ContextCompat.getColor(context, R.color.mostPriority)
                    }.lparams {
                        width = matchParent
                        height = dip(1)
                        alignParentStart()
                        below(R.id.mostPriorityTextView)
                        contentDescription = ""
                    }
                    
                    linearLayout {
                        id = R.id.mostPriorityCardLinear
                        orientation = LinearLayout.HORIZONTAL
                    }.lparams {
                        width = matchParent
                        height = wrapContent
                        alignParentStart()
                        below(R.id.mostPriorityLine)
                    }
                    
                    //high
                    textView(context.getString(R.string.highPriority)) {
                        id = R.id.highPriorityTextView
                        textSize = 20f
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        padding = dip(10)
                        topMargin = dip(20)
                        below(R.id.mostPriorityCardLinear)
                    }
                    
                    imageView {
                        id = R.id.highPriorityLine
                        backgroundColor = ContextCompat.getColor(context, R.color.highPriority)
                    }.lparams {
                        width = matchParent
                        height = dip(1)
                        alignParentStart()
                        below(R.id.highPriorityTextView)
                        contentDescription = ""
                    }
                    
                    linearLayout {
                        id = R.id.highPriorityCardLinear1
                        orientation = LinearLayout.HORIZONTAL
                    }.lparams {
                        width = matchParent
                        height = wrapContent
                        alignParentStart()
                        below(R.id.highPriorityLine)
                    }
                    linearLayout {
                        id = R.id.highPriorityCardLinear2
                        orientation = LinearLayout.HORIZONTAL
                    }.lparams {
                        width = matchParent
                        height = wrapContent
                        alignParentStart()
                        below(R.id.highPriorityCardLinear1)
                    }
                    
                    //middle
                    textView(context.getString(R.string.middlePriority)) {
                        id = R.id.middlePriorityTextView
                        textSize = 20f
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        padding = dip(10)
                        topMargin = dip(20)
                        below(R.id.highPriorityCardLinear2)
                    }
                    
                    imageView {
                        id = R.id.middlePriorityLine
                        backgroundColor = ContextCompat.getColor(context, R.color.middlePriority)
                    }.lparams {
                        width = matchParent
                        height = dip(1)
                        alignParentStart()
                        below(R.id.middlePriorityTextView)
                        contentDescription = ""
                    }
                    
                    linearLayout {
                        id = R.id.middlePriorityCardLinear1
                        orientation = LinearLayout.HORIZONTAL
                    }.lparams {
                        width = matchParent
                        height = wrapContent
                        alignParentStart()
                        below(R.id.middlePriorityLine)
                    }
                    linearLayout {
                        id = R.id.middlePriorityCardLinear2
                        orientation = LinearLayout.HORIZONTAL
                    }.lparams {
                        width = matchParent
                        height = wrapContent
                        alignParentStart()
                        below(R.id.middlePriorityCardLinear1)
                    }
                    
                    //low
                    textView(context.getString(R.string.lowPriority)) {
                        id = R.id.lowPriorityTextView
                        textSize = 20f
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        padding = dip(10)
                        topMargin = dip(20)
                        below(R.id.middlePriorityCardLinear2)
                    }
                    
                    imageView {
                        id = R.id.lowPriorityLine
                        backgroundColor = ContextCompat.getColor(context, R.color.lowPriority)
                    }.lparams {
                        width = matchParent
                        height = dip(1)
                        alignParentStart()
                        below(R.id.lowPriorityTextView)
                        contentDescription = ""
                    }
                    
                    linearLayout {
                        id = R.id.lowPriorityCardLinear1
                        orientation = LinearLayout.HORIZONTAL
                    }.lparams {
                        width = matchParent
                        height = wrapContent
                        alignParentStart()
                        below(R.id.lowPriorityLine)
                    }
                    linearLayout {
                        id = R.id.lowPriorityCardLinear2
                        orientation = LinearLayout.HORIZONTAL
                    }.lparams {
                        width = matchParent
                        height = wrapContent
                        alignParentStart()
                        below(R.id.lowPriorityCardLinear1)
                    }
                }
            }.lparams {
                width = matchParent
                height = matchParent
            }
            
        }
        
    }
    
    
}