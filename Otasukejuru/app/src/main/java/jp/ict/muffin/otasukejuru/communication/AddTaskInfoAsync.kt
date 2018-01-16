package jp.ict.muffin.otasukejuru.communication

import android.os.AsyncTask
import com.squareup.moshi.Moshi
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.TaskInfo


class AddTaskInfoAsync : AsyncTask<TaskInfo, Void, Unit>() {
    
    override fun doInBackground(vararg params: TaskInfo) {
        AddInfo().post("${GlobalValue.SERVER_URL}/add/task", convertToJson(params[0]))
    }
    
    private fun convertToJson(taskInfo: TaskInfo): String {
        val moshi = Moshi.Builder().build()
        val taskMoshiAdapter = moshi.adapter(TaskInfo::class.java)
        
        return taskMoshiAdapter.toJson(taskInfo)
    }
}