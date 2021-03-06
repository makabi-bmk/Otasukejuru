package jp.ict.muffin.otasukejuru_peer.other

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
    
    fun getNowDate(): String {
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(System.currentTimeMillis())
        return df.format(date)
    }
}