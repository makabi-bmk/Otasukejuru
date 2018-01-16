package jp.ict.muffin.otasukejuru.communication

import jp.ict.muffin.otasukejuru.`object`.EveryInfo
import jp.ict.muffin.otasukejuru.`object`.GlobalValue

class UpdateEveryInfoAsyncAsync : PostInfoAsync() {
    
    fun updateEveryInfo(everyInfo: EveryInfo) {
        post("${GlobalValue.SERVER_URL}/update/schedule", convertToJson(everyInfo))
    }
}
