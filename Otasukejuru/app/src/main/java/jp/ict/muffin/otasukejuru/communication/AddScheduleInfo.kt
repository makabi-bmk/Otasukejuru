package jp.ict.muffin.otasukejuru.communication

import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.ScheduleInfo

class AddScheduleInfo : PostInfoAsync() {
    
    fun sendScheduleInfo(scheduleInfo: ScheduleInfo) {
        post("${GlobalValue.SERVER_URL}/add/schedule", convertToJson(scheduleInfo))
    }
}