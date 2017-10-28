package jp.ict.muffin.otasukejuru.`object`


data class ScheduleInfo(
        var schedule_name: String = "",
        var start_time: String = "",
        var end_time: String = ""
//        @Transient var startDate: Int = 0,
//        @Transient var endDate: Int = 0
)