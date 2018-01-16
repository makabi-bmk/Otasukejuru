package jp.ict.muffin.otasukejuru.communication

import android.util.Log
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result


open class PostInfo {
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
}