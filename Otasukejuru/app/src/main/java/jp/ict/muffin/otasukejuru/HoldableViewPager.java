package jp.ict.muffin.otasukejuru;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HoldableViewPager extends ViewPager {
    private boolean isSwipeHoldVar = false;

    HoldableViewPager(Context context) {
        super(context);
    }

    HoldableViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setSwipeHoldVar(boolean swipeHoldVar) {
        isSwipeHoldVar = swipeHoldVar;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !isSwipeHoldVar && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSwipeHoldVar && super.onInterceptTouchEvent(ev);
    }
}
