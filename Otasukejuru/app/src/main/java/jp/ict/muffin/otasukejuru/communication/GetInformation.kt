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
                        parseTaskArray(jsonArray.toString(), true)
                    }
                    "schedule" -> {
                        parseScheduleArray(jsonArray.toString(), true)
                    }
                    "every" -> {
                        parseEveryArray(jsonArray.toString(), true)
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
        
        parseTaskArray(jsonArray.toString(), false)
    }
    
    private fun getCalendarInfo() {
        val response = run("${GlobalValue.SERVER_URL}/get/calendar")
        
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
                when (key) {
                    "schedule" -> {
                        parseScheduleArray(jsonArray.toString(), false)
                    }
                    "every" -> {
                        parseEveryArray(jsonArray.toString(), false)
                    }
                    else -> {
                    }
                }
            }
        }
        GlobalValue.apply {
            scheduleInfoArrayList = scheduleInfoArray
            everyInfoArrayList = everyInfoArray
        }
    }
    
    private fun parseTaskArray(jsonArrayString: String, isFriend: Boolean) {
        val taskArrayParameterizedType =
                Types.newParameterizedType(ArrayList::class.java, TaskInfo::class.java)
        val taskArrayMoshiAdapter =
                moshi.adapter<ArrayList<TaskInfo>>(taskArrayParameterizedType)
        if (isFriend) {
            GlobalValue.friendTaskInfoArrayList =
                    taskArrayMoshiAdapter.fromJson(jsonArrayString)
                            ?: GlobalValue.friendTaskInfoArrayList
        } else {
            GlobalValue.taskInfoArrayList =
                    taskArrayMoshiAdapter.fromJson(jsonArrayString)
                            ?: GlobalValue.taskInfoArrayList
        }
    }
    
    private fun parseScheduleArray(jsonArrayString: String, isFriend: Boolean) {
        val scheduleArrayParameterizedType =
                Types.newParameterizedType(ArrayList::class.java, ScheduleInfo::class.java)
        val scheduleArrayMoshiAdapter =
                moshi.adapter<ArrayList<ScheduleInfo>>(scheduleArrayParameterizedType)
        if (isFriend) {
            GlobalValue.friendScheduleInfoArrayList =
                    scheduleArrayMoshiAdapter.fromJson(jsonArrayString)
                            ?: GlobalValue.friendScheduleInfoArrayList
        } else {
            GlobalValue.scheduleInfoArrayList =
                    scheduleArrayMoshiAdapter.fromJson(jsonArrayString)
                            ?: GlobalValue.scheduleInfoArrayList
        }
    }
    
    private fun parseEveryArray(jsonArrayString: String, isFriend: Boolean) {
        val everyArrayParameterizedType =
                Types.newParameterizedType(ArrayList::class.java, EveryInfo::class.java)
        val everyArrayMoshiAdapter =
                moshi.adapter<ArrayList<EveryInfo>>(everyArrayParameterizedType)
        if (isFriend) {
            GlobalValue.friendEveryInfoArrayList =
                    everyArrayMoshiAdapter.fromJson(jsonArrayString)
                            ?: GlobalValue.friendEveryInfoArrayList
        } else {
            GlobalValue.everyInfoArrayList =
                    everyArrayMoshiAdapter.fromJson(jsonArrayString)
                            ?: GlobalValue.everyInfoArrayList
        }
    }
    
}