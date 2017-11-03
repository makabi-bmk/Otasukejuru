package jp.ict.muffin.otasukejuru_peer.activity

import android.app.Activity
import android.os.Bundle
import android.widget.*
import jp.ict.muffin.otasukejuru_peer.R
import jp.ict.muffin.otasukejuru_peer.`object`.EveryInfo
import jp.ict.muffin.otasukejuru_peer.`object`.GlobalValue
import jp.ict.muffin.otasukejuru_peer.`object`.ScheduleInfo
import jp.ict.muffin.otasukejuru_peer.`object`.TaskInfo
import jp.ict.muffin.otasukejuru_peer.communication.UpdateEveryInfoAsync
import jp.ict.muffin.otasukejuru_peer.communication.UpdateScheduleInfoAsync
import jp.ict.muffin.otasukejuru_peer.communication.UpdateTaskInfoAsync
import jp.ict.muffin.otasukejuru_peer.other.Utils
import jp.ict.muffin.otasukejuru_peer.ui.ChangePriorityActivityUI
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.ctx
import org.jetbrains.anko.find
import java.util.*


class AdditionActivity : Activity() {
    
    //common
    private var isSchedule: Boolean = false
    private var titleName: String = ""
    private var startYear: Int = 0
    private var startMonth: Int = 0
    private var startDay: Int = 0
    private var startHour: Int = 0
    private var startMinute: Int = 0
    private var finishYear: Int = 0
    private var finishMonth: Int = 0
    private var finishDay: Int = 0
    private var finishHour: Int = 0
    private var finishMinute: Int = 0
    private var dateLimit: Int = 0
    private var timeLimit: Int = 0
    
    private var calendar = Calendar.getInstance()
    
    private var isAdd: Boolean = true
    private var isTask: Boolean = false
    private var index: Int = -1
    private lateinit var beforeTaskInfo: TaskInfo
    private lateinit var beforeScheduleInfo: ScheduleInfo
    
    private var taskPriority: Int = -1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        isAdd = intent.getBooleanExtra("add", true)
        index = intent.getIntExtra("index", -1)
        
