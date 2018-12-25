package jp.ict.muffin.otasukejuru.fragment.timer

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.databinding.FragmentTimerSetTimeBinding
import jp.ict.muffin.otasukejuru.ui.timer.set.TimerSetTimeFragmentUI
import org.jetbrains.anko.AnkoContext

class TimerSetTimeFragment : Fragment(), TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        this.activity?.findViewById<Button>(R.id.nextButton)?.let {
            if (s.toString() != "") {
                it.setTextColor(ContextCompat.getColor(
                        context!!,
                        R.color.colorPrimary
                ))
                it.isEnabled = true
            } else {
                it.setTextColor(Color.argb(
                        0,
                        0,
                        0,
                        0
                ))
                it.isEnabled = false
            }
        }
    }

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTimerSetTimeBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_timer_set_time,
                container,
                false
        )
        
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
                view,
                savedInstanceState
        )

        activity?.findViewById<TextView>(R.id.setTimeEdit)?.addTextChangedListener(this)
    }
}
