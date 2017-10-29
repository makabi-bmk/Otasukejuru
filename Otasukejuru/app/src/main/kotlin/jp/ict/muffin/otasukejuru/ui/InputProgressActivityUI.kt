package jp.ict.muffin.otasukejuru.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.AsyncTask
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.squareup.moshi.Moshi
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.`object`.TaskInfo
import jp.ict.muffin.otasukejuru.activity.InputProgressActivity
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class InputProgressActivityUI(private val index: Int) : AnkoComponent<InputProgressActivity> {
    private lateinit var seekBar: SeekBar
    override fun createView(ui: AnkoContext<InputProgressActivity>): View = with(ui) {
        relativeLayout {
            relativeLayout {
                seekBar = seekBar {
                    id = R.id.inputProgressSeek
                    progress = GlobalValue.taskInfoArrayList[index].progress
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    margin = dip(100)
                }
                
                textView(GlobalValue.taskInfoArrayList[index].progress.toString()) {
                    id = R.id.progressTextView
                    text = seekBar.progress.toString()
                    textSize = 20f
                }.lparams {
                    below(seekBar)
                    centerHorizontally()
                }
            }.lparams {
                width = wrapContent
                height = wrapContent
                centerVertically()
                centerHorizontally()
            }
            
            button("確定") {
                id = R.id.finishButton
                backgroundColor = Color.argb(0, 0, 0, 0)
                textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                textSize = 20f
                onClick {
                    GlobalValue.taskInfoArrayList[index].progress = seekBar.progress
                    postProgress()
                }
            }.lparams {
                margin = 30
                alignParentBottom()
                alignParentRight()
            }
        }
        
    }
    
    private val mediaType = MediaType.parse("application/json; charset=utf-8")
    private val client = OkHttpClient()
    
    private fun postProgress(vararg params: TaskInfo) {
        val task = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg params: Unit?) {
                post("${GlobalValue.SERVER_URL}/update/task",
                        convertToJson(GlobalValue.taskInfoArrayList[index]))
            }
            
            
        }
        
        task.execute()
    }
    
    private fun post(url: String, json: String): String? {
        try {
            val body = RequestBody.create(mediaType, json)
            val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .build()
            val response = client.newCall(request).execute()
            return response.body()?.string()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    
    private fun convertToJson(taskInfo: TaskInfo): String {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(TaskInfo::class.java)
        
        Log.d("postTask", adapter.toJson(taskInfo))
        
        return adapter.toJson(taskInfo)
    }
}