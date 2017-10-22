package jp.ict.muffin.otasukejuru.fragment

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.ui.CalendarFragmentUI
import kotlinx.android.synthetic.main.task_card_view.*
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.textColor
import java.util.*


class CalendarFragment2 : Fragment() {
    private var mTimer: Timer? = null
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            CalendarFragmentUI().createView(AnkoContext.create(ctx, this))
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setCardView()
    }
    
    override fun onResume() {
        super.onResume()
        val mHandler = Handler()
        mTimer = Timer()
        mTimer?.schedule(object : TimerTask(){
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
    private fun setCardView() {
        val calendar = Calendar.getInstance()
        val today = (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH)
        val showTaskNum = (GlobalValue.displayWidth - 50) / 90
    
        find<LinearLayout>(R.id.taskLinear).removeAllViews()
        GlobalValue.taskInfoArrayList.forEach {
            val diffDays = diffDayNum(today, it.limitDate, calendar.get(Calendar.YEAR))

            val inflater: LayoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val linearLayout: LinearLayout = inflater.inflate(R.layout.task_card_view, null) as LinearLayout
    
            linearLayout.apply {
                dateTextView?.apply {
                    text = diffDays.toString()
                    if (it.priority == 0) {
                        textColor = ContextCompat.getColor(context, R.color.mostPriority)
                    }
                }
                cardView?.apply {
                    tag = it.limitDate
                }
                taskNameTextView?.text = it.task_name
                find<LinearLayout>(R.id.taskLinear).addView(linearLayout, 0)
            }

        }
    }
    
    private fun diffDayNum(beforeDate: Int, afterDate: Int, year: Int): Int {
        
        val totalDays = if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        } else {
            intArrayOf(0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335)
        }
        
        Log.d("$beforeDate ", "$afterDate")
        val beforeDay: Int = beforeDate % 100
        val beforeMonth: Int = beforeDate / 100
        val afterDay: Int = afterDate % 100
        val afterMonth: Int = afterDate / 100
        return (totalDays[afterMonth] + afterDay - (totalDays[beforeMonth] + beforeDay))
    }
}
