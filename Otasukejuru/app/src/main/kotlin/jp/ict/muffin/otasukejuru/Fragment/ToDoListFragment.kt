package jp.ict.muffin.otasukejuru

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import jp.ict.muffin.otasukejuru.MainActivity.taskInformationArrayList
import kotlinx.android.synthetic.main.fragment_list_todo.*
import kotlinx.android.synthetic.main.task_card_view.view.*
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.textColor
import java.util.*


class ToDoListFragment : Fragment() {
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_list_todo, container, false)
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setCardView()
    }
    
    override fun onResume() {
        super.onResume()
        val mHandler = Handler()
        val mTimer = Timer()
        mTimer.schedule(object : TimerTask() {
            override fun run() {
                mHandler.post {
                    setCardView()
                }
            }
            
            
        }, 5000, 5000)
    }
    
    fun setCardView() {
        (0..6).forEach {
            when (it) {
                0 -> mostPriorityCardLinear
                1 -> highPriorityCardLinear1
                2 -> highPriorityCardLinear2
                3 -> middlePriorityCardLinear1
                4 -> middlePriorityCardLinear2
                5 -> lowPriorityCardLinear1
                else -> lowPriorityCardLinear2
            }.removeAllViews()
        }
        
        var highPriorityNum = 0
        var middlePriorityNum = 0
        var lowPriorityNum = 0
        (0 until taskInformationArrayList.size).forEach {
            val inflater: LayoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val linearLayout: LinearLayout = inflater.inflate(R.layout.task_card_view, null) as LinearLayout
            linearLayout.apply {
                dateTextView.apply {
                    text = (taskInformationArrayList[it].limitDate % 100).toString()
                    if (taskInformationArrayList[it].priority == 0) {
                        textColor = context.resources.getColor(R.color.mostPriority)
                    }
                }
                cardView.apply {
                    tag = taskInformationArrayList[it].limitDate
                    setOnClickListener {
                        toast(tag.toString())
                    }
                }
                taskNameTextView.text = taskInformationArrayList[it].name
            }
            when (taskInformationArrayList[it].priority) {
                0 -> mostPriorityCardLinear
                1 -> {
                    highPriorityNum++
                    if (4 < highPriorityNum) {
                        highPriorityCardLinear1
                    } else {
                        highPriorityCardLinear2
                    }
                }
                2 -> {
                    middlePriorityNum++
                    if (4 < middlePriorityNum) {
                        middlePriorityCardLinear1
                    } else {
                        middlePriorityCardLinear2
                    }
                }
                else -> {
                    lowPriorityNum++
                    if (4 < lowPriorityNum) {
                        lowPriorityCardLinear1
                    } else {
                        lowPriorityCardLinear2
                    }
                }
            }.addView(linearLayout, 0)
        }
    }
}