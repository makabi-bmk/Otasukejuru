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

class CircleGraphView(context: Context, private var param: HashMap<String, String>) : View(context) {
    internal var _end_angle = -90f

    public override fun onDraw(c: Canvas) {
        val width = c.width
        val max = (0 until 1)
                .map { Integer.parseInt(param["value"]).toFloat() }
                .sum()
        val radius = (width / 2 - 30).toFloat()
        val start_angle = -90f
        var end_angle: Float
        val x = radius + 15f
        val y = radius + 15f
        val valParam = Integer.parseInt(param["value"]).toFloat()
        end_angle = start_angle + 360 * (valParam / max)
        if (end_angle > _end_angle) {
            end_angle = _end_angle
        }
        this.createPieSlice(c, Integer.parseInt(param["color"]), start_angle, end_angle, x, y, radius)
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

    fun startAnimation(time: Long) {
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
        timer.schedule(task, 0, time)

    }

    fun changeParam(param: HashMap<String, String>) {
        this.param = param
        this.invalidate()
    }
}
