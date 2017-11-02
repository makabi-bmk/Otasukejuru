package jp.ict.muffin.otasukejuru_peer.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import jp.ict.muffin.otasukejuru_peer.R
import org.jetbrains.anko.find


class CustomCell(context: Context, attr: AttributeSet) : LinearLayout(context, attr) {
    private val date: TextView? = null
    private val viewTask1: TextView
    private val viewTask2: TextView
    private val viewTask3: TextView
    
    init {
        
        val layout = LayoutInflater.from(context).inflate(R.layout.cell, this)
        
        viewTask1 = layout.find<TextView>(R.id.schedule_view1)
        viewTask2 = layout.find<TextView>(R.id.schedule_view2)
        viewTask3 = layout.find<TextView>(R.id.schedule_view3)
    }
    
}
