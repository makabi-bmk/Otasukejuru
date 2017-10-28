package jp.ict.muffin.otasukejuru.fragment

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.other.Utils
import jp.ict.muffin.otasukejuru.ui.ScheduleFragmentUI
import kotlinx.android.synthetic.main.task_card_view.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.dip
import org.jetbrains.anko.support.v4.find
import java.util.*


class ScheduleFragment : Fragment() {
    private var mTimer: Timer? = null
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            ScheduleFragmentUI().createView(AnkoContext.create(ctx, this))
    
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSchedule()
        setCardView()
        
    }
    
    override fun onResume() {
        super.onResume()
        val mHandler = Handler()
        mTimer = Timer()
        mTimer?.schedule(object : TimerTask() {
            override fun run() {
                mHandler.post {
                    find<RelativeLayout>(R.id.refreshRelative).removeAllViews()
                    setSchedule()
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
        
        find<RelativeLayout>(R.id.refreshRelative).removeAllViews()
        GlobalValue.scheduleInfoArrayList.forEach {
            val showScheduleDate = today + 7
            
            val diffDays = Utils().diffDayNum(today, Utils().getDate(it.start_time), calendar.get
            (Calendar.YEAR))
            if (Utils().getDate(it.start_time) in today..showScheduleDate) {
                val line = RelativeLayout(context)
                val rParam = RelativeLayout.LayoutParams(0, 0)
                val endMinute = Utils().getTime(it.end_time) / 100 * 60 +
                        Utils().getTime(it.end_time) % 100
                val startMinute = Utils().getTime(it.start_time) / 100 * 60 +
                        Utils().getTime(it.start_time) % 100
                rParam.apply {
                    width = matchParent
                    height = ((Utils().getDate(it.end_time) - Utils().getDate(it.start_time)) *
                            dip(150) + endMinute * dip(0.18f))
                    Log.d("height", endMinute.toString())
                    leftMargin = dip(120)
                    rightMargin = dip(60)
                    topMargin = dip(25) + diffDays * dip(2000) +
                            dip(0.18f) * startMinute
                }
                line.apply {
                    layoutParams = rParam
                    backgroundColor = Color.argb(100, 112, 173, 71)
                }
                val scheduleNameText = TextView(context)
                val tParam = RelativeLayout.LayoutParams(wrapContent, wrapContent)
                tParam.apply {
                    addRule(RelativeLayout.CENTER_HORIZONTAL)
                    addRule(RelativeLayout.CENTER_VERTICAL)
                }
                scheduleNameText.apply {
                    layoutParams = tParam
                    text = it.schedule_name
                }
                line.addView(scheduleNameText)
                find<RelativeLayout>(R.id.refreshRelative).addView(line)
            }
        }
        
    }
    
    
    private fun setCardView() {
        val calendar = Calendar.getInstance()
        val today = (calendar.get(Calendar.MONTH) + 1) * 100 +
                calendar.get(Calendar.DAY_OF_MONTH)
        val showTaskNum = (GlobalValue.displayWidth - 50) / 90 - 3
        
        val forNum = minOf(showTaskNum, GlobalValue.taskInfoArrayList.size)
        find<LinearLayout>(R.id.taskLinear).removeAllViews()
        (0 until forNum).forEach {
            val taskInfo = GlobalValue.taskInfoArrayList[it]
            val hoge = Utils().getTime(taskInfo.due_date) / 100 * 60 +
                    Utils().getTime(taskInfo.due_date) % 100
            
            val diffDays = Utils().diffDayNum(today, Utils().getDate(taskInfo.due_date),
                    calendar.get(Calendar.YEAR))
            
            if (-1 < diffDays) {
                
                val inflater: LayoutInflater =
                        context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val linearLayout: LinearLayout =
                        inflater.inflate(R.layout.task_card_view,
                                null) as LinearLayout
                
                linearLayout.apply {
                    dateTextView.apply {
                        text = diffDays.toString()
                        if (taskInfo.priority == 0) {
                            textColor = ContextCompat.getColor(context,
                                    R.color.mostPriority)
                        }
                    }
                    taskNameTextView.text = taskInfo.task_name
                    cardView.apply {
                        tag = Utils().getDate(taskInfo.due_date)
                        setOnClickListener {
                        }
                    }
                }
                find<LinearLayout>(R.id.taskLinear).addView(linearLayout, it)
                
                val line = LinearLayout(context)
                val lParam = RelativeLayout.LayoutParams(0, 0)
                lParam.apply {
                    width = 3
                    height = diffDays * dip(200) + hoge *
                            dip(0.13f) + dip(25)
                    Log.d("time", hoge.toString())
                    leftMargin = dip(80 + 45 + 90 * it)
//                topMargin = dip(25)
                }
                line.apply {
                    layoutParams = lParam
                    backgroundColor = ContextCompat.getColor(context, R.color.mostPriority)
                }
                find<RelativeLayout>(R.id.refreshRelative).addView(line)
            }
        }
    }
}
