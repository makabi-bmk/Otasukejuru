package jp.ict.muffin.otasukejuru.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.AsyncTask
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
import com.squareup.moshi.Moshi
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.TaskInfo
import jp.ict.muffin.otasukejuru.activity.InputProgressActivity
import jp.ict.muffin.otasukejuru.activity.TaskAdditionActivity
import jp.ict.muffin.otasukejuru.other.Utils
import jp.ict.muffin.otasukejuru.ui.ToDoListFragmentUI
import kotlinx.android.synthetic.main.fragment_list_todo.*
import kotlinx.android.synthetic.main.task_card_view.view.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
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
            ToDoListFragmentUI().createView(AnkoContext.create(ctx, this))
    
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
                        createDialog(taskNameTextView.text.toString(), element, index)
                    }
                }
                find<RelativeLayout>(R.id.taskProgress).scaleY = dip(element.progress * 0.7f).toFloat()
//                val rParam = RelativeLayout.LayoutParams(0, 0)
//                rParam.height = (element.progress * 0.7).toInt()
//                taskProgress.layoutParams = rParam
//                Log.d("height", taskProgress.height.toString())
//                Log.d("height", (element.progress).toString())
                
            }
            
            val position: Int
            when (element.priority) {
//            when (Utils().diffDayNum(today, Utils().getDate(element.due_date), 2017)) {
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
    
    private fun createDialog(tag: String, element: TaskInfo, index: Int) {
        val listDialog = arrayOf("開始", "変更", "完了", "削除", "進捗")
        
        AlertDialog.Builder(context)
                .setTitle(tag)
                .setItems(listDialog) { _, which ->
                    when (which) {
                        0 -> {
//                            startActivity<TimeSetActivity>()
                            AlertDialog.Builder(context)
                                    .setTitle(element.task_name)
                                    .setMessage(getString(R.string.attentionMassage))
                                    .setPositiveButton("OK") { dialog, which ->
                                        // OK button pressed
                                    }
                                    .setNegativeButton("Cancel", null)
                                    .show()
                        }
                        
                        1 -> {
                            startActivity<TaskAdditionActivity>("init" to true)
                        }
                        
                        4 -> {
                            startActivity<InputProgressActivity>("index" to index)
                        }
                        
                        else -> {
                            deleteTask(element, index)
                        }
                        
                    }
                }
                .show()
    }

//    private fun deleteTask(element: TaskInfo, index: Int) {
//        deleteTaskPost(index = index)
//        setCardView()
//    }
    
    private val mediaType = MediaType.parse("application/json; charset=utf-8")
    private val client = OkHttpClient()
    
    private fun deleteTask(element: TaskInfo, index: Int) {
        val task = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg params: Unit?) {
                post("${GlobalValue.SERVER_URL}/delete/task",
                        convertToJson(GlobalValue.taskInfoArrayList[index]))
                GlobalValue.taskInfoArrayList.remove(element)
            }
            
            
        }
        
        task.execute()
    }
    
    private fun post(url: String, json: String): String? {
        try {
            val body = RequestBody.create(mediaType, json)
            val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .build()
            val response = client.newCall(request).execute()
            return response.body()?.string()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    
    private fun convertToJson(taskInfo: TaskInfo): String {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(TaskInfo::class.java)
        
        Log.d("postTask", adapter.toJson(taskInfo))
        
        return adapter.toJson(taskInfo)
    }
    
}