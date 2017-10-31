package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.*
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.EveryInfo
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.ScheduleInfo
import jp.ict.muffin.otasukejuru.`object`.TaskInfo
import jp.ict.muffin.otasukejuru.communication.AddEveryTaskInfoAsync
import jp.ict.muffin.otasukejuru.communication.AddScheduleTaskInfoAsync
import jp.ict.muffin.otasukejuru.communication.UpdateTaskInfoAsync
import jp.ict.muffin.otasukejuru.other.Utils
import kotlinx.android.synthetic.main.set_plan_notification_time.*
import kotlinx.android.synthetic.main.set_plan_repeat.*
import kotlinx.android.synthetic.main.set_task_repeat.*
import org.jetbrains.anko.find
import java.util.*


class TaskAdditionActivity : Activity() {
    
    //common
    private var isPlan: Boolean = false
    private var taskTitleName: String = ""
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
    private var taskRepeat: Int = 0
    private var dateLimit: Int = 0
    private var timeLimit: Int = 0
    
    //plan
    private var notificationTime: Int = 0
    //task
    private var isMust: Boolean = false
    private var isShould: Boolean = false
    private var isWant: Boolean = false
    private var guideTime: Int = 0
    
    private var calendar = Calendar.getInstance()
    
    private var isAdd: Boolean = true
    private var index: Int = -1
    private lateinit var beforeTaskInfo: TaskInfo
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        isAdd = intent.getBooleanExtra("add", true)
        index = intent.getIntExtra("index", -1)
        
        if (isAdd) {
            selectAddType()
        } else {
            beforeTaskInfo = GlobalValue.taskInfoArrayList[index]
            inputTaskName()
        }
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
        startYear = calendar.get(Calendar.YEAR)
        
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
        
