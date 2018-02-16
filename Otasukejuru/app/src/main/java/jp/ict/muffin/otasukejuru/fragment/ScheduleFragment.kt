package jp.ict.muffin.otasukejuru.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.activity.AdditionActivity
import jp.ict.muffin.otasukejuru.activity.InputProgressActivity
import jp.ict.muffin.otasukejuru.activity.TimeSetActivity
import jp.ict.muffin.otasukejuru.communication.DeleteScheduleInfo
import jp.ict.muffin.otasukejuru.communication.DeleteTaskInfo
import jp.ict.muffin.otasukejuru.other.Utils
import jp.ict.muffin.otasukejuru.ui.ScheduleFragmentUI
import kotlinx.android.synthetic.main.task_card_view.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.support.v4.*
import java.util.*


class ScheduleFragment : Fragment() {
    private var mTimer: Timer? = null
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            ScheduleFragmentUI().createView(AnkoContext.create(ctx, this))
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSchedule()
        setCardView()
        drawNowLine()
    }
    
    private fun drawNowLine() {
        val nowDate = Utils().getNowDate()
        val nowTime = Utils().getTime(nowDate)
        
        val nowMinute = nowTime / 100 * 60 + nowTime % 100
        val line = RelativeLayout(context)
        val nowText = TextView(context)
        line.also {
            it.layoutParams = RelativeLayout.LayoutParams(matchParent, 3).also {
                it.leftMargin = dip(40)
                it.rightMargin = dip(20)
                it.topMargin = dip(0.1556f * nowMinute)// + dip(25)// - dip(70)
                
            }
            it.backgroundColor = Color.GRAY
        }
        
        nowText.also {
            it.text = "現在"
            it.layoutParams = RelativeLayout.LayoutParams(wrapContent, wrapContent).also {
                it.leftMargin = dip(10)
                it.topMargin = dip(0.1556f * nowMinute) - dip(10)// + dip(25) //- dip(70)
            }
        }
        
        find<RelativeLayout>(R.id.refreshRelative).also {
            it.addView(line)
            it.addView(nowText)
        }
    }
    
    override fun onResume() {
        super.onResume()
        val mHandler = Handler()
        mTimer = Timer()
        mTimer?.schedule(object : TimerTask() {
            override fun run() {
                mHandler.post {
                    find<RelativeLayout>(R.id.refreshRelative).removeAllViews()
                    setEvery()
//                    setSubTask()
                    setSchedule()
                    setCardView()
                    drawNowLine()
                }
            }
        }, 5000, 5000)
    }
    
    override fun onPause() {
        super.onPause()
        mTimer?.cancel()
        mTimer = null
    }
    
    private fun setEvery() {
        val calendar = Calendar.getInstance()
        val today = (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH)
        
        find<RelativeLayout>(R.id.refreshRelative).removeAllViews()
        GlobalValue.everyInfoArrayList.forEachWithIndex { index, everyInfo ->
            val showScheduleDate = today + 7
            
            val diffDays = Utils().diffDayNum(today, Utils().getDate(everyInfo.start_time),
                    calendar.get(Calendar.YEAR))
            
            if (Utils().getDate(everyInfo.start_time) in today..showScheduleDate) {
                val scheduleRelative = RelativeLayout(context)
                val endMinute = Utils().getTime(everyInfo.end_time) / 100 * 60 +
                        Utils().getTime(everyInfo.end_time) % 100
                val startMinute = Utils().getTime(everyInfo.start_time) / 100 * 60 +
                        Utils().getTime(everyInfo.start_time) % 100
                val startDate = Utils().getDate(everyInfo.start_time)
                val endDate = Utils().getDate(everyInfo.end_time)
                
                scheduleRelative.also {
                    it.layoutParams = RelativeLayout.LayoutParams(0, 0).also {
                        it.width = matchParent
                        it.height = dip((Utils().diffDayNum(startDate, endDate,
                                calendar.get(Calendar.YEAR)) * 1440 - startMinute + endMinute) * 0.15f)
                        it.leftMargin = dip(120)
                        it.rightMargin = dip(60)
                        it.topMargin = dip(0.15f * (diffDays * 1440 + startMinute)) + dip(10)
                        
                    }
                    it.backgroundColor = Color.argb(100, 112, 173, 71)
                    it.setOnClickListener {
                        createDialog(index, false)
                    }
                }
                
                val scheduleNameText = TextView(context)
                
                scheduleNameText.also {
                    it.layoutParams = RelativeLayout.LayoutParams(wrapContent, wrapContent).also {
                        it.addRule(RelativeLayout.CENTER_HORIZONTAL)
                        it.addRule(RelativeLayout.CENTER_VERTICAL)
                    }
                    it.text = everyInfo.every_name
                }
                
                scheduleRelative.addView(scheduleNameText)
                find<RelativeLayout>(R.id.refreshRelative).addView(scheduleRelative, 0)
            }
        }
    }
    
    private fun setSchedule() {
        val calendar = Calendar.getInstance()
        val today = (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH)
        find<RelativeLayout>(R.id.refreshRelative).removeAllViews()
        
        GlobalValue.scheduleInfoArrayList.forEachWithIndex { index, scheduleInfo ->
            val showScheduleDate = today + 7
            
            val diffDays = Utils().diffDayNum(today, Utils().getDate(scheduleInfo.start_time),
                    calendar.get(Calendar.YEAR))
            
            if (Utils().getDate(scheduleInfo.start_time) in today..showScheduleDate) {
                val schedule = RelativeLayout(context)
                val endMinute = Utils().getTime(scheduleInfo.end_time) / 100 * 60 +
                        Utils().getTime(scheduleInfo.end_time) % 100
                val startMinute = Utils().getTime(scheduleInfo.start_time) / 100 * 60 +
                        Utils().getTime(scheduleInfo.start_time) % 100
                val startDate = Utils().getDate(scheduleInfo.start_time)
                val endDate = Utils().getDate(scheduleInfo.end_time)
                
                schedule.also {
                    it.layoutParams = RelativeLayout.LayoutParams(0, 0).also {
                        it.width = matchParent
                        it.height = dip((Utils().diffDayNum(startDate, endDate,
                                calendar.get(Calendar.YEAR)) * 1440 - startMinute + endMinute) * 0.15f)
                        it.leftMargin = dip(120)
                        it.rightMargin = dip(60)
                        it.topMargin = dip(0.15f * (diffDays * 1440 + startMinute)) + dip(10)
                    }
                    it.backgroundColor = Color.argb(100, 112, 173, 71)
                    it.setOnClickListener {
                        createDialog(index, false)
                    }
                }
                
                val scheduleNameText = TextView(context)
                
                scheduleNameText.also {
                    it.layoutParams = RelativeLayout.LayoutParams(wrapContent, wrapContent).also {
                        it.addRule(RelativeLayout.CENTER_HORIZONTAL)
                        it.addRule(RelativeLayout.CENTER_VERTICAL)
                    }
                    it.text = scheduleInfo.schedule_name
                }
                
                schedule.addView(scheduleNameText)
                find<RelativeLayout>(R.id.refreshRelative).addView(schedule, 0)
            }
        }
    }
    
    @SuppressLint("InflateParams")
    private fun setCardView() {
        val calendar = Calendar.getInstance()
        val today = (calendar.get(Calendar.MONTH) + 1) * 100 +
                calendar.get(Calendar.DAY_OF_MONTH)
        
        find<LinearLayout>(R.id.taskLinear).removeAllViews()
        var taskCount = 0
        
        (GlobalValue.taskInfoArrayList).forEachWithIndex { index, taskInfo ->
            val diffTimeMin = Utils().getTime(taskInfo.due_date) / 100 * 60 +
                    Utils().getTime(taskInfo.due_date) % 100
            
            val diffDays = Utils().diffDayNum(today, Utils().getDate(taskInfo.due_date),
                    calendar.get(Calendar.YEAR))
            
            if (-1 < diffDays && diffDays < 8) {
                val inflater: LayoutInflater =
                        ctx.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val linearLayout: LinearLayout =
                        inflater.inflate(R.layout.task_card_view,
                                null) as LinearLayout
                
                linearLayout.also {
                    it.dateTextView.also {
                        it.text = diffDays.toString()
                        if (taskInfo.priority == 0 || (diffDays == 1 || diffDays == 0)) {
                            context?.let { ctx ->
                                it.textColor = ContextCompat.getColor(ctx,
                                        R.color.mostPriority)
                            }
                        }
                    }
                    
                    it.taskNameTextView.text = taskInfo.task_name
                    
                    it.cardView.also {
                        it.tag = Utils().getDate(taskInfo.due_date)
                        it.setOnClickListener {
                            createDialog(index, true)
                        }
                    }
                    find<RelativeLayout>(R.id.taskProgress).scaleY =
                            taskInfo.progress / 100f * dip(70)
                }
                
                find<LinearLayout>(R.id.taskLinear).addView(linearLayout, taskCount)
                
                val line = LinearLayout(context)
                val lParam = RelativeLayout.LayoutParams(0, 0)
                
                lParam.also {
                    it.width = 5
                    it.height = diffDays * dip(200) + (diffTimeMin *
                            0.13f).toInt() + dip(50)//dip(25)
                    it.leftMargin = dip(80 + 45 + 90 * taskCount)
                }
                
                line.also {
                    it.layoutParams = lParam
                    context?.let { ctx ->
                        it.backgroundColor =
                                ContextCompat.getColor(ctx, R.color.mostPriority)
                    }
                    it.setOnClickListener {
                        AlertDialog.Builder(activity).also {
                            it.setTitle(taskInfo.task_name)
                            it.setMessage("サブタスクを追加しますか？")
                            it.setPositiveButton("YES") { _, _ ->
                                startActivity<AdditionActivity>("sub" to true,
                                        "index" to index)
                            }
                            it.setNegativeButton("NO", null)
                            it.show()
                        }
                    }
                }
                
                find<RelativeLayout>(R.id.refreshRelative).addView(line)
                taskInfo.subTaskArrayList.forEach { element ->
                    val nowTime = Utils().getTime(element.time)
                    val nowMinute = nowTime / 100 * 60 + nowTime % 100
                    val subTaskSquare = RelativeLayout(context)
                    val rParam = RelativeLayout.LayoutParams(0, 0)
                    
                    rParam.also {
                        it.width = dip(15)
                        it.height = dip(15)
                        it.leftMargin = dip(80 + 45 + 90 * taskCount) - dip(7)
                        it.topMargin = Utils().diffDayNum(today, Utils().getDate(element.time),
                                calendar.get(Calendar.YEAR)) * dip(200) +
                                dip(0.13f * nowMinute)// + dip(50)// - dip(70)
                    }
                    
                    subTaskSquare.also {
                        it.layoutParams = rParam
                        it.backgroundColor = Color.RED
                        it.setOnClickListener {
                            longToast(element.sub_task_name)
                        }
                    }
                    find<RelativeLayout>(R.id.refreshRelative).addView(subTaskSquare)
                }
                taskCount++
            }
        }
    }
    
    private fun createDialog(index: Int, isTask: Boolean) {
        val listDialog = if (isTask) {
            arrayOf("開始", "変更", "完了", "削除", "サブタスクの追加", "進捗")
        } else {
            arrayOf("変更", "削除")
        }
        
        val title = if (isTask) {
            GlobalValue.taskInfoArrayList[index].task_name
        } else {
            GlobalValue.scheduleInfoArrayList[index].schedule_name
        }
        
        AlertDialog.Builder(context).also {
            it.setTitle(title)
            it.setItems(listDialog) { _, which ->
                when (which) {
                    0 -> {
                        if (isTask) {
                            startActivity<TimeSetActivity>("taskIndex" to index)
                        } else {
                            startActivity<AdditionActivity>("index" to index,
                                    "add" to false, "schedule" to true)
                        }
                    }
                    
                    1 -> {
                        if (isTask) {
                            startActivity<AdditionActivity>("index" to index,
                                    "add" to false, "task" to true)
                        } else {
                            AlertDialog.Builder(context).also {
                                it.setTitle(title)
                                it.setMessage(getString(R.string.deleteMassage))
                                it.setPositiveButton("OK") { _, _ ->
                                    deleteElement(isTask, index)
                                }
                                it.setNegativeButton("Cancel", null)
                                it.show()
                            }
                        }
                    }
                    
                    2 -> {
                        AlertDialog.Builder(context).also {
                            it.setTitle(title)
                            it.setMessage(getString(R.string.complicatedMassage))
                            it.setPositiveButton("Yes") { _, _ ->
                                deleteElement(isTask, index)
                            }
                            it.setNegativeButton("No", null)
                            it.show()
                        }
                    }
                    
                    3 -> {
                        AlertDialog.Builder(context).also {
                            it.setTitle(title)
                            it.setMessage(getString(R.string.deleteMassage))
                            it.setPositiveButton("OK") { _, _ ->
                                deleteElement(isTask, index)
                            }
                            it.setNegativeButton("Cancel", null)
                            it.show()
                        }
                    }
                    
                    4 -> {
                        startActivity<AdditionActivity>("sub" to true,
                                "index" to index)
                    }
                    
                    5 -> {
                        startActivity<InputProgressActivity>("index" to index)
                    }
                    
                    else -> {
                    }
                }
            }
            it.show()
        }
    }
    
    private fun deleteElement(isTask: Boolean, index: Int) = if (isTask) {
        DeleteTaskInfo().deleteTaskInfo(GlobalValue.taskInfoArrayList[index])
        GlobalValue.taskInfoArrayList.removeAt(index)
        Utils().saveTaskInfoList(ctx)
    } else {
        DeleteScheduleInfo().deleteScheduleInfo(GlobalValue.scheduleInfoArrayList[index])
        GlobalValue.scheduleInfoArrayList.removeAt(index)
        Utils().saveScheduleInfoList(ctx)
    }
}
