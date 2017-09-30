package jp.ict.muffin.otasukejuru


internal class TaskInformation {
    var name: String = ""
    var limitDate: Int = 0
    var limitTime: Int = 0
    var repeat: String = ""
    var must: Boolean = false
    var should: Boolean = false
    var want: Boolean = false
    var finishTimeMinutes: Int = 0
    var priority: Int = 0
}