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
        
        val map = HashMap<String, String>()
        map.put("color", Color.argb(255, 255, 0, 0).toString())
        map.put("value", "1")
        
        val circleGraphView = CircleGraphView(context, map)
        circle.addView(circleGraphView)
        
        circleGraphView.startAnimation(1)
        val map1 = HashMap<String, String>()
        map1.put("color", Color.argb(255, 255, 255, 255).toString())
        map1.put("value", "1")
    
        val circleGraphView1 = CircleGraphView(context, map1)
        circle.addView(circleGraphView1)
        circleGraphView1.startAnimation(36000)
    }
}