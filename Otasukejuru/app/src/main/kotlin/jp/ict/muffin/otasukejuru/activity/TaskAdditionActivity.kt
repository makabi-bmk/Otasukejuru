package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.*
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.TaskInformation
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
        
        setActionBar(findViewById(R.id.toolbar_back) as Toolbar)
        
        val planButton = findViewById(R.id.button_plan) as Button
        planButton.setOnClickListener {
            isPlan = true
            inputPlanName()
        }
        
        val taskButton = findViewById(R.id.button_task) as Button
        taskButton.setOnClickListener {
            isPlan = false
            inputTaskName()
        }
        
        val backButton = findViewById(R.id.button_back) as ImageButton
        backButton.setOnClickListener { finish() }
    }
    
    private fun inputPlanName() {
        setContentView(R.layout.input_plan_name)
        setActionBar(findViewById(R.id.toolbar_back) as Toolbar)
        
        val inputNameEdit = findViewById(R.id.plan_name) as EditText
        
        val nextButton = findViewById(R.id.button_next) as Button
        nextButton.setOnClickListener {
            taskTitleName = inputNameEdit.text.toString()
            if (taskTitleName == "") taskTitleName = "無題"
            
            startPlanTime()
        }
        
        val backButton = findViewById(R.id.button_back) as ImageButton
        backButton.setOnClickListener { selectAddType() }
        
    }
    
    //TODO:CHANGE XML
    private fun startPlanTime() {
        setContentView(R.layout.start_plan_time)
        setActionBar(findViewById(R.id.toolbar_back) as Toolbar)
        
        startMonth = calendar.get(Calendar.MONTH)
        startDay = calendar.get(Calendar.DAY_OF_MONTH)
        startHour = calendar.get(Calendar.HOUR_OF_DAY)
        startMinute = calendar.get(Calendar.MINUTE)
        
        val startMonthNumPick = findViewById(R.id.start_month_num_pick) as NumberPicker
        startMonthNumPick.maxValue = 12
        startMonthNumPick.minValue = 1
        startMonthNumPick.value = startMonth
        startMonthNumPick.setOnValueChangedListener { _, _, newVal -> startMonth = newVal }
        
        val startDayNumPick = findViewById(R.id.start_day_num_pick) as NumberPicker
        startDayNumPick.maxValue = 31
        startDayNumPick.minValue = 1
        startDayNumPick.value = startDay
        startDayNumPick.setOnValueChangedListener { _, _, newVal -> startDay = newVal }
        
        val startHourNumPick = findViewById(R.id.start_hour_num_pick) as NumberPicker
        startHourNumPick.maxValue = 23
        startHourNumPick.minValue = 0
        startHourNumPick.value = startHour
        startHourNumPick.setOnValueChangedListener { _, _, newVal -> startHour = newVal }
        
        val startMinuteNumPick = findViewById(R.id.start_minute_num_pick) as NumberPicker
        startMinuteNumPick.maxValue = 59
        startMinuteNumPick.minValue = 0
        startMinuteNumPick.value = startMinute
        startMinuteNumPick.setOnValueChangedListener { _, _, newVal -> startMinute = newVal }
        
        val next = findViewById(R.id.button_next) as Button
        next.setOnClickListener { finishPlanTime() }
        
        val backButton = findViewById(R.id.button_back) as ImageButton
        backButton.setOnClickListener { inputPlanName() }
        
    }
    
    private fun finishPlanTime() {
        setContentView(R.layout.finish_plan_time)
        setActionBar(findViewById(R.id.toolbar_back) as Toolbar)
        
        finishMonth = startMonth
        finishDay = startDay
        finishHour = startHour
        finishMinute = startMinute
        
        val finishMonthNumPick = findViewById(R.id.finish_month_num_pick) as NumberPicker
        finishMonthNumPick.maxValue = 12
        finishMonthNumPick.minValue = 1
        finishMonthNumPick.value = finishMonth
        finishMonthNumPick.setOnValueChangedListener { _, _, newVal -> finishMonth = newVal }
        
        val finishDayNumPick = findViewById(R.id.finish_day_num_pick) as NumberPicker
        finishDayNumPick.maxValue = 31
        finishDayNumPick.minValue = 1
        finishDayNumPick.value = finishDay
        finishDayNumPick.setOnValueChangedListener { _, _, newVal -> finishDay = newVal }
        
        val finishHourNumPick = findViewById(R.id.finish_hour_num_pick) as NumberPicker
        finishHourNumPick.maxValue = 23
        finishHourNumPick.minValue = 0
        finishHourNumPick.value = finishHour
        finishHourNumPick.setOnValueChangedListener { _, _, newVal -> finishHour = newVal }
        
        val finishMinuteNumPick = findViewById(R.id.finish_minute_num_pick) as NumberPicker
        finishMinuteNumPick.maxValue = 59
        finishMinuteNumPick.minValue = 0
        finishMinuteNumPick.value = finishMinute
        finishMinuteNumPick.setOnValueChangedListener { _, _, newVal -> finishMinute = newVal }
        
        val next = findViewById(R.id.button_next) as Button
        next.setOnClickListener { setPlanRepeat() }
        
        val backButton = findViewById(R.id.button_back) as ImageButton
        backButton.setOnClickListener { startPlanTime() }
        
    }
    
    private fun setPlanRepeat() {
        setContentView(R.layout.set_plan_repeat)
        setActionBar(findViewById(R.id.toolbar_back) as Toolbar)
        
        val planRepeatRadioGroup = findViewById(R.id.plan_repeat_radio_group) as RadioGroup
        
        val next = findViewById(R.id.button_next) as Button
        next.setOnClickListener {
            val num = planRepeatRadioGroup.checkedRadioButtonId
            
            taskRepeat = if (num != -1) {
                (findViewById(num) as RadioButton).text.toString()
            } else {
                "選択されていない"
            }
            setPlanMessageTime()
        }
        
        val backButton = findViewById(R.id.button_back) as ImageButton
        backButton.setOnClickListener { finishPlanTime() }
        
    }
    
    private fun setPlanMessageTime() {
        setContentView(R.layout.set_plan_message_time)
        setActionBar(findViewById(R.id.toolbar_back) as Toolbar)
        
        val setMessageTimeEdit = findViewById(R.id.set_message_time_edit) as EditText
        setMessageTimeEdit.setText("5")
        messageTime = 5
        
        val next = findViewById(R.id.button_next) as Button
        next.setOnClickListener {
            val str: String = setMessageTimeEdit.text.toString()
            messageTime = Integer.parseInt(str)
            
            Log.d("plan", "タイトル名:" + taskTitleName + "\n予定開始の日付:" + startMonth + "月" +
                    startDay + "日" + startHour + "時" + startMinute + "分" + "\n予定終了の時間:" +
                    finishMonth + "月" + finishMonth + "似り" + finishHour + "時" +
                    finishMinute + "分" + "\n繰り返し:" + taskRepeat + "\n何分前に通知するか:" +
                    messageTime)
            
            finish()
        }
        
        val backButton = findViewById(R.id.button_back) as ImageButton
        backButton.setOnClickListener { setPlanRepeat() }
        
    }
    
    private fun inputTaskName() {
        setContentView(R.layout.input_task_name)
        setActionBar(findViewById(R.id.toolbar_back) as Toolbar)
        
        val inputTaskNameEdit = findViewById(R.id.input_task_name_edit) as EditText
        
        val next = findViewById(R.id.button_next) as Button
        next.setOnClickListener {
            taskTitleName = inputTaskNameEdit.text.toString()
            if (taskTitleName == "") taskTitleName = "無題"
            
            finishTaskTime()
        }
        
        val backButton = findViewById(R.id.button_back) as ImageButton
        backButton.setOnClickListener { selectAddType() }
    }
    
    private fun finishTaskTime() {
        setContentView(R.layout.finish_task_time)
        setActionBar(findViewById(R.id.toolbar_back) as Toolbar)
        
        finishMonth = calendar.get(Calendar.MONTH)
        finishDay = calendar.get(Calendar.DAY_OF_MONTH)
        finishHour = calendar.get(Calendar.HOUR_OF_DAY)
        finishMinute = calendar.get(Calendar.MINUTE)
        
        val finishMonthNumPick = findViewById(R.id.finish_month_num_pick) as NumberPicker
        finishMonthNumPick.maxValue = 12
        finishMonthNumPick.minValue = 1
        finishMonthNumPick.value = finishMonth
        finishMonthNumPick.setOnValueChangedListener { _, _, newVal -> finishMonth = newVal }
        
        val finishDayNumPick = findViewById(R.id.finish_day_num_pick) as NumberPicker
        finishDayNumPick.maxValue = 31
        finishDayNumPick.minValue = 1
        finishDayNumPick.value = finishDay
        finishDayNumPick.setOnValueChangedListener { _, _, newVal -> finishDay = newVal }
        
        val finishHourNumPick = findViewById(R.id.finish_hour_num_pick) as NumberPicker
        finishHourNumPick.maxValue = 23
        finishHourNumPick.minValue = 0
        finishHourNumPick.value = finishHour
        finishHourNumPick.setOnValueChangedListener { _, _, newVal -> finishHour = newVal }
        
        val finishMinuteNumPick = findViewById(R.id.finish_minute_num_pick) as NumberPicker
        finishMinuteNumPick.maxValue = 59
        finishMinuteNumPick.minValue = 0
        finishMinuteNumPick.value = finishMinute
        finishMinuteNumPick.setOnValueChangedListener { _, _, newVal -> finishMinute = newVal }
        
        val next = findViewById(R.id.button_next) as Button
        next.setOnClickListener { setTaskRepeat() }
        
        val noLimit = findViewById(R.id.no_limit) as Button
        noLimit.setOnClickListener {
            startMonth = -1
            setTaskRepeat()
        }
        
        val backButton = findViewById(R.id.button_back) as ImageButton
        backButton.setOnClickListener { inputTaskName() }
        
    }
    
    private fun setTaskRepeat() {
        setContentView(R.layout.set_task_repeat)
        setActionBar(findViewById(R.id.toolbar_back) as Toolbar)
        
        val planRepeatRadioGroup = findViewById(R.id.plan_repeat_radio_group) as RadioGroup
        
        val next = findViewById(R.id.button_next) as Button
        next.setOnClickListener {
            val num = planRepeatRadioGroup.checkedRadioButtonId
            
            taskRepeat = if (num != -1) {
                (findViewById(num) as RadioButton).text.toString()
            } else {
                "選択されてない"
            }
            setMust()
        }
        
        val backButton = findViewById(R.id.button_back) as ImageButton
        backButton.setOnClickListener { finishTaskTime() }
        
    }
    
    private fun setMust() {
        setContentView(R.layout.set_must)
        setActionBar(findViewById(R.id.toolbar_back) as Toolbar)
        
        val noMust = findViewById(R.id.no_must) as Button
        noMust.setOnClickListener {
            isMust = false
            setShould()
        }
        
        val yesMust = findViewById(R.id.yes_must) as Button
        yesMust.setOnClickListener {
            isMust = true
            setShould()
        }
        
        val backButton = findViewById(R.id.button_back) as ImageButton
        backButton.setOnClickListener { setTaskRepeat() }
    }
    
    private fun setShould() {
        setContentView(R.layout.set_should)
        setActionBar(findViewById(R.id.toolbar_back) as Toolbar)
        
        val noShould = findViewById(R.id.no_should) as Button
        noShould.setOnClickListener {
            isShould = false
            setWantTo()
        }
        
        val yesShould = findViewById(R.id.yes_should) as Button
        yesShould.setOnClickListener {
            isShould = true
            setWantTo()
        }
        
        val backButton = findViewById(R.id.button_back) as ImageButton
        backButton.setOnClickListener { setMust() }
        
    }
    
    private fun setWantTo() {
        setContentView(R.layout.set_want)
        setActionBar(findViewById(R.id.toolbar_back) as Toolbar)
        
        val noWantTo = findViewById(R.id.no_want_to) as Button
        noWantTo.setOnClickListener {
            isWant = false
            setTaskMessageTime()
        }
        
        val yes = findViewById(R.id.yes_w) as Button
        yes.setOnClickListener {
            isWant = true
            setTaskMessageTime()
        }
        
        val imageButton = findViewById(R.id.button_back) as ImageButton
        imageButton.setOnClickListener { setShould() }
        
    }
    
    private fun setTaskMessageTime() {
        setContentView(R.layout.set_task_message_time)
        setActionBar(findViewById(R.id.toolbar_back) as Toolbar)
        
        val finishHourEdit = findViewById(R.id.finish_hour_num_pick) as EditText
        finishHourEdit.setText("0")
        finishHour = 0
        
        val finishMinutesEdit = findViewById(R.id.finish_minute_num_pick) as EditText
        finishMinutesEdit.setText("5")
        finishMinute = 5
        
        val next = findViewById(R.id.button_next) as Button
        next.setOnClickListener {
            finishHour = Integer.parseInt(finishHourEdit.text.toString())
            finishMinute = Integer.parseInt(finishMinutesEdit.text.toString())
            
            if (startMonth == -1) {
                dateLimit = -1
            } else {
                dateLimit = startMonth * 100 + startDay
                timeLimit = this@TaskAdditionActivity.startHour * 100 + startDay
            }
            
            Log.d("task", "タイトル名:" + taskTitleName + "\n期限の開始:" + dateLimit +
                    "\n繰り返し:" + taskRepeat +
                    "\nisMust:" + isMust + "\nisShould:" + isShould + "\nisWant to:" +
                    isWant + "\n終了目安:" + finishHour + "時間" + finishMinute + "分")
            
            setTaskInformation()
            
            finish()
        }
        
        val backButton = findViewById(R.id.button_back) as ImageButton
        backButton.setOnClickListener { setWantTo() }
    }
    
    private fun setTaskInformation() {
        val taskInformation = TaskInformation()
        taskInformation.name = taskTitleName
        taskInformation.limitDate = dateLimit
        taskInformation.limitTime = timeLimit
        taskInformation.repeat = taskRepeat
        taskInformation.must = isMust
        taskInformation.should = isShould
        taskInformation.want = isWant
        taskInformation.finishTimeMinutes = finishHour * 100 + finishMinute
        taskInformation.priority = 0
        
        GlobalValue.taskInformationArrayList.add(0, taskInformation)
        
    }
}