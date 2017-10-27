package jp.ict.muffin.otasukejuru.fragment

import android.app.AlertDialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.TaskInfo
import jp.ict.muffin.otasukejuru.activity.TaskAdditionActivity
import jp.ict.muffin.otasukejuru.activity.TimeSetActivity
import jp.ict.muffin.otasukejuru.other.SplitDate
import jp.ict.muffin.otasukejuru.ui.ToDoListFragmentUI
import kotlinx.android.synthetic.main.fragment_list_todo.*
import kotlinx.android.synthetic.main.task_card_view.view.*
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.textColor
import java.util.*


class ToDoListFragment : Fragment() {
    
    private var mTimer: Timer? = null
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            ToDoListFragmentUI().createView(AnkoContext.create(ctx, this))
//            inflater.inflate(R.layout.fragment_list_todo, container, false)
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setCardView()
    }
    
    override fun onResume() {
        super.onResume()
        val mHandler = Handler()
        mTimer = Timer()
        mTimer?.schedule(object : TimerTask() {
            override fun run() {
                mHandler.post {
                    setCardView()
                }
            }
        }, 5000, 5000)
    }
    
    override fun onStop() {
        super.onStop()
        mTimer?.cancel()
        mTimer = null
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
            }?.removeAllViews()
        }
        
        var highPriorityNum = 0
        var middlePriorityNum = 0
        var lowPriorityNum = 0
        val calendar = Calendar.getInstance()
        val today = (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH)
        
        val showTaskNum = GlobalValue.displayWidth / 90 - 1
        GlobalValue.taskInfoArrayList.forEach { element ->
            val diffDays = diffDayNum(today, SplitDate().getDate(element.due_date), calendar.get
            (Calendar
                    .YEAR))
            
            val inflater: LayoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val linearLayout: LinearLayout = inflater.inflate(R.layout.task_card_view, null) as LinearLayout
            
            linearLayout.apply {
                dateTextView.apply {
                    text = diffDays.toString()
                    if (element.priority == 0) {
                        textColor = ContextCompat.getColor(context, R.color.mostPriority)
                    }
                }
                taskNameTextView.text = element.task_name
                cardView.apply {
                    tag = SplitDate().getDate(element.due_date)
                    setOnClickListener {
                        createDialog(taskNameTextView.text.toString(), element)
                    }
                }
            }
            when (element.priority) {
                0 -> mostPriorityCardLinear
                1 -> {
                    highPriorityNum++
                    if (highPriorityNum <= showTaskNum) {
                        highPriorityCardLinear1
                    } else {
                        highPriorityCardLinear2
                    }
                }
                2 -> {
                    middlePriorityNum++
                    if (middlePriorityNum <= showTaskNum) {
                        middlePriorityCardLinear1
                    } else {
                        middlePriorityCardLinear2
                    }
                }
                else -> {
                    lowPriorityNum++
                    if (lowPriorityNum <= showTaskNum) {
                        lowPriorityCardLinear1
                    } else {
                        lowPriorityCardLinear2
                    }
                }
            }.addView(linearLayout, 0)
        }
    }
    
    private fun createDialog(tag: String, element: TaskInfo) {
        val listDialog = arrayOf("開始", "変更", "完了", "削除")
        
        AlertDialog.Builder(activity)
                .setTitle(tag)
                .setItems(listDialog) { _, which ->
                    when (which) {
                        0 -> {
                            startActivity<TimeSetActivity>()
                        }
                        
                        1 -> {
                            startActivity<TaskAdditionActivity>("init" to true)
                        }
                        
                        else -> {
                            deleteTask(element)
                        }
                        
                    }
                }
                .show()
    }
    
    private fun deleteTask(element: TaskInfo) {
        GlobalValue.taskInfoArrayList.remove(element)
        setCardView()
    }
    
    private fun diffDayNum(beforeDate: Int, afterDate: Int, year: Int): Int {
        
        val totalDays = if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        } else {
            intArrayOf(0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335)
        }
        
        val beforeDay: Int = beforeDate % 100
        val beforeMonth: Int = beforeDate / 100
        val afterDay: Int = afterDate % 100
        val afterMonth: Int = afterDate / 100
        return (totalDays[afterMonth] + afterDay - (totalDays[beforeMonth] + beforeDay))
    }
}