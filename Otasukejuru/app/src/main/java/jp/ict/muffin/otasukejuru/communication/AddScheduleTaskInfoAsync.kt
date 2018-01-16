package jp.ict.muffin.otasukejuru.communication

import android.os.AsyncTask
import com.squareup.moshi.Moshi
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.ScheduleInfo


class AddScheduleTaskInfoAsync : AsyncTask<ScheduleInfo, Void, Unit>() {
    
    override fun doInBackground(vararg params: ScheduleInfo) {
        AddInfo().post("${GlobalValue.SERVER_URL}/add/schedule", convertToJson(params[0]))
    }
    
    private fun convertToJson(scheduleInfo: ScheduleInfo): String {
        val moshi = Moshi.Builder().build()
        val scheduleMoshiAdapter = moshi.adapter(ScheduleInfo::class.java)
        
        return scheduleMoshiAdapter.toJson(scheduleInfo)
    }
}