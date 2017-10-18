package jp.ict.muffin.otasukejuru.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.view.View
import java.util.*


class CircleGraphView(context: Context, private var params: ArrayList<HashMap<String, Int>>, isInit: Boolean) : View(context) {
    internal var endAngleTmp = -90f
    internal lateinit var timer: Timer
    private val drawTime: Long = if (isInit) {
        1L
    } else {
        (params[0]["value"] ?: 0) * 60 * 100L
    }
    
    override fun onDraw(c: Canvas) {
        val width = c.width
        val length = params.size
        val max = (0 until length)
                .map { params[it]["value"]?.toFloat() ?: 0f }
                .sum()
        val radius = (width / 2 - 30).toFloat()
        var startAngle = -90f
        var endAngle: Float
        val x = radius + 15f
        val y = radius + 15f
        (0 until length).forEach {
            val value = params[it]["value"]?.toFloat() ?: 0f
            endAngle = startAngle + 360 * (value / max)
            if (endAngle > endAngleTmp) {
                endAngle = endAngleTmp
            }
            this.createPieSlice(c, params[it]["color"] ?: 0, startAngle, endAngle, x, y, radius)
            startAngle = endAngle
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
        paint.color = Color.argb(0, 0, 0, 0)
        paint.style = Paint.Style.STROKE
        c.drawArc(oval1, start_angle, end_angle - start_angle, true, paint)
        
    }
    
    fun startAnimation() {
        endAngleTmp = -90f
        val handler = Handler()
        val task = object : TimerTask() {
            override fun run() {
                val angle = (360 / 100.0).toFloat()
                endAngleTmp += angle
                if (endAngleTmp > 270) {
                    endAngleTmp = 270f
                    timer.cancel()
                }
                handler.post { invalidate() }
            }
        }
        
        timer = Timer()
        //アニメーションのスピード調整できるようにしたいところ
        timer.schedule(task, 0, drawTime + 1)
        
    }
    
}