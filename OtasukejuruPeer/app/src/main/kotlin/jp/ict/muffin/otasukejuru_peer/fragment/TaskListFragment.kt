package jp.ict.muffin.otasukejuru_peer.fragment

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
import jp.ict.muffin.otasukejuru_peer.R
import jp.ict.muffin.otasukejuru_peer.`object`.GlobalValue
import jp.ict.muffin.otasukejuru_peer.activity.AdditionActivity
import jp.ict.muffin.otasukejuru_peer.other.Utils
import jp.ict.muffin.otasukejuru_peer.ui.TaskListFragmentUI
import kotlinx.android.synthetic.main.fragment_list_todo.*
import kotlinx.android.synthetic.main.task_card_view.view.*
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.dip
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
        
        var mostPriority = 0
        var highPriorityNum = 0
        var middlePriorityNum = 0
        var lowPriorityNum = 0
        val calendar = Calendar.getInstance()
        val today = (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH)
        
        val showTaskNum = GlobalValue.displayWidth / 90 - 1
        GlobalValue.taskInfoArrayList.forEachWithIndex { index, element ->
            val diffDays = Utils().diffDayNum(today, Utils().getDate(element.due_date),
                    calendar.get(Calendar.YEAR))
            
            val inflater: LayoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val linearLayout: LinearLayout = inflater.inflate(R.layout.task_card_view, null) as LinearLayout
            
            linearLayout.apply {
                dateTextView.apply {
                    text = diffDays.toString()
                    if (element.priority == 0 && (diffDays == 1 || diffDays == 0)) {
                        textColor = ContextCompat.getColor(context, R.color.mostPriority)
                    }
                }
                taskNameTextView.text = element.task_name
                cardView.apply {
                    tag = Utils().getDate(element.due_date)
                    setOnClickListener {
                        AlertDialog.Builder(context).apply {
                            setTitle(element.task_name)
                            setMessage("変更しますか？")
                            setPositiveButton("Yes") { _, _ ->
                                //                                 OK button pressed
                                startActivity<AdditionActivity>("add" to false,
                                        "index" to index, "task" to true)
                            }
                            setNegativeButton("Cancel", null)
                            show()
                        }
                    }
                }
                find<RelativeLayout>(R.id.taskProgress).scaleY = dip(element.progress * 1.4f).toFloat()
                
            }
            
            val position: Int
            when (element.priority) {
                0 -> {
                    position = mostPriority++ % showTaskNum
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
}