        isSchedule = intent.getBooleanExtra("schedule", false)
        if (intent.getBooleanExtra("task", false)) {
            beforeTaskInfo = GlobalValue.taskInfoArrayList[index]
            inputTaskName()
        } else if (isSchedule) {
            beforeScheduleInfo = GlobalValue.scheduleInfoArrayList[index]
            inputScheduleName()
        }
    }
    
    private fun inputScheduleName() {
        setContentView(R.layout.input_plan_name)
        setActionBar(find(R.id.toolbar_back))
        
        val inputNameEdit = find<EditText>(R.id.plan_name)
        if (!isAdd) {
            inputNameEdit.setText(beforeScheduleInfo.schedule_name)
        }
        
        find<Button>(R.id.button_next).setOnClickListener {
            titleName = inputNameEdit.text.toString()
            if (titleName == "") titleName = "無題"
            
            startScheduleTime()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener {
            finish()
        }
        
    }
    
    //TODO:CHANGE XML
    private fun startScheduleTime() {
        setContentView(R.layout.start_plan_time)
        setActionBar(find(R.id.toolbar_back))
        
        val startTime = beforeScheduleInfo.start_time
        startMonth = Utils().getDate(startTime) / 100
        startDay = Utils().getDate(startTime) % 100
        startHour = Utils().getTime(startTime) / 100
        startMinute = Utils().getTime(startTime) % 100
        
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
        startYear = calendar.get(Calendar.YEAR)
        
        find<Button>(R.id.button_next).setOnClickListener { finishScheduleTime() }
        find<ImageButton>(R.id.button_back).setOnClickListener { inputScheduleName() }
        
    }
    
    private fun finishScheduleTime() {
        setContentView(R.layout.finish_plan_time)
        setActionBar(find(R.id.toolbar_back))
        
            val finishTime = beforeScheduleInfo.end_time
            finishMonth = Utils().getDate(finishTime) / 100
            finishDay = Utils().getDate(finishTime) % 100
            finishHour = Utils().getTime(finishTime) / 100
            finishMinute = Utils().getTime(finishTime) % 100
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
        find<NumberPicker>(R.id.finish_hour_num_pick).apply {
            maxValue = 23
            minValue = 0
            value = finishHour
            setOnValueChangedListener { _, _, newVal -> finishHour = newVal }
        }
        find<NumberPicker>(R.id.finish_minute_num_pick).apply {
            maxValue = 59
            minValue = 0
            value = finishMinute
            setOnValueChangedListener { _, _, newVal -> finishMinute = newVal }
        }
        finishYear = calendar.get(Calendar.YEAR)
        
        find<ImageButton>(R.id.button_back).setOnClickListener { startScheduleTime() }
        find<Button>(R.id.button_finish).setOnClickListener {
            if (isSchedule) {
                setScheduleInformation()
            } else {
                setEveryInformation()
            }
            finish()
        }
        
        
    }
    
    private fun inputTaskName() {
        setContentView(R.layout.input_task_name)
        setActionBar(find(R.id.toolbar_back))
        
        val inputTaskNameEdit = find<EditText>(R.id.input_task_name_edit)
        
        if (!isAdd) {
            inputTaskNameEdit.setText(beforeTaskInfo.task_name)
            
        }
        
        find<Button>(R.id.button_next).setOnClickListener {
            titleName = inputTaskNameEdit.text.toString()
            if (titleName == "") titleName = "無題"
            
            finishTaskTime()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener {
            finish()
        }
    }
    
    private fun finishTaskTime() {
        setContentView(R.layout.finish_task_time)
        setActionBar(find(R.id.toolbar_back))
        
        if (isAdd) {
            finishMonth = calendar.get(Calendar.MONTH) + 1
            finishDay = calendar.get(Calendar.DAY_OF_MONTH)
            finishHour = calendar.get(Calendar.HOUR_OF_DAY)
            finishMinute = calendar.get(Calendar.MINUTE)
        } else {
            val dueDate = beforeTaskInfo.due_date
            finishMonth = Utils().getDate(dueDate) / 100
            finishDay = Utils().getDate(dueDate) % 100
            finishHour = Utils().getTime(dueDate) / 100
            finishMinute = Utils().getTime(dueDate) / 100
        }
        
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
        
        find<Button>(R.id.button_next).setOnClickListener { changePriority() }
        
        finishYear = calendar.get(Calendar.YEAR)
        
        find<ImageButton>(R.id.button_back).setOnClickListener { inputTaskName() }
        
    }
    
    private fun changePriority() {
        ChangePriorityActivityUI().createView(AnkoContext.create(ctx, this))
        setActionBar(find(R.id.ankoToolbar))
        
        find<Button>(R.id.nextButton).setOnClickListener {
            val num = find<RadioGroup>(R.id.changePriorityRadioGroup).checkedRadioButtonId
            taskPriority = when (find<RadioButton>(num).text.toString()) {
                getString(R.string.mostPriority) -> 0
                getString(R.string.highPriority) -> 1
                getString(R.string.middlePriority) -> 2
                getString(R.string.lowPriority) -> 3
                else -> 4
            }
        }
        
        if (isTask) {
            setTaskInformation()
        } else {
            setEveryInformation()
        }
        finish()
        
        find<ImageButton>(R.id.button_back).setOnClickListener { changePriority() }
    }
    
    private fun setEveryInformation() {
        val everyInformation = EveryInfo()
        everyInformation.apply {
            every_name = titleName
            start_time = "$startYear-$startMonth-$startDay $startHour:$startMinute:00"
            end_time = "$finishYear-$finishMonth-$finishDay $finishHour:$finishMinute:00"
        }
        //TODO:Remove comment out when Communication
        GlobalValue.everyInfoArrayList[index] = everyInformation
        val update = UpdateEveryInfoAsync()
        update.execute(GlobalValue.everyInfoArrayList[index])
    }
    
    private fun setScheduleInformation() {
        val scheduleInformation = ScheduleInfo()
        scheduleInformation.apply {
            schedule_name = titleName
            start_time = "$startYear-$startMonth-$startDay $startHour:$startMinute:00"
            end_time = "$finishYear-$finishMonth-$finishDay $finishHour:$finishMinute:00"
        }
        GlobalValue.scheduleInfoArrayList[index] = scheduleInformation
        val update = UpdateScheduleInfoAsync()
        update.execute(GlobalValue.scheduleInfoArrayList[index])
        
    }
    
    private fun setTaskInformation() {
        val taskInformation = TaskInfo()
        taskInformation.apply {
            task_name = titleName
            due_date = "$finishYear-$finishMonth-$finishDay $finishHour:$finishMinute:00"
            priority = 0
        }
        GlobalValue.taskInfoArrayList[index] = taskInformation
        val update = UpdateTaskInfoAsync()
        update.execute(GlobalValue.taskInfoArrayList[index])
    }
}