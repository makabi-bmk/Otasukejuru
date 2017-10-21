package jp.ict.muffin.otasukejuru.`object`


data class TaskInfo(
        var task_name: String = "",
        var due_date: String = "",
        var task_type: String = "",
        var guide_time: Int = 0,
        var progress: Int = 0,
        var priority: Int = 0,
        @Transient var limitDate: Int = 0
)