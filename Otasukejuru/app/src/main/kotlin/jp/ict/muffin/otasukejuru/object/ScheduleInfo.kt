package jp.ict.muffin.otasukejuru.`object`


data class ScheduleInfo(
        var schedule_name: String = "",
        var start_date: Int = 0,
        var end_date: Int = 0,
        var notice: Int = 0
)