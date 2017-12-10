package jp.ict.muffin.otasukejuru.activity

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.communication.GetInformation
import jp.ict.muffin.otasukejuru.communication.UpdateEveryInfoAsync
import jp.ict.muffin.otasukejuru.communication.UpdateScheduleInfoAsync
import jp.ict.muffin.otasukejuru.communication.UpdateTaskInfoAsync
import jp.ict.muffin.otasukejuru.fragment.ScheduleFragment
import jp.ict.muffin.otasukejuru.fragment.TaskListFragment
import jp.ict.muffin.otasukejuru.fragment.TimerSetTimeFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    private var mTimer: Timer? = null
    
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
        
        val mViewPager: ViewPager = find(R.id.view_pager)
        mViewPager.adapter = mSectionsPagerAdapter
        
        tabs.apply {
            setupWithViewPager(mViewPager)
            setTabTextColors(Color.parseColor("#FBFBF0"), Color.parseColor("#66B7EC"))
        }
        fab.setOnClickListener {
            startActivity<AdditionActivity>()
        }
        
        val display = windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        
        GlobalValue.apply {
            displayHeight = point.y
            displayWidth = point.x
            SERVER_URL = getString(R.string.server_url)
        }
        //TODO:Remove comment out when Communication
        val getInfo = GetInformation()
        getInfo.execute()
        
    }
    
    override fun onResume() {
        super.onResume()
        val mHandler = Handler()
        mTimer = Timer()
        mTimer?.schedule(object : TimerTask() {
            override fun run() {
                mHandler.post {
                    val getInformation = GetInformation()
                    getInformation.execute()
                    
                    GlobalValue.friendTaskInfoArrayList.forEachWithIndex { index, element ->
                        AlertDialog.Builder(applicationContext).apply {
                            setTitle(element.task_name)
                            setMessage(getString(R.string.changeMessage))
                            setPositiveButton("Yes") { _, _ ->
                                //ToDo:Write send function
                                val updateTask = UpdateTaskInfoAsync()
                                updateTask.execute(element)
                            }
                            setNegativeButton("No", null)
                            show()
                        }
                    }
                    
                    GlobalValue.friendScheduleInfoArrayList.forEachWithIndex { index, element ->
                        AlertDialog.Builder(applicationContext).apply {
                            setTitle(element.schedule_name)
                            setMessage(getString(R.string.changeMessage))
                            setPositiveButton("Yes") { _, _ ->
                                //ToDo:Write send function
                                val updateTask = UpdateScheduleInfoAsync()
                                updateTask.execute(element)
                            }
                            setNegativeButton("No", null)
                            show()
                        }
                    }
                    
                    GlobalValue.friendEveryInfoArrayList.forEachWithIndex { index, element ->
                        AlertDialog.Builder(applicationContext).apply {
                            setTitle(element.every_name)
                            setMessage(getString(R.string.changeMessage))
                            setPositiveButton("Yes") { _, _ ->
                                //ToDo:Write send function
                                val updateTask = UpdateEveryInfoAsync()
                                updateTask.execute(element)
                            }
                            setNegativeButton("No", null)
                            show()
                        }
                    }
                }
            }
        }, 5000, 5000)
        
    }
    
    override fun onPause() {
        super.onPause()
        mTimer?.cancel()
        mTimer = null
    }
    
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            if (item.itemId == R.id.move_to_top) {
                true
            } else {
                super.onOptionsItemSelected(item)
            }
    
    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private inner class SectionsPagerAdapter internal constructor(fm: FragmentManager) :
            FragmentPagerAdapter(fm) {
        
        //TODO : Show screen what was chosen.
        // Return a PlaceholderFragment (defined as a static inner class below).
        // getItem is called to instantiate the fragment for the given page.
        override fun getItem(position: Int): Fragment? = when (position) {
            0 -> ScheduleFragment()
            1 -> TaskListFragment()
            2 -> TimerSetTimeFragment()
            else -> null
        }
        
        // Show 3 total pages.
        override fun getCount(): Int = 3
        
        override fun getPageTitle(position: Int): CharSequence? = when (position) {
            0 -> getString(R.string.main_tab1)
            1 -> getString(R.string.main_tab2)
            2 -> getString(R.string.main_tab3)
            else -> null
        }
    }
}
