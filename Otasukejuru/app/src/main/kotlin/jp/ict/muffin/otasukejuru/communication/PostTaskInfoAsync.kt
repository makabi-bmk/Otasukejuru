package jp.ict.muffin.otasukejuru.communication

import android.os.AsyncTask
import android.util.Log
import com.squareup.moshi.Moshi
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.TaskInfo
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody


class PostTaskInfoAsync : AsyncTask<TaskInfo, Void, Unit>() {
    private val mediaType = MediaType.parse("application/json; charset=utf-8")
    private val client = OkHttpClient()
    
    override fun doInBackground(vararg params: TaskInfo) {
        post("${GlobalValue.SERVER_URL}/add/task", convertToJson(params[0]))
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
    
    private fun convertToJson(taskInfo: TaskInfo): String {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(TaskInfo::class.java)
        
        Log.d("postTask", adapter.toJson(taskInfo))
        
        return adapter.toJson(taskInfo)
    }
}