        find<Button>(R.id.button_next).setOnClickListener { setPlanRepeat() }
        find<ImageButton>(R.id.button_back).setOnClickListener { startPlanTime() }
        
    }
    
    private fun setPlanRepeat() {
        setContentView(R.layout.set_plan_repeat)
        setActionBar(find(R.id.toolbar_back))
        
        
        find<Button>(R.id.button_next).setOnClickListener {
            val num = plan_repeat_radio_group.checkedRadioButtonId
            
            taskRepeat = if (find<RadioButton>(num).text.toString() == "今回だけ") {
                0
            } else {
                1
            }
            setPlanNotificationTime()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener { finishPlanTime() }
        
    }
    
    private fun setPlanNotificationTime() {
        setContentView(R.layout.set_plan_notification_time)
        setActionBar(find(R.id.toolbar_back))
        
        set_notification_time_edit.setText("5")
        notificationTime = 5
        
        find<Button>(R.id.button_finish).setOnClickListener {
            val str: String = set_notification_time_edit.text.toString()
            notificationTime = Integer.parseInt(str)
            
            Log.d("plan", "タイトル名:" + taskTitleName + "\n予定開始の日付:" + startMonth + "月" +
                    startDay + "日" + startHour + "時" + startMinute + "分" + "\n予定終了の時間:" +
                    finishMonth + "月" + finishDay + "日" + finishHour + "時" +
                    finishMinute + "分" + "\n繰り返し:" + taskRepeat + "\n何分前に通知するか:" +
                    notificationTime)
            
            if (taskRepeat == 0) {
                setScheduleInformation()
            } else {
                setEveryInformation()
            }
            
            finish()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener { setPlanRepeat() }
        
    }
    
    private fun inputTaskName() {
        setContentView(R.layout.input_task_name)
        setActionBar(find(R.id.toolbar_back))
        
        val inputTaskNameEdit = find<EditText>(R.id.input_task_name_edit)
        
        if (!isAdd) {
            inputTaskNameEdit.setText(beforeTaskInfo.task_name)
            
        }
        
        find<Button>(R.id.button_next).setOnClickListener {
            taskTitleName = inputTaskNameEdit.text.toString()
            if (taskTitleName == "") taskTitleName = "無題"
            
            finishTaskTime()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener {
            if (isAdd) {
                selectAddType()
            } else {
                finish()
            }
        }
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
            
            value = if (isAdd) {
                finishMonth
            } else {
                Utils().getDate(beforeTaskInfo.due_date) / 100
            }
            setOnValueChangedListener { _, _, newVal -> finishMonth = newVal }
        }
        
        find<NumberPicker>(R.id.finish_day_num_pick).apply {
            maxValue = 31
            minValue = 1
            value = if (isAdd) {
                finishDay
            } else {
                Utils().getDate(beforeTaskInfo.due_date) % 100
            }
            setOnValueChangedListener { _, _, newVal -> finishDay = newVal }
        }
        
        find<NumberPicker>(R.id.finish_hour_edit).apply {
            maxValue = 23
            minValue = 0
            value = if (isAdd) {
                finishHour
            } else {
                Utils().getTime(beforeTaskInfo.due_date) / 100
            }
            setOnValueChangedListener { _, _, newVal -> finishHour = newVal }
        }
        
        find<NumberPicker>(R.id.finish_minute_edit).apply {
            maxValue = 59
            minValue = 0
            value = if (isAdd) {
                finishMinute
            } else {
                Utils().getTime(beforeTaskInfo.due_date) % 100
            }
            setOnValueChangedListener { _, _, newVal -> finishMinute = newVal }
        }
        
        find<Button>(R.id.button_next).setOnClickListener { setTaskRepeat() }
        
        finishYear = calendar.get(Calendar.YEAR)
        
        find<ImageButton>(R.id.button_back).setOnClickListener { inputTaskName() }
        
    }
    
    private fun setTaskRepeat() {
        setContentView(R.layout.set_task_repeat)
        setActionBar(find(R.id.toolbar_back))
        
        
        find<Button>(R.id.button_next).setOnClickListener {
            val num = task_repeat_radio_group.checkedRadioButtonId
            taskRepeat = if (find<RadioButton>(num).text.toString() == "今日だけ") {
                0
            } else {
                1
            }
            Log.d("Repeat", taskRepeat.toString())
            setMust()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener { finishTaskTime() }
        
    }
    
    private fun setMust() {
        setContentView(R.layout.set_must)
        setActionBar(find(R.id.toolbar_back))
        
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
            setTaskGuideTime()
        }
        
        find<Button>(R.id.yes_want).setOnClickListener {
            isWant = true
            setTaskGuideTime()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener { setShould() }
        
    }
    
    private fun setTaskGuideTime() {
        setContentView(R.layout.set_task_notification_time)
        setActionBar(find(R.id.toolbar_back))
        
        val finishHourEdit = find<EditText>(R.id.finish_hour_edit)
        finishHourEdit.setText(if (isAdd) {
            "0"
        } else {
            (Utils().getTime(beforeTaskInfo.guide_time) / 100).toString()
        })
        
        val finishMinuteEdit = find<EditText>(R.id.finish_minute_edit)
        finishMinuteEdit.setText(if (isAdd) {
            "5"
        } else {
            (Utils().getTime(beforeTaskInfo.guide_time) % 100).toString()
        })
        
        guideTime = if (isAdd) {
            5
            
        } else {
            Utils().getTime(beforeTaskInfo.guide_time)
        }
        find<Button>(R.id.button_next).setOnClickListener {
            guideTime = Integer.parseInt(finishHourEdit.text.toString()) * 100 +
                    Integer.parseInt(finishMinuteEdit.text.toString())
            
            if (startMonth == -1) {
                dateLimit = -1
            } else {
                dateLimit = (finishMonth - startMonth) * 100 + finishDay - startDay
                timeLimit = startHour * 100 + startDay
            }
            
            Log.d("task", "タイトル名:" + taskTitleName + "\n期限の開始:" + dateLimit +
                    "\n繰り返し:" + taskRepeat +
                    "\nisMust:" + isMust + "\nisShould:" + isShould + "\nisWant to:" +
                    isWant + "\n終了目安:" + finishHour + "時間" + finishMinute + "分")
            
            if (!isAdd) {
                val update = UpdateTaskInfoAsync()
                update.execute(GlobalValue.taskInfoArrayList[index])
            }
            
            if (taskRepeat == 0) {
                setTaskInformation()
            } else {
                setEveryInformation()
            }
            finish()
        }
        
        find<ImageButton>(R.id.button_back).setOnClickListener { setWantTo() }
    }
    
    private fun setEveryInformation() {
        val everyInformation = EveryInfo()
        everyInformation.apply {
            every_name = taskTitleName
            start_time = "$startYear-$startMonth-$startDay $startHour:$startMinute:00"
            end_time = "$finishYear-$finishMonth-$finishDay $finishHour:$finishMinute:00"
            repeat_type = taskRepeat
            
        }
        //TODO:Remove comment out when Communication
        val postEveryInfo = AddEveryTaskInfoAsync()
        postEveryInfo.execute(everyInformation)
        
    }
    
    private fun setScheduleInformation() {
        val scheduleInformation = ScheduleInfo()
        scheduleInformation.apply {
            schedule_name = taskTitleName
            start_time = "$startYear-$startMonth-$startDay $startHour:$startMinute:00"
            end_time = "$finishYear-$finishMonth-$finishDay $finishHour:$finishMinute:00"
//            startDate = startMonth * 100 + startDay
//            endDate = finishMonth * 100 + finishDay
        }
        GlobalValue.scheduleInfoArrayList.add(0, scheduleInformation)
        //TODO:Remove comment out when Communication
        val postScheduleInfo = AddScheduleTaskInfoAsync()
        postScheduleInfo.execute(scheduleInformation)
        
    }
    
    private fun setTaskInformation() {
        val taskInformation = TaskInfo()
        taskInformation.apply {
            task_name = taskTitleName
            task_type = if (isMust) {
                "1"
            } else {
                "0"
            } + if (isShould) {
                "1"
            } else {
                "0"
            } + if (isWant) {
                "1"
            } else {
                "0"
            }
            due_date = "$finishYear-$finishMonth-$finishDay $finishHour:$finishMinute:00"
            guide_time = "${guideTime / 100}:${guideTime % 100}:00"
            priority = 0
        }
        Log.d("task", taskInformation.due_date)
        GlobalValue.taskInfoArrayList.add(0, taskInformation)
        
    }
}