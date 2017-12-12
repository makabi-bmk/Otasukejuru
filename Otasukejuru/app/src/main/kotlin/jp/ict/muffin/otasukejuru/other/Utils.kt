package jp.ict.muffin.otasukejuru.other

import android.annotation.SuppressLint
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
}