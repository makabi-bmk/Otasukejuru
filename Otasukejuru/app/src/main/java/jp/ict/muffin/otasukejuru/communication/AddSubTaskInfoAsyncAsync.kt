package jp.ict.muffin.otasukejuru.communication

import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.SubTaskInfo

class AddSubTaskInfoAsyncAsync : PostInfoAsync() {
    
    fun sendSubTaskInfo(subTaskInfo: SubTaskInfo) {
        post("${GlobalValue.SERVER_URL}/add/sub_task", convertToJson(subTaskInfo))
    }
}