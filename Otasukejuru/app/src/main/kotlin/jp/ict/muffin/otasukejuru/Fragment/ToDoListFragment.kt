package jp.ict.muffin.otasukejuru

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_list_todo.*
import kotlinx.android.synthetic.main.taskCardView.view.*
import org.jetbrains.anko.support.v4.toast


class ToDoListFragment : Fragment() {
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_list_todo, container, false)
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (0..3).forEach { i ->
            (0..6).forEach { it ->
                val inflater: LayoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val linearLayout: LinearLayout = inflater.inflate(R.layout.taskCardView, null) as LinearLayout
                linearLayout.apply {
                    textBox.text = "9/2"
                    cardView.apply {
                        tag = it
                        setOnClickListener {
                            toast(it.tag.toString())
                        }
                    }
                }
                when (it) {
                    0 -> mostPriorityCardLinear
                    1 -> highPriorityCardLinear1
                    2 -> highPriorityCardLinear2
                    3 -> middlePriorityCardLinear1
                    4 -> middlePriorityCardLinear2
                    5 -> lowPriorityCardLinear1
                    else -> lowPriorityCardLinear2
                }.addView(linearLayout, i)
//                mostPriorityCardLinear.addView(linearLayout, it)
            }
        }
    }
}