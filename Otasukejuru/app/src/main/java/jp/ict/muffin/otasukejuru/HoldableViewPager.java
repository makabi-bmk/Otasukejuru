package jp.ict.muffin.otasukejuru;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by mito on 2017/08/22.
 */

public class HoldableViewPager extends android.support.v4.view.ViewPager {
    boolean isSwipeHold_ = false;   // スワイプによるページ切り替えを抑制する

    /*
     * スワイプによるページ切り替え有効/無効設定
     */
    public void setSwipeHold(boolean enable) {
        isSwipeHold_ = enable;
    }

    public HoldableViewPager(Context context) {
        super(context);
    }

    public HoldableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !isSwipeHold_ && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return !isSwipeHold_ && super.onInterceptTouchEvent(event);
    }
}
