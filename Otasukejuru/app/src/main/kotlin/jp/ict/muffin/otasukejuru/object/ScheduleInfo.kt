package jp.ict.muffin.otasukejuru.`object`


data class ScheduleInfo(
        var schedule_name: String = "",
        var start_date: String = "",
        var end_date: String = "",
        var notice: Int = 0
//        @Transient var startDate: Int = 0,
//        @Transient var endDate: Int = 0
)