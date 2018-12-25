package jp.ict.muffin.otasukejuru.ui.task.addition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.databinding.FragmentSelectAddTypeBinding

/**
 * Created by mito on 2018/07/24.
 */
class SelectAddTypeFragment : Fragment() {
    private lateinit var binding: FragmentSelectAddTypeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_select_add_type,
                container,
                false
        )

        return binding.root
    }
}
