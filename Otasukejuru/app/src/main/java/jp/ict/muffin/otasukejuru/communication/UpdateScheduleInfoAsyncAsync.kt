package jp.ict.muffin.otasukejuru.communication

import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.ScheduleInfo

class UpdateScheduleInfoAsyncAsync : PostInfoAsync() {
    
    fun updateScheduleInfo(scheduleInfo: ScheduleInfo) {
        post("${GlobalValue.SERVER_URL}/update/schedule", convertToJson(scheduleInfo))
    }
}