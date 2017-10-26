package jp.ict.muffin.otasukejuru.other


class SpltDate {
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
}