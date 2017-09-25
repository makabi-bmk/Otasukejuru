package jp.ict.muffin.otasukejuru

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class ToDoListFragment : Fragment() {
    companion object {
        fun getInstance(): ToDoListFragment = ToDoListFragment()
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_list_todo, container, false)
}