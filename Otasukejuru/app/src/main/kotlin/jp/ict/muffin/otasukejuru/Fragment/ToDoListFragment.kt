package jp.ict.muffin.otasukejuru

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_list_todo.*
import kotlinx.android.synthetic.main.view_card.view.*
import org.jetbrains.anko.support.v4.toast


class ToDoListFragment : Fragment() {
    companion object {
        fun getInstance(): ToDoListFragment = ToDoListFragment()
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_list_todo, container, false)
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (0..3).forEach {
            val inflater: LayoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val linearLayout: LinearLayout = inflater.inflate(R.layout.view_card, null) as LinearLayout
            linearLayout.apply {
                textBox.text = "CardView$it"
                cardView.apply {
                    tag = it
                    setOnClickListener {
                        toast(it.tag.toString())
                    }
                }
            }
            cardLinear.addView(linearLayout, it)
        }
    }
}