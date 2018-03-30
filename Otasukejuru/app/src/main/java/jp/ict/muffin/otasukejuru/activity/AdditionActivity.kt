package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.widget.*
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.*
import jp.ict.muffin.otasukejuru.`object`.GlobalValue.notificationContent
import jp.ict.muffin.otasukejuru.`object`.GlobalValue.notificationId
import jp.ict.muffin.otasukejuru.communication.*
import jp.ict.muffin.otasukejuru.databinding.*
import jp.ict.muffin.otasukejuru.other.AlarmReceiver
import jp.ict.muffin.otasukejuru.other.Utils
import kotlinx.android.synthetic.main.activity_set_task_repeat.*
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
    private var taskRepeat: Int = 0
    private var dateLimit: Int = 0
    private var timeLimit: Int = 0

    //schedule
    private var notificationTime: Int = 0
    //task
    private var isMust: Boolean = false
    private var isShould: Boolean = false
    private var isWant: Boolean = false
    private var guideTime: Int = 0

    private var calendar = Calendar.getInstance()

    private var isAdd: Boolean = true
    private var isSub: Boolean = false
    private var index: Int = -1
    private val beforeTaskInfo: TaskInfo by lazy { TaskInfo() }
    private val beforeScheduleInfo: ScheduleInfo by lazy { ScheduleInfo() }
    private var taskProgress: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isAdd = intent.getBooleanExtra("add", true)
        isSub = intent.getBooleanExtra("sub", false)
        index = intent.getIntExtra("index", -1)

        if (isSub) {
            isAdd = false
        }
        if (isAdd) {
            selectAddType()
        } else {
            when {
                isSub || intent.getBooleanExtra("task", false) -> {
                    inputTaskName()
                }
                intent.getBooleanExtra("schedule", false) -> {
                    inputScheduleName()
                }
            }
        }
    }

    private fun selectAddType() {
        val binding: ActivitySelectionBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_selection)
        setActionBar(find(R.id.toolbar_back))

        binding.apply {
            setTaskOnClick {
                isSchedule = false
                inputTaskName()
            }
            setScheduleOnClick {
                isSchedule = true
                inputScheduleName()
            }
            setBackOnClick {
                finish()
            }
        }
    }

    private fun inputScheduleName() {
        val binding: ActivityInputScheduleNameBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_input_schedule_name)
        setActionBar(find(R.id.toolbar_back))

        binding.apply {
            defaultText = if (isAdd) {
                ""
            } else {
                beforeScheduleInfo.schedule_name
            }

            setNextOnClick {
                titleName = this.planName.text.toString()
                if (titleName == "") {
                    titleName = getString(R.string.no_title)
                }

                startScheduleTime()
            }

            setBackOnClick {
                if (isAdd) {
                    selectAddType()
                } else {
                    finish()
                }
            }
        }
    }

    private fun startScheduleTime() {
        val binding: ActivityStartScheduleTimeBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_start_schedule_time)
        setActionBar(find(R.id.toolbar_back))

        if (isAdd) {
            startMonth = calendar.get(Calendar.MONTH) + 1
            startDay = calendar.get(Calendar.DAY_OF_MONTH)
            startHour = calendar.get(Calendar.HOUR_OF_DAY)
            startMinute = calendar.get(Calendar.MINUTE)
        } else {
            val startTime = beforeScheduleInfo.start_time
            startMonth = Utils().getDate(startTime) / 100
            startDay = Utils().getDate(startTime) % 100
            startHour = Utils().getTime(startTime) / 100
            startMinute = Utils().getTime(startTime) % 100
        }

        binding.apply {
            startMonthNumPick.also {
                it.maxValue = 12
                it.minValue = 1
                it.value = startMonth
            }
            startDayNumPick.also {
                it.maxValue = 31
                it.minValue = 1
                it.value = startDay
            }
            startHourNumPick.also {
                it.maxValue = 23
                it.minValue = 0
                it.value = startHour
            }
            startMinuteNumPick.also {
                it.maxValue = 59
                it.minValue = 0
                it.value = startMinute
            }

            setNextOnClick {
                startYear = calendar.get(Calendar.YEAR)
                startMonth = startMonthNumPick.value
                startDay = startDayNumPick.value
                startHour = startHourNumPick.value
                startMinute = startMinuteNumPick.value
                
                finishScheduleTime()
            }
            setBackOnClick {
                inputScheduleName()
            }
        }
    }

    private fun finishScheduleTime() {
        val binding: ActivityFinishScheduleTimeBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_finish_schedule_time)
        setActionBar(find(R.id.toolbar_back))

        if (isAdd) {
            finishMonth = startMonth
            finishDay = startDay
            finishHour = startHour
            finishMinute = startMinute
        } else {
            val finishTime = beforeScheduleInfo.end_time
            finishMonth = Utils().getDate(finishTime) / 100
            finishDay = Utils().getDate(finishTime) % 100
            finishHour = Utils().getTime(finishTime) / 100
            finishMinute = Utils().getTime(finishTime) % 100
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

        binding.apply {
            finishMonthNumPick.also {
                it.maxValue = 12
                it.minValue = 1
                it.value = startMonth
            }
            finishDayNumPick.also {
                it.maxValue = 31
                it.minValue = 1
                it.value = startDay
            }
            finishHourNumPick.also {
                it.maxValue = 23
                it.minValue = 0
                it.value = startHour
            }
            finishMinuteNumPick.also {
                it.maxValue = 59
                it.minValue = 0
                it.value = startMinute
            }

            setNextOnClick {
                finishYear = calendar.get(Calendar.YEAR)
                finishMonth = finishMonthNumPick.value
                finishDay = finishDayNumPick.value
                finishHour = finishHourNumPick.value
                finishMinute = finishMinuteNumPick.value

                setScheduleRepeat()
            }
            setBackOnClick {
                startScheduleTime()
            }
        }
    }

    private fun setScheduleRepeat() {
        setActionBar(find(R.id.toolbar_back))
        val binding: ActivitySetScheduleRepeatBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_set_schedule_repeat)

        binding.apply {
            setNextOnClick {
                val checkedId = planRepeatRadioGroup.checkedRadioButtonId

                taskRepeat = if (find<RadioButton>(checkedId).text.toString() == "今回だけ") {
                    0
                } else {
                    1
                }
                setScheduleNotificationTime()
            }
            setBackOnClick {
                finishScheduleTime()
            }
        }
    }

    private fun setScheduleNotificationTime() {
        setActionBar(find(R.id.toolbar_back))

        val binding: ActivitySetScheduleNotificationTimeBinding =
                DataBindingUtil.setContentView(
                        this,
                        R.layout.activity_set_schedule_notification_time
                )
    
        notificationTime = 5
        binding.apply {
            this.notificationTime = this@AdditionActivity.notificationTime.toString()

            buttonText = if (isAdd) {
                getString(R.string.add)
            } else {
                getString(R.string.change)
            }
            setFinishOnClick {
                this@AdditionActivity.notificationTime =
                        Integer.parseInt(this.setNotificationTimeEdit.toString())

                Log.d("schedule", "タイトル名:" + titleName + "\n予定開始の日付:" + startMonth + "月" +
                        startDay + "日" + startHour + "時" + startMinute + "分" + "\n予定終了の時間:" +
                        finishMonth + "月" + finishDay + "日" + finishHour + "時" +
                        finishMinute + "分" + "\n繰り返し:" + taskRepeat + "\n何分前に通知するか:" +
                        notificationTime)

                if (isAdd) {
                    if (taskRepeat == 0) {
                        setScheduleInformation()
                    } else {
                        setEveryInformation()
                    }
                }

                finish()
            }
            setBackOnClick {
                setScheduleRepeat()
            }
        }
    }

    private fun inputTaskName() {
        setContentView(R.layout.activity_input_task_name)
        setActionBar(find(R.id.toolbar_back))

        val inputTaskNameEdit = find<EditText>(R.id.input_task_name_edit)

        if (!isAdd && !isSub) {
            inputTaskNameEdit.setText(beforeTaskInfo.task_name)
        }

        if (isSub) {
            find<TextView>(R.id.title).text = "サブタスク名"
            find<TextView>(R.id.question_text).text = "サブタスク名はなんですか"
            inputTaskNameEdit.hint = "サブタスク名"
        }

        find<Button>(R.id.button_next).setOnClickListener {
            titleName = inputTaskNameEdit.text.toString()
            if (titleName == "") titleName = "無題"

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
        setContentView(R.layout.activity_finish_task_time)
        setActionBar(find(R.id.toolbar_back))

        if (isAdd || isSub) {
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

        if (isSub) {
            find<TextView>(R.id.title).text = "サブタスクの時間"
            find<TextView>(R.id.question_text).text = "サブタスクをいつに入れますか？"
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

        if (isSub) {
            find<Button>(R.id.button_next).apply {
                text = "追加"
                setOnClickListener {
                    setSubTask()
                }
            }
        } else {
            find<Button>(R.id.button_next).setOnClickListener {
                setTaskRepeat()
            }
        }

        finishYear = calendar.get(Calendar.YEAR)

        find<ImageButton>(R.id.button_back).setOnClickListener { inputTaskName() }
    }

    private fun setTaskRepeat() {
        setContentView(R.layout.activity_set_task_repeat)
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
        setContentView(R.layout.activity_set_must)
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
        setContentView(R.layout.activity_set_should)
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
        setContentView(R.layout.activity_set_want)
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
        setContentView(R.layout.activity_set_task_notification_time)
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
        find<Button>(R.id.button_next).apply {
            if (!isAdd) {
                text = "変更"
                taskProgress = beforeTaskInfo.progress
            }
            setOnClickListener {
                guideTime = Integer.parseInt(finishHourEdit.text.toString()) * 100 +
                        Integer.parseInt(finishMinuteEdit.text.toString())

                if (startMonth == -1) {
                    dateLimit = -1
                } else {
                    dateLimit = (finishMonth - startMonth) * 100 + finishDay - startDay
                    timeLimit = startHour * 100 + startDay
                }

                Log.d("task", "タイトル名:" + titleName + "\n期限の開始:" + dateLimit +
                        "\n繰り返し:" + taskRepeat +
                        "\nisMust:" + isMust + "\nisShould:" + isShould + "\nisWant to:" +
                        isWant + "\n終了目安:" + finishHour + "時間" + finishMinute + "分")

                if (taskRepeat == 0) {
                    setTaskInformation()
                } else {
                    setEveryInformation()
                }
                finish()
            }
        }

        find<ImageButton>(R.id.button_back).setOnClickListener { setWantTo() }
    }

    private fun setEveryInformation() {
        val everyInformation = EveryInfo()
        everyInformation.apply {
            every_name = titleName
            start_time = "$startYear-$startMonth-$startDay $startHour:$startMinute:00"
            end_time = "$finishYear-$finishMonth-$finishDay $finishHour:$finishMinute:00"
            repeat_type = taskRepeat
        }
        if (isAdd) {
            GlobalValue.everyInfoArrayList.add(0, everyInformation)

            AddEveryTaskInfoAsync().execute(everyInformation)
        } else {
            everyInformation._id = GlobalValue.everyInfoArrayList[index]._id
            GlobalValue.everyInfoArrayList[index] = everyInformation

            UpdateEveryInfoAsync().execute(everyInformation)
        }
        Utils().saveEveryInfoList(ctx)
    }

    private fun setScheduleInformation() {
        val scheduleInformation = ScheduleInfo()
        scheduleInformation.apply {
            schedule_name = titleName
            start_time = "$startYear-$startMonth-$startDay $startHour:$startMinute:00"
            end_time = "$finishYear-$finishMonth-$finishDay $finishHour:$finishMinute:00"
        }

        setScheduleNotification(scheduleInformation)
        if (isAdd) {
            GlobalValue.scheduleInfoArrayList.add(0, scheduleInformation)

            AddScheduleTaskInfoAsync().execute(scheduleInformation)
        } else {
            scheduleInformation._id = GlobalValue.scheduleInfoArrayList[index]._id
            GlobalValue.scheduleInfoArrayList[index] = scheduleInformation

            UpdateScheduleInfoAsync().execute(scheduleInformation)
        }
        Utils().saveScheduleInfoList(ctx)
    }

    private fun setScheduleNotification(scheduleInfo: ScheduleInfo) {
        val calendar = Calendar.getInstance()
        calendar.apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.SECOND, Utils().getDiffTime(Utils().getNowDate(), scheduleInfo.start_time) - notificationTime)
        }
        scheduleNotification(scheduleInfo.schedule_name, calendar)
    }

    private fun scheduleNotification(content: String, calendar: Calendar) {
        val notificationIntent = Intent(this, AlarmReceiver::class.java)
        notificationIntent.putExtra(notificationId, 1)
        notificationIntent.putExtra(notificationContent, content)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    private fun setTaskInformation() {
        val taskInformation = TaskInfo()
        taskInformation.apply {
            task_name = titleName
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
            progress = taskProgress
        }

        if (isAdd) {
            GlobalValue.taskInfoArrayList.add(0, taskInformation)

            AddTaskInfoAsync().execute(taskInformation)
        } else {
            taskInformation._id = GlobalValue.taskInfoArrayList[index]._id
            GlobalValue.taskInfoArrayList[index] = taskInformation

            UpdateTaskInfoAsync().execute(taskInformation)
        }

        Utils().saveTaskInfoList(ctx)
    }

    private fun setSubTask() {
        val subTaskInfo = SubTaskInfo()

        subTaskInfo.apply {
            _id = GlobalValue.taskInfoArrayList[index]._id
            sub_task_name = titleName
            time = "$finishYear-$finishMonth-$finishDay $finishHour:$finishMinute:00"
        }

        UpdateTaskInfoAsync().execute(GlobalValue.taskInfoArrayList[index])

        GlobalValue.taskInfoArrayList[index].subTaskArrayList.add(subTaskInfo)
        Utils().saveTaskInfoList(ctx)
        finish()
    }
}