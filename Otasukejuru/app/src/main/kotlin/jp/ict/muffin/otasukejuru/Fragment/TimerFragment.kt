package jp.ict.muffin.otasukejuru

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx


class TimerFragment : Fragment() {
    companion object {
        fun getInstance(): TimerFragment = TimerFragment()
    }
    
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            TimerFragmentUI().createView(AnkoContext.create(ctx, this))
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
    }
}