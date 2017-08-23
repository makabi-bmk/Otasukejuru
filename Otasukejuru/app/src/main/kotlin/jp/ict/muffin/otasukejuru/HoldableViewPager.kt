package jp.ict.muffin.otasukejuru

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by mito on 2017/08/23.
 */
class HoldableViewPager: android.support.v4.view.ViewPager {
    var isSwipeHoldVar = false
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    
    fun setSwipeHold(enable: Boolean) {
        isSwipeHoldVar = enable
    }
    
    override fun onTouchEvent(ev: MotionEvent?): Boolean = !isSwipeHoldVar && super.onTouchEvent(ev)
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean =
            !isSwipeHoldVar && super.onInterceptTouchEvent(ev)
}