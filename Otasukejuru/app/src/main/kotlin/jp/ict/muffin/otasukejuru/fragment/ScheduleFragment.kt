package jp.ict.muffin.otasukejuru.fragment

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.ui.ScheduleFragmentUI
import kotlinx.android.synthetic.main.task_card_view.view.*
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.dip
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.textColor
import java.util.*


class ScheduleFragment : Fragment() {
    private var mTimer: Timer? = null
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            ScheduleFragmentUI().createView(AnkoContext.create(ctx, this))
    
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    
    override fun onPause() {
        super.onPause()
        mTimer?.cancel()
        mTimer = null
    }
    
    private fun setSchedule() {
        val calendar = Calendar.getInstance()
        val today = (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH)
        
        GlobalValue.scheduleInfoArrayList.forEach {
            val showScheduleDate = today
        }
    
    }
    
    private fun setCardView() {
        val calendar = Calendar.getInstance()
        val today = (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH)
        val showTaskNum = (GlobalValue.displayWidth - 50) / 90 - 1
        
        val forNum = minOf(showTaskNum, GlobalValue.taskInfoArrayList.size)
        find<LinearLayout>(R.id.taskLinear).removeAllViews()
        (0 until forNum).forEach {
            val taskInfo = GlobalValue.taskInfoArrayList[it]
            val diffDays = diffDayNum(today, taskInfo.limitDate, calendar.get(Calendar.YEAR))
            
            val inflater: LayoutInflater =
                    context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val linearLayout: LinearLayout =
                    inflater.inflate(R.layout.task_card_view, null) as LinearLayout
            
            linearLayout.apply {
                dateTextView?.apply {
                    text = diffDays.toString()
                    if (taskInfo.priority == 0) {
                        textColor = ContextCompat.getColor(context, R.color.mostPriority)
                    }
                }
                taskNameTextView?.text = taskInfo.task_name
                cardView?.apply {
                    tag = taskInfo.limitDate
                    setOnClickListener {
                    }
                }
            }
            find<LinearLayout>(R.id.taskLinear).addView(linearLayout, it)
            
            
            val line = LinearLayout(context)
            val lParam = RelativeLayout.LayoutParams(0, 0)
            lParam.apply {
                width = 3
                height = diffDays * dip(150) + dip(it - 1)
                leftMargin = dip(80 + 45 + 90 * it)
                topMargin = dip(25)
            }
            line.apply {
                layoutParams = lParam
                backgroundColor = ContextCompat.getColor(context, R.color.mostPriority)
            }
            find<RelativeLayout>(R.id.taskRelative).addView(line)
        }
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
