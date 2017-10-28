package jp.ict.muffin.otasukejuru.`object`


data class TaskInfo(
        var _id: String = "",
        var task_name: String = "",
        var due_date: String = "",
        var task_type: String = "",
        var guide_time: String = "",
        var progress: Int = 0,
        var priority: Int = 0,
        var friend: Boolean = false
)