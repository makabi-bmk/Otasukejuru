package jp.ict.muffin.otasukejuru.communication

import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.TaskInfo

class UpdateTaskInfoAsyncAsync : PostInfoAsync() {
    
    fun updateTaskInfo(taskInfo: TaskInfo) {
        post("${GlobalValue.SERVER_URL}/update/task", convertToJson(taskInfo))
    }
}