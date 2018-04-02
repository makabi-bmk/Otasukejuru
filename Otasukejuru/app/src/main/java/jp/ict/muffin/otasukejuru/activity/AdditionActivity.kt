package jp.ict.muffin.otasukejuru.activity

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.*
import jp.ict.muffin.otasukejuru.`object`.GlobalValue.notificationContent
import jp.ict.muffin.otasukejuru.`object`.GlobalValue.notificationId
import jp.ict.muffin.otasukejuru.communication.*
import jp.ict.muffin.otasukejuru.databinding.*
import jp.ict.muffin.otasukejuru.other.AlarmReceiver
import jp.ict.muffin.otasukejuru.other.Utils
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
        val binding: ActivitySetScheduleRepeatBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_set_schedule_repeat)
        setActionBar(find(R.id.toolbar_back))

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
        val binding: ActivitySetScheduleNotificationTimeBinding =
                DataBindingUtil.setContentView(
                        this,
                        R.layout.activity_set_schedule_notification_time
                )
        setActionBar(find(R.id.toolbar_back))

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
        val binding: ActivityInputTaskNameBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_input_task_name)
        setActionBar(find(R.id.toolbar_back))

        binding.apply {

            if (!isAdd && !isSub) {
                taskName = beforeTaskInfo.task_name
            }

            if (isSub) {
                title = "サブタスク名"
                body = "サブタスク名はなんですか"
                hint = "サブタスク名"
            } else {
                title = "タスク名"
                body = "タスク名は何ですか？"
                hint = "タスク名"
            }

            setNextOnClick {
                titleName = this.inputTaskNameEdit.text.toString()
                if (titleName == "") {
                    titleName = getString(R.string.no_title)
                }
                finishTaskTime()
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

    private fun finishTaskTime() {
        val binding: ActivityFinishTaskTimeBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_finish_task_time)
        setActionBar(find(R.id.toolbar_back))

        finishYear = calendar.get(Calendar.YEAR)
        if (isAdd || isSub) {
            calendar.apply {
                finishMonth = get(Calendar.MONTH) + 1
                finishDay = get(Calendar.DAY_OF_MONTH)
                finishHour = get(Calendar.HOUR_OF_DAY)
                finishMinute = get(Calendar.MINUTE)
            }
        } else {
            val dueDate = beforeTaskInfo.due_date
            Utils().apply {
                val date = getDate(dueDate)
                val time = getTime(dueDate)

                finishMonth = date / 100
                finishDay = date % 100
                finishHour = time / 100
                finishMinute = time / 100
            }
        }

        binding.apply {
            if (isSub) {
                title = "サブタスクの時間"
                body = "サブタスクをいつに入れますか？"
                buttonText = getString(R.string.add)
            } else {
                title = "タスクの期限"
                body = "期限はいつまでですか？"
                buttonText = getString(R.string.next)
            }

            finishMonthNumPick.also {
                it.maxValue = 12
                it.minValue = 1
                it.value = finishMonth
            }

            finishDayNumPick.also {
                it.maxValue = 31
                it.minValue = 1
                it.value = finishDay
            }

            finishHourEdit.also {
                it.maxValue = 23
                it.minValue = 0
                it.value = finishHour
            }

            finishMinuteEdit.also {
                it.maxValue = 59
                it.minValue = 0
                it.value = finishMinute
            }

            setNextOnClick {
                if (isSub) {
                    setSubTask()
                } else {
                    setTaskRepeat()
                }
            }
            setBackOnClick {
                inputTaskName()
            }
        }
    }

    private fun setTaskRepeat() {
        val binding: ActivitySetTaskRepeatBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_set_task_repeat)
        setActionBar(find(R.id.toolbar_back))

        binding.apply {
            setNextOnClick {
                val checkedId = taskRepeatRadioGroup.checkedRadioButtonId

                taskRepeat = if (find<RadioButton>(checkedId).text.toString() == "今回だけ") {
                    0
                } else {
                    1
                }
                setMust()
            }
            setBackOnClick {
                finishTaskTime()
            }
        }
    }

    private fun setMust() {
        val binding: ActivitySetMustBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_set_must)
        setActionBar(find(R.id.toolbar_back))
        
        binding.apply {
            setYesOnClick {
                isMust = true
                setShould()
            }
            setNoOnClick {
                isMust = false
                setShould()
            }
            setBackOnClick {
                setTaskRepeat()
            }
        }
    }

    private fun setShould() {
        val binding: ActivitySetShouldBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_set_should)
        setActionBar(find(R.id.toolbar_back))

        binding.apply {
            setYesOnClick {
                isShould = true
                setWantTo()
            }
            setNoOnClick {
                isShould = false
                setWantTo()
            }
            setBackOnClick {
                setMust()
            }
        }
    }

    private fun setWantTo() {
        val binding: ActivitySetWantBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_set_want)
        setActionBar(find(R.id.toolbar_back))

        binding.apply {
            setYesOnClick {
                isWant = true
                setTaskGuideTime()
            }
            setNoOnClick {
                isWant = false
                setTaskGuideTime()
            }
            setBackOnClick {
                setShould()
            }
        }
    }

    private fun setTaskGuideTime() {
        val binding: ActivitySetTaskNotificationTimeBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_set_task_notification_time)
        setActionBar(find(R.id.toolbar_back))

        binding.apply {
            this.defaultFinishHour = if (isAdd) {
                "0"
            } else {
                (Utils().getTime(beforeTaskInfo.guide_time) / 100).toString()
            }

            this.defaultFinishMinute = if (isAdd) {
                "5"
            } else {
                (Utils().getTime(beforeTaskInfo.guide_time) % 100).toString()
            }

            buttonText = if (isAdd) {
                getString(R.string.add)
            } else {
                taskProgress = beforeTaskInfo.progress
                getString(R.string.change)
            }

            setNextOnClick {
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
            setBackOnClick {
                setWantTo()
            }
        }

        guideTime = if (isAdd) {
            5
        } else {
            Utils().getTime(beforeTaskInfo.guide_time)
        }
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
            GlobalValue.everyInfoArrayList.add(everyInformation)

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
            GlobalValue.scheduleInfoArrayList.add(scheduleInformation)

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
            GlobalValue.taskInfoArrayList.add(taskInformation)

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