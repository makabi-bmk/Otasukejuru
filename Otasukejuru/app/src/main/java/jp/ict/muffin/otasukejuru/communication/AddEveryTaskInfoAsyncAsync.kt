package jp.ict.muffin.otasukejuru.communication

import jp.ict.muffin.otasukejuru.`object`.EveryInfo
import jp.ict.muffin.otasukejuru.`object`.GlobalValue

class AddEveryTaskInfoAsyncAsync : PostInfoAsync() {
    
    fun sendEveryInfo(everyInfo: EveryInfo) {
        post("${GlobalValue.SERVER_URL}/add/every", convertToJson(everyInfo))
    }
}