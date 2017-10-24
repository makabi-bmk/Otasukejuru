package jp.ict.muffin.otasukejuru.communication

import android.os.AsyncTask
import com.squareup.moshi.Moshi
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.ScheduleInfo
import jp.ict.muffin.otasukejuru.`object`.TaskInfo
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException


class GetInformation : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg params: Unit?) {
        getInfo()
    }
    
    private val client = OkHttpClient()
    
    private fun run(url: String): String {
        val request = Request.Builder()
                .url(url)
                .build()
        
        var response: Response? = null
        try {
            response = client.newCall(request).execute()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response?.body().toString()
    }
    
    private fun getInfo() {
        
        getTaskInfo()
        getCalendarInfo()
        
    }
    
    private fun getTaskInfo() {
        val response = run("${GlobalValue.SERVER_URL}/get/todo_list")
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(TaskInfo::class.java)
        
        
        val jsonArray = JSONArray(response)
        (0 until jsonArray.length()).forEach { i ->
            val taskJSON = jsonArray.getJSONObject(i).toString()
            adapter.fromJson(taskJSON)?.let { GlobalValue.taskInfoArrayList.add(it) }
            
        }
        
    }
    
    private fun getCalendarInfo() {
        val response = run("${GlobalValue.SERVER_URL}/get/calendar")
        
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(ScheduleInfo::class.java)
        
        val jsonArray = JSONArray(response)
        (0 until jsonArray.length()).forEach { i ->
            val taskJSON = jsonArray.getJSONObject(i).toString()
            adapter.fromJson(taskJSON)?.let { GlobalValue.scheduleInfoArrayList.add(it) }
            
        }
    }
}