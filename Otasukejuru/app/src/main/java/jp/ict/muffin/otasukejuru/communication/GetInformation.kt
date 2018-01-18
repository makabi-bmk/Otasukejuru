package jp.ict.muffin.otasukejuru.communication

import android.os.AsyncTask
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
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
    private val moshi = Moshi.Builder().build()
    private val client = OkHttpClient()
    
    override fun doInBackground(vararg params: Unit?) {
        getInfo()
    }
    
    
    private fun run(url: String): String {
        val request = Request.Builder()
                .url(url)
                .build()
        
        var response: Response? = null
        try {
            response = client.newCall(request).execute()
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
        
        val jsonObject = try {
            JSONObject(response)
        } catch (e: JSONException) {
            e.printStackTrace()
            null
        }
        val keys = arrayListOf("task", "schedule", "every")
        
        keys.forEach { key ->
            val jsonArray = jsonObject?.getJSONArray(key)
            if (jsonArray != null) {
                when (key) {
                    "task" -> {
                        val taskArrayParameterizedType =
                                Types.newParameterizedType(ArrayList::class.java, TaskInfo::class.java)
                        val taskArrayMoshiAdapter = moshi.adapter<ArrayList<TaskInfo>>(taskArrayParameterizedType)
                        GlobalValue.friendTaskInfoArrayList = taskArrayMoshiAdapter.fromJson(jsonArray.toString())
                                ?: GlobalValue.friendTaskInfoArrayList
                    }
                    "schedule" -> {
                        val scheduleArrayParameterizedType =
                                Types.newParameterizedType(ArrayList::class.java, ScheduleInfo::class.java)
                        val scheduleArrayMoshiAdapter = moshi.adapter<ArrayList<ScheduleInfo>>(scheduleArrayParameterizedType)
                        GlobalValue.friendScheduleInfoArrayList = scheduleArrayMoshiAdapter.fromJson(jsonArray.toString())
                                ?: GlobalValue.friendScheduleInfoArrayList
                        
                        
                    }
                    "every" -> {
                        val everyArrayParameterizedType =
                                Types.newParameterizedType(ArrayList::class.java, EveryInfo::class.java)
                        val taskArrayMoshiAdapter = moshi.adapter<ArrayList<EveryInfo>>(everyArrayParameterizedType)
                        GlobalValue.friendEveryInfoArrayList = taskArrayMoshiAdapter.fromJson(jsonArray.toString())
                                ?: GlobalValue.friendEveryInfoArrayList
                    }
                    else -> {
                    }
                }
            }
        }
    }
    
    private fun getTaskInfo() {
        val response = run("${GlobalValue.SERVER_URL}/get/todo_list")
        
        var jsonArray = JSONArray()
        try {
            jsonArray = JSONObject(response).getJSONArray("todo_list")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        
        val taskArrayParameterizedType =
                Types.newParameterizedType(ArrayList::class.java, TaskInfo::class.java)
        val taskArrayMoshiAdapter = moshi.adapter<ArrayList<TaskInfo>>(taskArrayParameterizedType)
        GlobalValue.taskInfoArrayList = taskArrayMoshiAdapter.fromJson(jsonArray.toString())
                ?: GlobalValue.taskInfoArrayList
    }
    
    private fun getCalendarInfo() {
        val response = run("${GlobalValue.SERVER_URL}/get/calendar")
        
        val scheduleMoshiAdapter = moshi.adapter(ScheduleInfo::class.java)
        val everyMoshiAdapter = moshi.adapter(EveryInfo::class.java)
        
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
                        "schedule" -> scheduleMoshiAdapter.fromJson(json)
                                ?.let { scheduleInfoArray.add(it) }
                        "every" -> everyMoshiAdapter.fromJson(json)
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