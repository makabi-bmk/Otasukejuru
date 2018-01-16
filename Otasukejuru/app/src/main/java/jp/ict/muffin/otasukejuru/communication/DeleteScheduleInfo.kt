package jp.ict.muffin.otasukejuru.communication

import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.ScheduleInfo

class DeleteScheduleInfo : PostInfoAsync() {
    
    fun deleteScheduleInfo(scheduleInfo: ScheduleInfo) {
        post("${GlobalValue.SERVER_URL}/delete/schedule", convertToJson(scheduleInfo))
    }
}