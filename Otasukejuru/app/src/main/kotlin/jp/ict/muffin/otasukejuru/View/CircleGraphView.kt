package jp.ict.muffin.otasukejuru.View

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.util.Log
import android.view.View
import jp.ict.muffin.otasukejuru.GlobalValue
import java.util.*


class CircleGraphView(context: Context, private var param: Int, private var time: Long, private var isInit: Boolean) : View(context) {
    private var _start_angle: Float = (60 - time) * 6f
    internal var _end_angle: Float = 0.0f
    
    init {
        if (isInit) {
            time = 1
        } else {
            time *= 60 * 10L
        }
        _start_angle -= 90
        _end_angle = _start_angle
    }

    public override fun onDraw(c: Canvas) {
        val width = c.width
        val radius = (width / 2f - 30f)
        val start_angle: Float = _start_angle % 360
        val x = radius + 15f
        val y = radius + 15f
        val end_angle = Math.min(start_angle + 360, _end_angle)
        this.createPieSlice(c, param, start_angle, end_angle, x, y, radius)
    }

    private fun createPieSlice(c: Canvas, color: Int, start_angle: Float, end_angle: Float, x: Float, y: Float, r: Float) {
        var paint = Paint()
        paint.isAntiAlias = false
        paint.color = color
        val oval1 = RectF(x - r, y - r, x + r, y + r)
        c.drawArc(oval1, start_angle, end_angle - start_angle, true, paint)

        //外枠
        paint = Paint()
        paint.color = Color.argb(0, 0, 0, 0)
        paint.style = Paint.Style.STROKE
        c.drawArc(oval1, start_angle, end_angle - start_angle, true, paint)
    }

    internal lateinit var timer: Timer
    private var ct = 0
    fun startAnimation() {
        val handler = Handler()
        val task = object : TimerTask() {
            override fun run() {
                val angle = (360 / 100f)
                _end_angle += angle
                Log.d("time:", ct++.toString())
                if (_end_angle > 270f) {
                    _end_angle = 270f
                    timer.cancel()
//                    if (!isInit) {
                        GlobalValue.setTimerFlag(true)
//                    }
                }
                handler.post { invalidate() }
            }
        }

        timer = Timer()
        //アニメーションのスピード調整できるようにしたいところ
        timer.schedule(task, 0, time)
    }

    fun changeParam(param: Int) {
        this.param = param
        this.invalidate()
    }
}
