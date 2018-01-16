package jp.ict.muffin.otasukejuru.communication

import android.os.AsyncTask
import com.squareup.moshi.Moshi
import jp.ict.muffin.otasukejuru.`object`.EveryInfo
import jp.ict.muffin.otasukejuru.`object`.GlobalValue

class AddEveryTaskInfoAsync : AsyncTask<EveryInfo, Void, Unit>() {
    
    override fun doInBackground(vararg params: EveryInfo) {
        PostInfo().post("${GlobalValue.SERVER_URL}/add/every", convertToJson(params[0]))
    }
    
    private fun convertToJson(everyInfo: EveryInfo): String {
        val moshi = Moshi.Builder().build()
        val everyMoshiAdapter = moshi.adapter(EveryInfo::class.java)
        
        return everyMoshiAdapter.toJson(everyInfo)
    }
}