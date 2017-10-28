package jp.ict.muffin.otasukejuru.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.ui.TimerSetTimeFragmentUI
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.textColor


class TimerSetTimeFragment : Fragment(), TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        find<Button>(R.id.nextButton).apply {
            if (s.toString() != "") {
                
                textColor = ContextCompat.getColor(context, R.color.colorPrimary)
                isEnabled = true
            } else {
                textColor = Color.argb(0, 0, 0, 0)
                isEnabled = false
            }
        }
    }
    
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }
    
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
    
    companion object {
        fun getInstance(): TimerSetTimeFragment = TimerSetTimeFragment()
    }
    
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            TimerSetTimeFragmentUI().createView(AnkoContext.create(ctx, this))
    
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        find<TextView>(R.id.setTimeEdit).addTextChangedListener(this)
    }
}