package jp.ict.muffin.otasukejuru.`object`


object GlobalValue {
    var displayWidth: Int = 0
    var displayHeight: Int = 0
    var taskInfoArrayList = ArrayList<TaskInfo>()
    var scheduleInfoArrayList = ArrayList<ScheduleInfo>()
    var everyInfoArrayList = ArrayList<EveryInfo>()
    var friendTaskInfoArrayList = ArrayList<TaskInfo>()
    var friendScheduleInfoArrayList = ArrayList<ScheduleInfo>()
    var friendEveryInfoArrayList = ArrayList<EveryInfo>()
    var SERVER_URL: String = ""
    var focusTimeG: Long = 1
    var intervalTimeG: Long = 0
    var notificationId = "notificationId"
    var notificationContent = "content"
}