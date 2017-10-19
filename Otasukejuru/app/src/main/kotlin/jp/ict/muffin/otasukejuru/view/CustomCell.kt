package jp.ict.muffin.otasukejuru.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import jp.ict.muffin.otasukejuru.R


class CustomCell(context: Context, attr: AttributeSet) : LinearLayout(context, attr) {
    private val date: TextView? = null
    private val viewTask1: TextView
    private val viewTask2: TextView
    private val viewTask3: TextView
    
    init {
        
        val layout = LayoutInflater.from(context).inflate(R.layout.cell, this)
        
        viewTask1 = layout.findViewById(R.id.task_view1) as TextView
        viewTask2 = layout.findViewById(R.id.task_view2) as TextView
        viewTask3 = layout.findViewById(R.id.task_view3) as TextView
    }
    
}
