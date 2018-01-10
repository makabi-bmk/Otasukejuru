package jp.ict.muffin.otasukejuru.communication

import android.os.AsyncTask
import android.util.Log
import com.squareup.moshi.Moshi
import jp.ict.muffin.otasukejuru.`object`.EveryInfo
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.ScheduleInfo
import jp.ict.muffin.otasukejuru.`object`.TaskInfo
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


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
            Log.d("Response", response.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response?.body()?.string().toString()
    }
    
    private fun getInfo() {
        getTaskInfo()
        getCalendarInfo()
        getFriendInfo()
        
    }
    
    private fun getFriendInfo() {
        val response = run("${GlobalValue.SERVER_URL}/get/friend")
        
        val moshi = Moshi.Builder().build()
        val taskAdapter = moshi.adapter(TaskInfo::class.java)
        val scheduleAdapter = moshi.adapter(ScheduleInfo::class.java)
        val everyAdapter = moshi.adapter(EveryInfo::class.java)
        
        val jsonObject = try {
            JSONObject(response)
        } catch (e: JSONException) {
            e.printStackTrace()
            null
        }
        val keys = arrayListOf("task", "schedule", "every")
        
        val friendTaskInfoArray: ArrayList<TaskInfo> = arrayListOf()
        val friendScheduleInfoArray: ArrayList<ScheduleInfo> = arrayListOf()
        val friendEveryInfoArray: ArrayList<EveryInfo> = arrayListOf()
        
        keys.forEach { key ->
            val jsonArray = jsonObject?.getJSONArray(key)
            if (jsonArray != null) {
                (0 until jsonArray.length()).forEach { i ->
                    val json = jsonArray.getJSONObject(i).toString()
                    when (key) {
                        "task" -> taskAdapter.fromJson(json)
                                ?.let { friendTaskInfoArray.add(it) }
                        "schedule" -> scheduleAdapter.fromJson(json)
                                ?.let { friendScheduleInfoArray.add(it) }
                        "every" -> everyAdapter.fromJson(json)
                                ?.let { friendEveryInfoArray.add(it) }
                        else -> {
                        }
                    }
                }
            }
        }
        
        GlobalValue.apply {
            friendTaskInfoArrayList = friendTaskInfoArray
            friendEveryInfoArrayList = friendEveryInfoArray
            friendScheduleInfoArrayList = friendScheduleInfoArray
        }
    }
    
    private fun getTaskInfo() {
        val response = run("${GlobalValue.SERVER_URL}/get/todo_list")
        
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(TaskInfo::class.java)
        
        var jsonArray = JSONArray()
        try {
            jsonArray = JSONObject(response).getJSONArray("todo_list")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        
        val taskInfoArray = arrayListOf<TaskInfo>()
        (0 until jsonArray.length()).forEach { i ->
            val taskJSON = jsonArray.getJSONObject(i).toString()
            adapter.fromJson(taskJSON)?.let { taskInfoArray.add(it) }
        }
        GlobalValue.taskInfoArrayList = taskInfoArray
    }
    
    private fun getCalendarInfo() {
        val response = run("${GlobalValue.SERVER_URL}/get/calendar")
        
        val moshi = Moshi.Builder().build()
        val scheduleAdapter = moshi.adapter(ScheduleInfo::class.java)
        val everyAdapter = moshi.adapter(EveryInfo::class.java)
        
        val jsonObject = try {
            JSONObject(response)
        } catch (e: JSONException) {
            e.printStackTrace()
            null
        }
        val keys = arrayListOf("schedule", "every")
        
        val scheduleInfoArray = arrayListOf<ScheduleInfo>()
        val everyInfoArray = arrayListOf<EveryInfo>()
        keys.forEach { key ->
            val jsonArray = jsonObject?.getJSONArray(key)
            if (jsonArray != null) {
                (0 until jsonArray.length()).forEach { i ->
                    val json = jsonArray.getJSONObject(i).toString()
                    when (key) {
                        "schedule" -> scheduleAdapter.fromJson(json)
                                ?.let { scheduleInfoArray.add(it) }
                        "every" -> everyAdapter.fromJson(json)
                                ?.let { everyInfoArray.add(it) }
                        else -> {
                        }
                    }
                }
            }
        }
        GlobalValue.apply {
            scheduleInfoArrayList = scheduleInfoArray
            everyInfoArrayList = everyInfoArray
        }
    }
}