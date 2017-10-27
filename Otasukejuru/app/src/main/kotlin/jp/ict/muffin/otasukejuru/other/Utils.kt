package jp.ict.muffin.otasukejuru.other


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
        val date = binaryTime[1].split(":")
        
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
    
    private fun diffTime(beforeTime: Int, afterTime: Int, diffDay: Int): Int {
        val before = beforeTime / 100 * 60 + beforeTime % 100
        val after = afterTime / 100 * 60 + afterTime % 100
        val diff = after - before
        return diff / 60 * 100 + diff % 60
        
    }
}