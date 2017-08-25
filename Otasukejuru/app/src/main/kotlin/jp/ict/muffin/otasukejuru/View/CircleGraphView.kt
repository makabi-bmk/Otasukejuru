package jp.ict.muffin.otasukejuru.View

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.view.View
import java.util.*

/**
 * Created by mito on 2017/08/25.
 */

class CircleGraphView(context: Context, private var params: ArrayList<HashMap<String, String>>?) : View(context) {
    internal var _end_angle = -90f

    public override fun onDraw(c: Canvas) {
        val width = c.width
        val length = params!!.size
        val max = (0 until length)
                .map { Integer.parseInt(params!![it]["value"]).toFloat() }
                .sum()
        val radius = (width / 2 - 30).toFloat()
        var start_angle = -90f
        var end_angle = 0f
        val x = radius + 15f
        val y = radius + 15f
        for (i in 0 until length) {
            val `val` = Integer.parseInt(params!![i]["value"]).toFloat()
            end_angle = start_angle + 360 * (`val` / max)
            if (end_angle > _end_angle) {
                end_angle = _end_angle
            }
            this.createPieSlice(c, Integer.parseInt(params!![i]["color"]), start_angle, end_angle, x, y, radius)
            start_angle = end_angle
        }
    }

    private fun createPieSlice(c: Canvas, color: Int, start_angle: Float, end_angle: Float, x: Float, y: Float, r: Float) {
        var paint = Paint()
        paint.isAntiAlias = false
        paint.color = color
        val oval1 = RectF(x - r, y - r, x + r, y + r)
        c.drawArc(oval1, start_angle, end_angle - start_angle, true, paint)

        //外枠
        paint = Paint()
        paint.color = Color.argb(255, 0, 0, 0)
        paint.style = Paint.Style.STROKE
        c.drawArc(oval1, start_angle, end_angle - start_angle, true, paint)

    }


    internal lateinit var timer: Timer

    fun startAnimation() {
        _end_angle = -90f
        val handler = Handler()
        val task = object : TimerTask() {
            override fun run() {
                val angle = (360 / 100.0).toFloat()
                _end_angle += angle
                if (_end_angle > 270) {
                    _end_angle = 270f
                    timer.cancel()
                }
                handler.post { invalidate() }
            }
        }

        timer = Timer()
        //アニメーションのスピード調整できるようにしたいところ
        timer.schedule(task, 0, 30)

    }

    fun changeParam(params: ArrayList<HashMap<String, String>>) {
        this.params = params
        this.invalidate()
    }
}
