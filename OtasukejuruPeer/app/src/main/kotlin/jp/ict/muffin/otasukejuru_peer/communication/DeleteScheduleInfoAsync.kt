package jp.ict.muffin.otasukejuru_peer.communication

import android.os.AsyncTask
import com.squareup.moshi.Moshi
import jp.ict.muffin.otasukejuru_peer.`object`.GlobalValue
import jp.ict.muffin.otasukejuru_peer.`object`.ScheduleInfo
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class DeleteScheduleInfoAsync : AsyncTask<ScheduleInfo, Void, Unit>() {
    private val mediaType = MediaType.parse("application/json; charset=utf-8")
    private var client = OkHttpClient()
    
    override fun doInBackground(vararg params: ScheduleInfo) {
        post("${GlobalValue.SERVER_URL}/delete/schedule", convertToJson(params[0]))
    }
    
    private fun post(url: String, json: String): String? {
        try {
            val body = RequestBody.create(mediaType, json)
            val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .build()
            val response = client.newCall(request).execute()
            return response.body()?.string()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    
    private fun convertToJson(scheduleInfo: ScheduleInfo): String {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(ScheduleInfo::class.java)
        
        return adapter.toJson(scheduleInfo)
    }
}