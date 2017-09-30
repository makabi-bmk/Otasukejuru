package jp.ict.muffin.otasukejuru


object TaskInformation {
    private var name: String = ""
    private var limitDate: String = ""
    private var repeat: String = ""
    private var must: Boolean = false
    private var should: Boolean = false
    private var want: Boolean = false
    private var finishTimeMinutes: Int = 0
}