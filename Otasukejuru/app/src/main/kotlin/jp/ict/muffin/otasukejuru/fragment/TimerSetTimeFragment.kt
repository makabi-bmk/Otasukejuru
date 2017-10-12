package jp.ict.muffin.otasukejuru

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx


class TimerSetTimeFragment : Fragment() {
    companion object {
        fun getInstance(): TimerSetTimeFragment = TimerSetTimeFragment()
    }
    
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            TimerSetTimeFragmentUI().createView(AnkoContext.create(ctx, this))
}