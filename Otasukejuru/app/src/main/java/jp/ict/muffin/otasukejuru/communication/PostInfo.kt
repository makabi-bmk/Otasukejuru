package jp.ict.muffin.otasukejuru.communication

import android.os.AsyncTask
import android.util.Log
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.squareup.moshi.Moshi
import jp.ict.muffin.otasukejuru.`object`.EveryInfo
import jp.ict.muffin.otasukejuru.`object`.ScheduleInfo
import jp.ict.muffin.otasukejuru.`object`.SubTaskInfo
import jp.ict.muffin.otasukejuru.`object`.TaskInfo


open class PostInfo : AsyncTask<Any, Void, Unit>(){
    override fun doInBackground(vararg p0: Any?) {
         //TODO:overrideして
    }
    
    fun post(url: String, jsonString: String) {
        url.httpPost().body(jsonString).response { _, response, result ->
            when (result) {
                is Result.Success -> {
                    Log.d("FuelResponse", response.data.toString())
                }
                is Result.Failure -> {
                    Log.d("FuelResponse", "Failure")
                }
            }
        }
    }
    
    fun <T> convertToJson(info: T): String {
        val moshi = Moshi.Builder().build()
        
        return when (info) {
            is TaskInfo -> {
                moshi.adapter(TaskInfo::class.java).toJson(info)
            }
            is ScheduleInfo -> {
                moshi.adapter(ScheduleInfo::class.java).toJson(info)
            }
            is EveryInfo -> {
                moshi.adapter(EveryInfo::class.java).toJson(info)
                
            }
            is SubTaskInfo -> {
                moshi.adapter(SubTaskInfo::class.java).toJson(info)
            }
            else -> {
                ""
            }
        }
    }
}