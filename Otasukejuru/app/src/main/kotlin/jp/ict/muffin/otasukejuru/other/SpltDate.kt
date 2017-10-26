package jp.ict.muffin.otasukejuru.other

import android.util.Log


class SpltDate {
    fun getDate(str: String): Int {
        if (str == "") {
            return 0
        }
        val a = str.split(" ")
        val b = a[0].split("-")
        
        Log.d(a.toString(), b.toString())
        return Integer.parseInt(b[1]) * 100 + Integer.parseInt(b[2])
    }
    
    fun getTime(str: String): Int {
        val a = str.split(" ")
        val b = a[1].split(":")
        
        return Integer.parseInt(b[0]) * 100 + Integer.parseInt(b[1])
    }
}