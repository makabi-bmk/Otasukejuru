package jp.ict.muffin.otasukejuru.communication

import com.github.kittinunf.fuel.httpPost


open class AddInfo {
    fun post(url: String, jsonString: String): String? = try {
        url.httpPost().body(jsonString).response().second.data.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}