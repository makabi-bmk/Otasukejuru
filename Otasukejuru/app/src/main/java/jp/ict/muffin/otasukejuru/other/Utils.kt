package jp.ict.muffin.otasukejuru.other

import android.annotation.SuppressLint
import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import jp.ict.muffin.otasukejuru.`object`.EveryInfo
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.ScheduleInfo
import jp.ict.muffin.otasukejuru.`object`.TaskInfo
import org.json.JSONArray
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Calendar

class Utils {
    fun getDate(time: String): Int {
        if (time == "") {
            return 0
        }
        val binaryTime = time.split(" ")
        val date = binaryTime[0].split("-")

        return Integer.parseInt(date[1]) * 100 + Integer.parseInt(date[2])
    }

    fun getTime(time: String): Int {
        if (time == "") {
            return 0
        }
        val binaryTime = time.split(" ")
        val date = binaryTime[binaryTime.size - 1].split(":")

        return Integer.parseInt(date[0]) * 100 + Integer.parseInt(date[1])
    }

    fun diffDayNum(beforeDate: Int, afterDate: Int, year: Int): Int {
        val totalDays = if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        } else {
            intArrayOf(0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335)
        }

        val beforeDay: Int = beforeDate % 100
        val beforeMonth: Int = beforeDate / 100
        val afterDay: Int = afterDate % 100
        val afterMonth: Int = afterDate / 100
        return (totalDays[afterMonth - 1] + afterDay - (totalDays[beforeMonth - 1] + beforeDay))
    }

    @SuppressLint("SimpleDateFormat")
    fun getNowDate(): String {
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(System.currentTimeMillis())
        return df.format(date)
    }

    fun getDiffTime(beforeDate: String, afterDate: String): Int {
        var diffDays = diffDayNum(getDate(beforeDate), getDate(afterDate), Calendar.YEAR) - 1
        if (diffDays < 0) {
            diffDays = 0
        }

        val beforeTime =
                (24 - getTime(beforeDate) / 100 + 1) * 60 + (60 - getTime(beforeDate) % 100) * 60
        val afterTime = getTime(afterDate) / 100 * 60 + getTime(afterDate) % 100 * 60
        return diffDays * 60 * 24 + afterTime + beforeTime
    }

    fun saveTaskInfoList(ctx: Context) {
        setPriority()

        val moshi = Moshi.Builder().build()
        val taskArrayParameterizedType =
                Types.newParameterizedType(List::class.java, TaskInfo::class.java)
        val adapter = moshi.adapter<ArrayList<TaskInfo>>(taskArrayParameterizedType)

        saveString(ctx, ctx.getString(R.string.task_info_key),
                adapter.toJson(GlobalValue.taskInfoArrayList).toString())
    }

    fun saveScheduleInfoList(ctx: Context) {
        val moshi = Moshi.Builder().build()
        val scheduleArrayParameterizedType =
                Types.newParameterizedType(List::class.java, ScheduleInfo::class.java)
        val adapter = moshi.adapter<ArrayList<ScheduleInfo>>(scheduleArrayParameterizedType)

        saveString(ctx, ctx.getString(R.string.schedule_info_key),
                adapter.toJson(GlobalValue.scheduleInfoArrayList).toString())
    }

    fun saveEveryInfoList(ctx: Context) {
        val moshi = Moshi.Builder().build()
        val everyArrayParameterizedType =
                Types.newParameterizedType(List::class.java, EveryInfo::class.java)
        val adapter = moshi.adapter<ArrayList<EveryInfo>>(everyArrayParameterizedType)

        saveString(ctx, ctx.getString(R.string.every_info_key),
                adapter.toJson(GlobalValue.everyInfoArrayList).toString())
    }

    // 設定値 String を保存（Context は Activity や Application や Service）
    private fun saveString(ctx: Context, key: String, value: String) {
        ctx.getSharedPreferences(ctx.getString(R.string.app_name), Context.MODE_PRIVATE).edit().apply {
            putString(key, value)
        }.apply()
    }

    // 設定値 String を取得（Context は Activity や Application や Service）
    fun loadString(ctx: Context, key: String): String =
            ctx.getSharedPreferences(ctx.getString(R.string.app_name),
                    Context.MODE_PRIVATE).getString(key, "") // 第２引数はkeyが存在しない時に返す初期値

    fun parseData(ctx: Context, jsonDataString: String = "", parseKey: String = "") {
        val moshi = Moshi.Builder().build()
        if (jsonDataString == "") {
            return
        }

        when (parseKey) {
            ctx.getString(R.string.schedule_info_key) -> {
                val scheduleInfoAdapter = moshi.adapter(ScheduleInfo::class.java)

                val jsonArray = JSONArray(jsonDataString)
                val tmpScheduleArrayList: ArrayList<ScheduleInfo> = arrayListOf()
                (0 until jsonArray.length()).forEach { i ->
                    val dataJSON = jsonArray.getJSONObject(i).toString()
                    scheduleInfoAdapter.fromJson(dataJSON)?.let { tmpScheduleArrayList.add(it) }
                }
                GlobalValue.scheduleInfoArrayList = tmpScheduleArrayList
            }

            ctx.getString(R.string.task_info_key) -> {
                val taskInfoAdapter = moshi.adapter(TaskInfo::class.java)

                val jsonArray = JSONArray(jsonDataString)
                val tmpTaskArrayList: ArrayList<TaskInfo> = arrayListOf()
                (0 until jsonArray.length()).forEach { i ->
                    val dataJSON = jsonArray.getJSONObject(i).toString()
                    taskInfoAdapter.fromJson(dataJSON)?.let { tmpTaskArrayList.add(it) }
                }
                GlobalValue.taskInfoArrayList = tmpTaskArrayList
            }

            ctx.getString(R.string.every_info_key) -> {
                val everyInfoAdapter = moshi.adapter(EveryInfo::class.java)

                val jsonArray = JSONArray(jsonDataString)
                val tmpEveryArrayList: ArrayList<EveryInfo> = arrayListOf()
                (0 until jsonArray.length()).forEach { i ->
                    val dataJSON = jsonArray.getJSONObject(i).toString()
                    everyInfoAdapter.fromJson(dataJSON)?.let { tmpEveryArrayList.add(it) }
                }
                GlobalValue.everyInfoArrayList = tmpEveryArrayList
            }
        }
    }

    private fun setPriority() {
        val sortedTaskInfoArray = arrayListOf<TaskInfo>()
        GlobalValue.taskInfoArrayList.forEach {
            sortedTaskInfoArray.add(it)
        }

        sortedTaskInfoArray.apply {
            forEach {
                val diffDays = getDiffDays(it.due_date)
                val taskPriority = it.priority
                it.priority =
                        taskPriority / 100 * 15 + taskPriority % 10 / 10 * 5 +
                                taskPriority % 100 * 10 + it.progress % 50 + (10 - diffDays) * 6
            }
            sortByDescending { it.priority }
            forEach {
                it.priority = when (it.priority) {
                    in 0..25 -> 3
                    in 26..50 -> 2
                    in 51..75 -> 1
                    else -> 0
                }
            }
        }
        GlobalValue.taskInfoArrayList = sortedTaskInfoArray
    }

    private fun getDiffDays(afterDate: String): Int {
        val calendar = Calendar.getInstance()
        val today = (calendar.get(Calendar.MONTH) + 1) * 100 +
                calendar.get(Calendar.DAY_OF_MONTH)
        return Utils().diffDayNum(today, Utils().getDate(afterDate),
                calendar.get(Calendar.YEAR))
    }
}