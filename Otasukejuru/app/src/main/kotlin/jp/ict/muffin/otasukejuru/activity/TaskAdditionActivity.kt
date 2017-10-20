package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.*
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.TaskInfo
import kotlinx.android.synthetic.main.set_plan_notification_time.*
import kotlinx.android.synthetic.main.set_plan_repeat.*
import kotlinx.android.synthetic.main.set_task_repeat.*
import org.jetbrains.anko.find
import java.util.*


class TaskAdditionActivity : Activity() {
    
    //common
    private var isPlan: Boolean = false
    private var taskTitleName: String = ""
    private var startMonth: Int = 0
    private var startDay: Int = 0
    private var startHour: Int = 0
    private var startMinute: Int = 0
    private var finishMonth: Int = 0
    private var finishDay: Int = 0
    private var finishHour: Int = 0
    private var finishMinute: Int = 0
    private var taskRepeat: String = ""
    private var dateLimit: Int = 0
    private var timeLimit: Int = 0
    
    //plan
    private var messageTime: Int = 0
    //task
    private var isMust: Boolean = false
    private var isShould: Boolean = false
    private var isWant: Boolean = false
    
    private var calendar = Calendar.getInstance()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectAddType()
    }
    
    private fun selectAddType() {
        setContentView(R.layout.selection)
        
        setActionBar(find(R.id.toolbar_back))
        
        find<Button>(R.id.button_plan).setOnClickListener {
            isPlan = true
            inputPlanName()
        }
        
        find<Button>(R.id.button_task).setOnClickListener {
            isPlan = false
            inputTaskName()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener { finish() }
    }
    
    private fun inputPlanName() {
        setContentView(R.layout.input_plan_name)
        setActionBar(find(R.id.toolbar_back))
        
        val inputNameEdit = find<EditText>(R.id.plan_name)
        
        find<Button>(R.id.button_next).setOnClickListener {
            taskTitleName = inputNameEdit.text.toString()
            if (taskTitleName == "") taskTitleName = "無題"
            
            startPlanTime()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener { selectAddType() }
        
    }
    
    //TODO:CHANGE XML
    private fun startPlanTime() {
        setContentView(R.layout.start_plan_time)
        setActionBar(find(R.id.toolbar_back))
        
        startMonth = calendar.get(Calendar.MONTH) + 1
        startDay = calendar.get(Calendar.DAY_OF_MONTH)
        startHour = calendar.get(Calendar.HOUR_OF_DAY)
        startMinute = calendar.get(Calendar.MINUTE)
        
        find<NumberPicker>(R.id.start_month_num_pick).apply {
            maxValue = 12
            minValue = 1
            value = startMonth
            setOnValueChangedListener { _, _, newVal -> startMonth = newVal }
        }
        
        find<NumberPicker>(R.id.start_day_num_pick).apply {
            maxValue = 31
            minValue = 1
            value = startDay
            setOnValueChangedListener { _, _, newVal -> startDay = newVal }
        }
        
        find<NumberPicker>(R.id.start_hour_num_pick).apply {
            maxValue = 23
            minValue = 0
            value = startHour
            setOnValueChangedListener { _, _, newVal -> startHour = newVal }
        }
        
        
        find<NumberPicker>(R.id.start_minute_num_pick).apply {
            maxValue = 59
            minValue = 0
            value = startMinute
            setOnValueChangedListener { _, _, newVal -> startMinute = newVal }
        }
        
        find<Button>(R.id.button_next).setOnClickListener { finishPlanTime() }
        find<ImageButton>(R.id.button_back).setOnClickListener { inputPlanName() }
        
    }
    
    private fun finishPlanTime() {
        setContentView(R.layout.finish_plan_time)
        setActionBar(find(R.id.toolbar_back))
        
        finishMonth = startMonth
        finishDay = startDay
        finishHour = startHour
        finishMinute = startMinute
        
        find<NumberPicker>(R.id.finish_month_num_pick).apply {
            maxValue = 12
            minValue = 1
            value = finishMonth
            setOnValueChangedListener { _, _, newVal -> finishMonth = newVal }
        }
        find<NumberPicker>(R.id.finish_day_num_pick).apply {
            maxValue = 31
            minValue = 1
            value = finishDay
            setOnValueChangedListener { _, _, newVal -> finishDay = newVal }
        }
        find<NumberPicker>(R.id.finish_hour_edit).apply {
            maxValue = 23
            minValue = 0
            value = finishHour
            setOnValueChangedListener { _, _, newVal -> finishHour = newVal }
        }
        find<NumberPicker>(R.id.finish_minute_edit).apply {
            maxValue = 59
            minValue = 0
            value = finishMinute
            setOnValueChangedListener { _, _, newVal -> finishMinute = newVal }
        }
        find<Button>(R.id.button_next).setOnClickListener { setPlanRepeat() }
        find<ImageButton>(R.id.button_back).setOnClickListener { startPlanTime() }
        
    }
    
    private fun setPlanRepeat() {
        setContentView(R.layout.set_plan_repeat)
        setActionBar(find(R.id.toolbar_back))
        
        
        find<Button>(R.id.button_next).setOnClickListener {
            val num = plan_repeat_radio_group.checkedRadioButtonId
            
            taskRepeat = if (num != -1) {
                (findViewById(num) as RadioButton).text.toString()
            } else {
                "選択されていない"
            }
            setPlanNotificationTime()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener { finishPlanTime() }
        
    }
    
    private fun setPlanNotificationTime() {
        setContentView(R.layout.set_plan_notification_time)
        setActionBar(find(R.id.toolbar_back))
        
        set_notification_time_edit.setText("5")
        messageTime = 5
        
        find<Button>(R.id.button_finish).setOnClickListener {
            val str: String = set_notification_time_edit.text.toString()
            messageTime = Integer.parseInt(str)
            
            Log.d("plan", "タイトル名:" + taskTitleName + "\n予定開始の日付:" + startMonth + "月" +
                    startDay + "日" + startHour + "時" + startMinute + "分" + "\n予定終了の時間:" +
                    finishMonth + "月" + finishMonth + "似り" + finishHour + "時" +
                    finishMinute + "分" + "\n繰り返し:" + taskRepeat + "\n何分前に通知するか:" +
                    messageTime)
            
            finish()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener { setPlanRepeat() }
        
    }
    
    private fun inputTaskName() {
        setContentView(R.layout.input_task_name)
        setActionBar(find(R.id.toolbar_back))
        
        val inputTaskNameEdit = findViewById(R.id.input_task_name_edit) as EditText
        
        find<Button>(R.id.button_next).setOnClickListener {
            taskTitleName = inputTaskNameEdit.text.toString()
            if (taskTitleName == "") taskTitleName = "無題"
            
            finishTaskTime()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener { selectAddType() }
    }
    
    private fun finishTaskTime() {
        setContentView(R.layout.finish_task_time)
        setActionBar(find(R.id.toolbar_back))
        
        finishMonth = calendar.get(Calendar.MONTH) + 1
        finishDay = calendar.get(Calendar.DAY_OF_MONTH)
        finishHour = calendar.get(Calendar.HOUR_OF_DAY)
        finishMinute = calendar.get(Calendar.MINUTE)
        
        find<NumberPicker>(R.id.finish_month_num_pick).apply {
            maxValue = 12
            minValue = 1
            value = finishMonth
            setOnValueChangedListener { _, _, newVal -> finishMonth = newVal }
        }
        
        find<NumberPicker>(R.id.finish_day_num_pick).apply {
            maxValue = 31
            minValue = 1
            value = finishDay
            setOnValueChangedListener { _, _, newVal -> finishDay = newVal }
        }
        
        find<NumberPicker>(R.id.finish_hour_edit).apply {
            maxValue = 23
            minValue = 0
            value = finishHour
            setOnValueChangedListener { _, _, newVal -> finishHour = newVal }
        }
        
        find<NumberPicker>(R.id.finish_minute_edit).apply {
            maxValue = 59
            minValue = 0
            value = finishMinute
            setOnValueChangedListener { _, _, newVal -> finishMinute = newVal }
        }
        
        find<Button>(R.id.button_next).setOnClickListener { setTaskRepeat() }
        
        val noLimit = find<Button>(R.id.no_limit)
        noLimit.setOnClickListener {
            startMonth = -1
            setTaskRepeat()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener { inputTaskName() }
        
    }
    
    private fun setTaskRepeat() {
        setContentView(R.layout.set_task_repeat)
        setActionBar(findViewById(R.id.toolbar_back) as Toolbar)
        
        
        find<Button>(R.id.button_next).setOnClickListener {
            val num = task_repeat_radio_group.checkedRadioButtonId
            taskRepeat = if (num != -1) {
                (findViewById(num) as RadioButton).text.toString()
            } else {
                "選択されてない"
            }
            setMust()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener { finishTaskTime() }
        
    }
    
    private fun setMust() {
        setContentView(R.layout.set_must)
        setActionBar(findViewById(R.id.toolbar_back) as Toolbar)
        
        find<Button>(R.id.no_must).setOnClickListener {
            isMust = false
            setShould()
        }
        
        find<Button>(R.id.yes_must).setOnClickListener {
            isMust = true
            setShould()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener { setTaskRepeat() }
    }
    
    private fun setShould() {
        setContentView(R.layout.set_should)
        setActionBar(find(R.id.toolbar_back))
        
        find<Button>(R.id.no_should).setOnClickListener {
            isShould = false
            setWantTo()
        }
        
        find<Button>(R.id.yes_should).setOnClickListener {
            isShould = true
            setWantTo()
        }
        
        find<ImageView>(R.id.button_back).setOnClickListener { setMust() }
        
    }
    
    private fun setWantTo() {
        setContentView(R.layout.set_want)
        setActionBar(find(R.id.toolbar_back))
        
        find<Button>(R.id.no_want).setOnClickListener {
            isWant = false
            setTaskNotificationTime()
        }
        
        find<Button>(R.id.yes_want).setOnClickListener {
            isWant = true
            setTaskNotificationTime()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener { setShould() }
        
    }
    
    private fun setTaskNotificationTime() {
        setContentView(R.layout.set_task_notification_time)
        setActionBar(find(R.id.toolbar_back))
        
        val finishHourEdit = find<EditText>(R.id.finish_hour_edit)
        finishHourEdit.setText("0")
        finishHour = 0
        
        val finishMinuteEdit = find<EditText>(R.id.finish_minute_edit)
        finishMinuteEdit.setText("5")
        finishMinute = 5
        
        find<Button>(R.id.button_next).setOnClickListener {
            finishHour = Integer.parseInt(finishHourEdit.text.toString())
            finishMinute = Integer.parseInt(finishMinuteEdit.text.toString())
            
            if (startMonth == -1) {
                dateLimit = -1
            } else {
                dateLimit = (finishMonth - startMonth) * 100 + finishDay - startDay
                timeLimit = this@TaskAdditionActivity.startHour * 100 + startDay
            }
            
            Log.d("task", "タイトル名:" + taskTitleName + "\n期限の開始:" + dateLimit +
                    "\n繰り返し:" + taskRepeat +
                    "\nisMust:" + isMust + "\nisShould:" + isShould + "\nisWant to:" +
                    isWant + "\n終了目安:" + finishHour + "時間" + finishMinute + "分")
            
            setTaskInformation()
            
            finish()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener { setWantTo() }
    }
    
    private fun setTaskInformation() {
        val taskInformation = TaskInfo()
        taskInformation.apply {
            name = taskTitleName
            limitDate = dateLimit
            limitTime = timeLimit
            repeat = taskRepeat
            must = isMust
            should = isShould
            want = isWant
            finishTimeMinutes = finishHour * 100 + finishMinute
            priority = 0
        }
        GlobalValue.taskInformationArrayList.add(0, taskInformation)
        
    }
}