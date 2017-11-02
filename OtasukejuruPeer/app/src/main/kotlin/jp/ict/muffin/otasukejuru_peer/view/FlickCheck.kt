package jp.ict.muffin.otasukejuru_peer.view

import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * frickViewにはフリックを検知させるViewをセット<br></br>
 * adjustXには左右のフリック距離目安、adjustYには上下のフリック距離目安をセット
 *
 * @param flickView
 * @param adjustX
 * @param adjustY
 */
internal abstract class FlickCheck(flickView: View?, adjustX: Float, adjustY: Float) {
    
    private var adjustX = 150.0f
    private var adjustY = 150.0f
    private var touchX: Float = 0.toFloat()
    private var touchY: Float = 0.toFloat()
    private var nowTouchX: Float = 0.toFloat()
    private var nowTouchY: Float = 0.toFloat()
    
    init {
        
        this.adjustX = adjustX
        this.adjustY = adjustY
        
        flickView?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchX = event.x
                    touchY = event.y
                    Log.d("hoge", "ACTION_DOWN")
                }
                MotionEvent.ACTION_UP -> {
                    nowTouchX = event.x
                    nowTouchY = event.y
                    Log.d("hoge", "ACTION_UP")
                    check()
                    v.performClick()
                }
                MotionEvent.ACTION_MOVE -> {
                }
                MotionEvent.ACTION_CANCEL -> {
                }
            }
            true
        }
    }
    
    /**
     * どの方向にフリックしたかチェック
     */
    private fun check() {
        Log.d("FlickPoint", "startX:" + touchX + " endX:" + nowTouchX
                + " startY:" + touchY + " endY:" + nowTouchY)
        // 左フリック
        if (nowTouchX < touchX && adjustX < touchX - nowTouchX) {
            getFlick(LEFT_FLICK)
            return
        }
        // 右フリック
        if (touchX < nowTouchX && adjustX < nowTouchX - touchX) {
            getFlick(RIGHT_FLICK)
            return
        }
        // 上フリック
        if (nowTouchY < touchY && adjustY < touchY - nowTouchY) {
            getFlick(UP_FLICK)
            return
        }
        // 下フリック
        if (touchY < nowTouchY && adjustY < nowTouchY - touchY) {
            getFlick(DOWN_FLICK)
        }
    }
    
    /**
     * 抽象メソッド：フリックを感知した際、方向を表す値をセットする
     *
     * @param swipe
     */
    abstract fun getFlick(swipe: Int)
    
    companion object {
        
        val LEFT_FLICK = 0
        val RIGHT_FLICK = 1
        private val UP_FLICK = 2
        private val DOWN_FLICK = 3
    }
    
}