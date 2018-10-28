package jp.ict.muffin.otasukejuru.view

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

/**
 * Created by mito on 2018/07/19.
 */

@BindingAdapter("app:pager")
fun TabLayout.bindViewPagerTabs(pagerView: ViewPager) {
    this.setupWithViewPager(pagerView, true)
}
