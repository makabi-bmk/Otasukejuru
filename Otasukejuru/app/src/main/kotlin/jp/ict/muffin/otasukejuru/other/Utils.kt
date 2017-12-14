package jp.ict.muffin.otasukejuru.other

import android.annotation.SuppressLint
import android.content.Context
import com.squareup.moshi.Moshi
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.EveryInfo
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.ScheduleInfo
import jp.ict.muffin.otasukejuru.`object`.TaskInfo
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*


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
        return (totalDays[afterMonth] + afterDay - (totalDays[beforeMonth] + beforeDay))
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
    
    // 設定値 String を保存（Context は Activity や Application や Service）
    private fun saveString(ctx: Context, key: String, value: String) {
        val prefs = ctx.getSharedPreferences(ctx.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }
    
    // 設定値 String を取得（Context は Activity や Application や Service）
    private fun loadString(ctx: Context, key: String): String {
        val prefs = ctx.getSharedPreferences(ctx.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getString(key, "") // 第２引数はkeyが存在しない時に返す初期値
    }
    
    private fun parseData(scheduleList: String = "", taskList: String = "", everyList: String = "") {
        val moshi = Moshi.Builder().build()
        when ("") {
            scheduleList -> {
                val dataAdapter = moshi.adapter(ScheduleInfo::class.java)
                
                val jsonArray = JSONArray(scheduleList)
                val tmpScheduleArrayList: ArrayList<ScheduleInfo> = arrayListOf()
                (0 until jsonArray.length()).forEach { i ->
                    val dataJSON = jsonArray.getJSONObject(i).toString()
                    dataAdapter.fromJson(dataJSON)?.let { tmpScheduleArrayList.add(it) }
                }
                GlobalValue.scheduleInfoArrayList = tmpScheduleArrayList
            }
            
            taskList -> {
                val dataAdapter = moshi.adapter(TaskInfo::class.java)
                
                val jsonArray = JSONArray(taskList)
                val tmpTaskArrayList: ArrayList<TaskInfo> = arrayListOf()
                (0 until jsonArray.length()).forEach { i ->
                    val dataJSON = jsonArray.getJSONObject(i).toString()
                    dataAdapter.fromJson(dataJSON)?.let { tmpTaskArrayList.add(it) }
                }
                GlobalValue.taskInfoArrayList = tmpTaskArrayList
            }
            
            everyList -> {
                val dataAdapter = moshi.adapter(EveryInfo::class.java)
                
                val jsonArray = JSONArray(everyList)
                val tmpEveryArrayList: ArrayList<EveryInfo> = arrayListOf()
                (0 until jsonArray.length()).forEach { i ->
                    val dataJSON = jsonArray.getJSONObject(i).toString()
                    dataAdapter.fromJson(dataJSON)?.let { tmpEveryArrayList.add(it) }
                }
                GlobalValue.everyInfoArrayList = tmpEveryArrayList
            }
        }
    }
}