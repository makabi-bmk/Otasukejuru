package jp.ict.muffin.otasukejuru.ui.main

import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.communication.GetInformation
import jp.ict.muffin.otasukejuru.databinding.ActivityMainBinding
import jp.ict.muffin.otasukejuru.ui.schedule.ScheduleFragment
import jp.ict.muffin.otasukejuru.ui.task.list.TaskListFragment
import jp.ict.muffin.otasukejuru.ui.timer.TimerSetTimeFragment
import jp.ict.muffin.otasukejuru.utils.Utils
import org.jetbrains.anko.find
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import jp.ict.muffin.otasukejuru.ui.task.addition.AdditionActivity

class MainActivity : AppCompatActivity() {
    private var mTimer: Timer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_main
        )

        val calendar = Calendar.getInstance()
        setSupportActionBar(binding.toolbar)
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

        binding.apply {
            this.title = "${(calendar.get(Calendar.MONTH) + 1)}月${calendar.get(Calendar.DATE)}日"
            setOnFabClick {
//                startActivity<AdditionActivity>()
                AdditionActivity.start(
                        this@MainActivity,
                        isAdd = true
                )
            }
            tabs.setupWithViewPager(mViewPager)
        }

        val display = windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)

        GlobalValue.also {
            it.displayWidth = point.x
            it.SERVER_URL = getString(R.string.server_url)
        }
        loadInfoList()
        GetInformation().execute()
        showDialog(
                "レポート",
                DialogInterface.OnClickListener { _, _ ->
                }
        )
    }

    override fun onResume() {
        super.onResume()
        val mHandler = Handler()
        mTimer = Timer()
        mTimer?.schedule(
                object : TimerTask() {
                    override fun run() {
                        mHandler.post {
                            GlobalValue.friendEveryInfoArrayList.forEach {
                                val clickListener = DialogInterface.OnClickListener { _, _ ->
                                    GlobalValue.everyInfoArrayList.add(it)
                                }
                                showDialog(
                                        it.every_name,
                                        clickListener
                                )
                            }
                            GlobalValue.friendEveryInfoArrayList.clear()

                            GlobalValue.friendTaskInfoArrayList.forEach {
                                val clickListener = DialogInterface.OnClickListener { _, _ ->
                                    GlobalValue.taskInfoArrayList.add(it)
                                }
                                showDialog(
                                        it.task_name,
                                        clickListener
                                )
                            }
                            GlobalValue.taskInfoArrayList.clear()

                            GlobalValue.friendScheduleInfoArrayList.forEach {
                                val clickListener = DialogInterface.OnClickListener { _, _ ->
                                    GlobalValue.scheduleInfoArrayList.add(it)
                                }
                                showDialog(
                                        it.schedule_name,
                                        clickListener
                                )
                            }
                            GlobalValue.scheduleInfoArrayList.clear()
                        }
                    }
                },
                5000,
                5000
        )
    }

    fun showDialog(
        title: String,
        clickListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(this@MainActivity)
                .setTitle(title)
                .setMessage(getString(R.string.change_message))
                .setPositiveButton(
                        getString(R.string.ok),
                        clickListener
                )
                .setNegativeButton(
                        getString(R.string.cancel_en),
                        null
                )
                .show()
    }

    override fun onStop() {
        super.onStop()
        mTimer?.cancel()
        mTimer = null
    }
    private fun loadInfoList() {
        Utils().apply {
            parseData(
                    applicationContext,
                    loadString(
                            applicationContext,
                            getString(R.string.task_info_key)
                    ),
                    getString(R.string.task_info_key)
            )
            parseData(
                    applicationContext,
                    loadString(
                            applicationContext,
                            getString(R.string.schedule_info_key)
                    ),
                    getString(R.string.schedule_info_key)
            )
            parseData(
                    applicationContext,
                    loadString(
                            applicationContext,
                            getString(R.string.every_info_key)
                    ),
                    getString(R.string.every_info_key)
            )
        }
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private inner class SectionsPagerAdapter internal constructor(fm: FragmentManager) :
            FragmentPagerAdapter(fm) {

        // TODO : Show screen what was chosen.
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

        override fun getPageTitle(position: Int): CharSequence = when (position) {
            0 -> getString(R.string.main_tab1)
            1 -> getString(R.string.main_tab2)
            2 -> getString(R.string.main_tab3)
            else -> ""
        }
    }
}
