package jp.ict.muffin.otasukejuru.communication

import android.os.AsyncTask
import com.squareup.moshi.Moshi
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.SubTaskInfo


class AddSubTaskInfoAsync : AsyncTask<SubTaskInfo, Void, Unit>() {
    
    override fun doInBackground(vararg params: SubTaskInfo) {
        AddInfo().post("${GlobalValue.SERVER_URL}/add/sub_task", convertToJson(params[0]))
    }
    
    private fun convertToJson(subTaskInfo: SubTaskInfo): String {
        val moshi = Moshi.Builder().build()
        val subMoshiAdapter = moshi.adapter(SubTaskInfo::class.java)
        
        return subMoshiAdapter.toJson(subTaskInfo)
    }
}