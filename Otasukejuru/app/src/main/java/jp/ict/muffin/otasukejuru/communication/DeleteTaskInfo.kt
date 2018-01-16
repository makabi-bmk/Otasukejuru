package jp.ict.muffin.otasukejuru.communication

import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.TaskInfo

class DeleteTaskInfo : PostInfoAsync() {
    
    fun deleteTaskInfo(taskInfo: TaskInfo) {
        post("${GlobalValue.SERVER_URL}/delete/task", convertToJson(taskInfo))
    }
}