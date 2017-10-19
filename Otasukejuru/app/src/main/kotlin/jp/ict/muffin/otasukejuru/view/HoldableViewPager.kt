package jp.ict.muffin.otasukejuru.view


import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class HoldableViewPager internal constructor(context: Context, attributeSet: AttributeSet) : ViewPager(context, attributeSet) {
    private var isSwipeHoldVar = false

    fun setSwipeHoldVar(swipeHoldVar: Boolean) {
        isSwipeHoldVar = swipeHoldVar
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean = !isSwipeHoldVar && super.onTouchEvent(ev)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean =
            isSwipeHoldVar && super.onInterceptTouchEvent(ev)
}
