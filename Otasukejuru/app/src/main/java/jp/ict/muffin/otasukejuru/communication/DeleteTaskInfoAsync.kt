package jp.ict.muffin.otasukejuru.communication

import android.os.AsyncTask
import com.squareup.moshi.Moshi
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.TaskInfo


class DeleteTaskInfoAsync : AsyncTask<TaskInfo, Void, Unit>() {
    
    override fun doInBackground(vararg params: TaskInfo) {
        PostInfo().post("${GlobalValue.SERVER_URL}/delete/task", convertToJson(params[0]))
    }
    
    private fun convertToJson(taskInfo: TaskInfo): String {
        val moshi = Moshi.Builder().build()
        val taskMoshiAdapter = moshi.adapter(TaskInfo::class.java)
        
        return taskMoshiAdapter.toJson(taskInfo)
    }
}