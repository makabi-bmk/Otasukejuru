package jp.ict.muffin.otasukejuru.fragment

import android.annotation.SuppressLint
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
import android.widget.RelativeLayout
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.TaskInfo
import jp.ict.muffin.otasukejuru.activity.AdditionActivity
import jp.ict.muffin.otasukejuru.activity.InputProgressActivity
import jp.ict.muffin.otasukejuru.activity.TimeSetActivity
import jp.ict.muffin.otasukejuru.other.Utils
import jp.ict.muffin.otasukejuru.ui.TaskListFragmentUI
import kotlinx.android.synthetic.main.fragment_list_todo.*
import kotlinx.android.synthetic.main.task_card_view.view.*
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.textColor
import java.util.*


class TaskListFragment : Fragment() {
    
    private var mTimer: Timer? = null
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            TaskListFragmentUI().createView(AnkoContext.create(ctx, this))
    
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
    
    @SuppressLint("InflateParams")
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
        
        var mostPriorityNum = 0
        var highPriorityNum = 0
        var middlePriorityNum = 0
        var lowPriorityNum = 0
        val calendar = Calendar.getInstance()
        val today = (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH)
        
        val showTaskNum = GlobalValue.displayWidth / 90 - 1
        GlobalValue.taskInfoArrayList.forEachWithIndex { index, element ->
            val diffDays = Utils().diffDayNum(today, Utils().getDate(element.due_date),
                    calendar.get(Calendar.YEAR))
            
            val inflater: LayoutInflater =
                    ctx.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val linearLayout: LinearLayout =
                    inflater.inflate(R.layout.task_card_view, null) as LinearLayout
            
            linearLayout.apply {
                dateTextView.apply {
                    text = diffDays.toString()
                    if (element.priority == 0 || (diffDays == 1 || diffDays == 0)) {
                        textColor = ContextCompat.getColor(context, R.color.mostPriority)
                    }
                }
                taskNameTextView.text = element.task_name
                cardView.apply {
                    tag = Utils().getDate(element.due_date)
                    setOnClickListener {
                        createDialog(element, index)
                    }
                }
                find<RelativeLayout>(R.id.taskProgress).scaleY = element.progress * 1.2f
                
            }
            
            val position: Int
            when (element.priority) {
                0 -> {
                    position = mostPriorityNum++ % showTaskNum
                    mostPriorityCardLinear
                }
                
                1 -> {
                    position = highPriorityNum++ % showTaskNum
                    if (highPriorityNum <= showTaskNum) {
                        highPriorityCardLinear1
                    } else {
                        highPriorityCardLinear2
                    }
                }
                
                2 -> {
                    position = middlePriorityNum++ % showTaskNum
                    if (middlePriorityNum <= showTaskNum) {
                        middlePriorityCardLinear1
                    } else {
                        middlePriorityCardLinear2
                    }
                }
                
                else -> {
                    position = lowPriorityNum++ % showTaskNum
                    if (lowPriorityNum <= showTaskNum) {
                        lowPriorityCardLinear1
                    } else {
                        lowPriorityCardLinear2
                    }
                }
            }.addView(linearLayout, position)
        }
    }
    
    private fun createDialog(element: TaskInfo, index: Int) {
        val listDialog = arrayOf("開始", "変更", "完了", "削除", "進捗")
        
        AlertDialog.Builder(context).apply {
            setTitle(element.task_name)
            setItems(listDialog) { _, which ->
                when (which) {
                    0 -> {
                        startActivity<TimeSetActivity>("taskIndex" to index)
                    }
                    
                    1 -> {
                        startActivity<AdditionActivity>("add" to false,
                                "index" to index, "task" to true)
                    }
                    
                    2 -> {
                        AlertDialog.Builder(context).apply {
                            setTitle(element.task_name)
                            setMessage(getString(R.string.complicatedMassage))
                            setPositiveButton("Yes") { _, _ ->
                                deleteTask(element)
                            }
                            setNegativeButton("No", null)
                            show()
                        }
                    }
                    
                    3 -> {
                        AlertDialog.Builder(context).apply {
                            setTitle(element.task_name)
                            setMessage(getString(R.string.deleteMassage))
                            setPositiveButton("OK") { _, _ ->
                                deleteTask(element)
                            }
                            setNegativeButton("Cancel", null)
                            show()
                        }
                    }
                    
                    4 -> {
                        startActivity<InputProgressActivity>("index" to index)
                    }
                    
                    else -> {
                    
                    }
                }
            }
            show()
        }
    }
    
    private fun deleteTask(element: TaskInfo) {
        try {
            GlobalValue.taskInfoArrayList.remove(element)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Utils().saveString(ctx, getString(R.string.TaskInfoKey),
                GlobalValue.taskInfoArrayList.toString())
    }
}