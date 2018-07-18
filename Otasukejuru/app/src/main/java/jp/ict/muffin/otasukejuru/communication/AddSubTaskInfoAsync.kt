package jp.ict.muffin.otasukejuru.communication

import android.os.AsyncTask
import com.squareup.moshi.Moshi
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.SubTaskInfo
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class AddSubTaskInfoAsync : AsyncTask<
        SubTaskInfo,
        Void,
        Unit
        >() {
    private val client = OkHttpClient()

    override fun doInBackground(vararg params: SubTaskInfo) {
        post(
                "${GlobalValue.SERVER_URL}/add/sub_task",
                convertToJson(params[0])
        )
    }

    private fun post(
        url: String,
        json: String
    ): String? {
        try {
            val body = RequestBody.create(
                    GlobalValue.mediaType,
                    json
            )
            val request = Request.Builder().apply {
                url(url)
                post(body)
            }.build()
            val response = client.newCall(request).execute()
            return response.body()?.string()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun convertToJson(subTaskInfo: SubTaskInfo): String {
        val moshi = Moshi.Builder().build()
        val subMoshiAdapter = moshi.adapter(SubTaskInfo::class.java)

        return subMoshiAdapter.toJson(subTaskInfo)
    }
}