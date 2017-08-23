package jp.ict.muffin.otasukejuru

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by mito on 2017/08/23.
 */
public class TimerFragment : Fragment() {
    companion object {
        fun getInstance(): TimerFragment {
            return TimerFragment()
        }
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }
}