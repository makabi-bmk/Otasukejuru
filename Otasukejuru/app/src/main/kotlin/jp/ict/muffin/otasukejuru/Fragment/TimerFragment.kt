package jp.ict.muffin.otasukejuru

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import jp.ict.muffin.otasukejuru.View.CircleGraphView
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.find


/**
 * Created by mito on 2017/08/23.
 */
class TimerFragment : Fragment() {
    companion object {
        fun getInstance(): TimerFragment = TimerFragment()
    }
    
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            TimerFragmentUI().createView(AnkoContext.create(ctx, this))
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        val circle = find<FrameLayout>(1)
        circle.backgroundColor = Color.argb(0, 0, 0, 0)
        
        val params = arrayListOf<HashMap<String, String>>()
        val map = HashMap<String, String>()
        map.put("color", Color.argb(255, 255, 0, 0).toString())
        map.put("value", "101")
        params.add(map)
        
        val circleGraphView = CircleGraphView(context, params)
        circle.addView(circleGraphView)
        
        circleGraphView.startAnimation()
    }
}