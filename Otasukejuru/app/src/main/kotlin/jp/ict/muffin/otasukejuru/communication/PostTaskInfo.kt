package jp.ict.muffin.otasukejuru.communication

import android.os.AsyncTask
import com.squareup.moshi.Moshi
import jp.ict.muffin.otasukejuru.`object`.TaskInfo
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody


class PostTaskInfo() : AsyncTask<TaskInfo, Void, Unit>() {
    private val mediaType = MediaType.parse("application/json; charset=utf-8")
    var client = OkHttpClient()
    
    override fun doInBackground(vararg params: TaskInfo) {
        post("hoge", convertToJson(params[0]))
    }
    
    private fun post(url: String, json: String): String? {
        val body = RequestBody.create(mediaType, json)
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
        val response = client.newCall(request).execute()
        return response.body()?.string()
    }
    
    private fun convertToJson(taskInfo: TaskInfo): String {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(TaskInfo::class.java)
        
        return adapter.toJson(taskInfo)
    }
}