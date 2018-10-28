package jp.ict.muffin.otasukejuru.model

import java.io.Serializable

data class ScheduleInfo(
    var _id: String = "",
    var schedule_name: String = "",
    var start_time: String = "",
    var end_time: String = ""
) : Serializable
