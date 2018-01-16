package jp.ict.muffin.otasukejuru.communication

import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.TaskInfo

class AddTaskInfoAsyncAsync : PostInfoAsync() {
    
    fun sendTaskInfo(taskInfo: TaskInfo) {
        post("${GlobalValue.SERVER_URL}/add/task", convertToJson(taskInfo))
    }
}