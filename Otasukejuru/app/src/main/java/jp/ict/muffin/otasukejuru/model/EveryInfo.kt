package jp.ict.muffin.otasukejuru.model

import java.io.Serializable

data class EveryInfo(
    var _id: String = "",
    var every_name: String = "",
    var start_time: String = "",
    var end_time: String = "",
    var repeat_type: Int = 0
) : Serializable
