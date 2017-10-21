package jp.ict.muffin.otasukejuru.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.ict.muffin.otasukejuru.ui.CalendarFragmentUI
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx


class CalendarFragment2 : Fragment() {
    companion object {
        fun getInstance(): CalendarFragment2 = CalendarFragment2()
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            CalendarFragmentUI().createView(AnkoContext.create(ctx, this))
}
