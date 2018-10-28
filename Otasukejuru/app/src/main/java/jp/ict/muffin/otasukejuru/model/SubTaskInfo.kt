package jp.ict.muffin.otasukejuru.model

import java.io.Serializable

data class SubTaskInfo(
    var _id: String = "",
    var sub_task_name: String = "",
    var time: String = ""
) : Serializable
