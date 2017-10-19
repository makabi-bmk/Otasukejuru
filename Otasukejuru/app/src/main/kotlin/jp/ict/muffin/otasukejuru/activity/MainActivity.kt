package jp.ict.muffin.otasukejuru.activity

import android.graphics.Point
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.TextView
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.fragment.CalendarFragment
import jp.ict.muffin.otasukejuru.fragment.TimerSetTimeFragment
import jp.ict.muffin.otasukejuru.fragment.ToDoListFragment
import jp.ict.muffin.otasukejuru.view.HoldableViewPager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    private val globalValue = GlobalValue
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        
        val calendar = Calendar.getInstance()
        toolbar.title = (calendar.get(Calendar.MONTH) + 1).toString() + "月" +
                calendar.get(Calendar.DATE) + "日"
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*G
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        val mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        
        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        
        val mViewPager = findViewById(R.id.view_pager) as HoldableViewPager
        view_pager.adapter = mSectionsPagerAdapter
        view_pager.setSwipeHoldVar(true)
        
        tabs.setupWithViewPager(mViewPager)
        
        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            startActivity<TaskAdditionActivity>()
        }
        
        val display = windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        
        globalValue.displayHeight = point.y
        globalValue.displayWidth = point.x
        
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        
        
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
        
    }
    
    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {
        
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_main, container, false)
            val textView = rootView.findViewById(R.id.section_label) as TextView
            textView.text = getString(R.string.section_format, arguments.getInt(ARG_SECTION_NUMBER))
            return rootView
        }
        
        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"
            
            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
    
    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private inner class SectionsPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        
        //TODO : Show screen what was chosen.
        // Return a PlaceholderFragment (defined as a static inner class below).
        // getItem is called to instantiate the fragment for the given page.
        override fun getItem(position: Int): Fragment = when (position) {
            0 -> CalendarFragment()
            1 -> ToDoListFragment()
            2 -> TimerSetTimeFragment()
            else -> PlaceholderFragment.newInstance(position + 1)
        }
        
        // Show 3 total pages.
        override fun getCount(): Int = 3
        
        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return getString(R.string.main_tab1)
                1 -> return getString(R.string.main_tab2)
                2 -> return getString(R.string.main_tab3)
            }
            return null
        }
    }
}
