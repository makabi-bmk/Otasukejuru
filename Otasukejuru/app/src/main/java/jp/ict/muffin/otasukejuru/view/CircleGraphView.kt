package jp.ict.muffin.otasukejuru.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.view.View
import java.util.*

@SuppressLint("ViewConstructor")
class CircleGraphView(
  context: Context,
  private var params: ArrayList<HashMap<String, Int>>,
  isInit: Boolean
) : View(context) {
  internal var endAngleTmp = -90f
  internal val timer: Timer by lazy { Timer() }
  private val drawTime: Long = if (isInit) {
    1L
  } else {
    (params[1]["value"] ?: 0) * 60 * 100L
  }

  override fun onDraw(c: Canvas) {
    val width = width
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
      if (endAngleTmp < endAngle) {
        endAngle = endAngleTmp
      }
      createPieSlice(
        c,
        params[it]["color"] ?: 0,
        startAngle,
        endAngle,
        x,
        y,
        radius
      )
      startAngle = endAngle
    }
  }

  private fun createPieSlice(
    c: Canvas,
    colorParams: Int,
    start_angle: Float,
    end_angle: Float,
    x: Float,
    y: Float,
    r: Float
  ) {
    val paint = Paint()
    paint.apply {
      isAntiAlias = false
      color = colorParams
    }
    val oval1 = RectF(
      x - r,
      y - r,
      x + r,
      y + r
    )
    c.drawArc(
      oval1,
      start_angle,
      end_angle - start_angle,
      true,
      paint
    )

    // 外枠
    val strokePaint = Paint()
    strokePaint.apply {
      color = Color.argb(
        0,
        0,
        0,
        0
      )
      style = Paint.Style.STROKE
    }
    c.drawArc(
      oval1,
      start_angle,
      end_angle - start_angle,
      true,
      paint
    )
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

    // アニメーションのスピード調整できるようにしたいところ
    timer.schedule(
      task,
      0,
      drawTime + 1
    )
  }
}
