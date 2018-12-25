package jp.ict.muffin.otasukejuru.ui.task.list

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.`object`.GlobalValue
import jp.ict.muffin.otasukejuru.model.TaskInfo
import jp.ict.muffin.otasukejuru.ui.task.AdditionActivity
import jp.ict.muffin.otasukejuru.ui.progress.InputProgressActivity
import jp.ict.muffin.otasukejuru.ui.timer.set.TimeSetActivity
import jp.ict.muffin.otasukejuru.communication.DeleteTaskInfoAsync
import jp.ict.muffin.otasukejuru.utils.Utils
import kotlinx.android.synthetic.main.fragment_list_todo.highPriorityCardLinear1
import kotlinx.android.synthetic.main.fragment_list_todo.highPriorityCardLinear2
import kotlinx.android.synthetic.main.fragment_list_todo.lowPriorityCardLinear1
import kotlinx.android.synthetic.main.fragment_list_todo.lowPriorityCardLinear2
import kotlinx.android.synthetic.main.fragment_list_todo.middlePriorityCardLinear1
import kotlinx.android.synthetic.main.fragment_list_todo.middlePriorityCardLinear2
import kotlinx.android.synthetic.main.fragment_list_todo.mostPriorityCardLinear
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.dip
import org.jetbrains.anko.find
import org.jetbrains.anko.textColor
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask

class TaskListFragment : Fragment() {
    private var mTimer: Timer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
            TaskListFragmentUI().createView(AnkoContext.create(
                    context!!,
                    this
            ))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setCardView()
    }

    override fun onResume() {
        super.onResume()
        val mHandler = Handler()
        mTimer = Timer()
        mTimer?.schedule(
                object : TimerTask() {
                    override fun run() {
                        mHandler.post {
                            setCardView()
                        }
                    }
                },
                5000,
                5000
        )
    }

    override fun onStop() {
        super.onStop()
        mTimer?.cancel()
        mTimer = null
    }

    @SuppressLint("InflateParams")
    fun setCardView() {
        (0..6).forEach {
            when (it) {
                0 -> mostPriorityCardLinear
                1 -> highPriorityCardLinear1
                2 -> highPriorityCardLinear2
                3 -> middlePriorityCardLinear1
                4 -> middlePriorityCardLinear2
                5 -> lowPriorityCardLinear1
                else -> lowPriorityCardLinear2
            }?.removeAllViews()
        }

        var mostPriorityNum = 0
        var highPriorityNum = 0
        var middlePriorityNum = 0
        var lowPriorityNum = 0
        val calendar = Calendar.getInstance()
        val today = (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH)

        val showTaskNum = GlobalValue.displayWidth / 90 - 1
        GlobalValue.taskInfoArrayList.forEachWithIndex { index, element ->
            val diffDays = Utils().diffDayNum(
                    today,
                    Utils().getDate(element.due_date),
                    calendar.get(Calendar.YEAR)
            )

            val inflater: LayoutInflater =
                    context?.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val cardLinearLayout: LinearLayout = inflater.inflate(
                    R.layout.task_card_view,
                    null
            ) as LinearLayout

            cardLinearLayout.apply {
                find<TextView>(R.id.dateTextView).apply {
                    this.text = diffDays.toString()
                    if (
                            element.priority == 0 ||
                            (diffDays == 1 || diffDays == 0)
                    ) {
                        this.textColor = ContextCompat.getColor(
                                context,
                                R.color.mostPriority
                        )
                    }
                }
                find<TextView>(R.id.taskNameTextView).text = element.task_name
                tag = Utils().getDate(element.due_date)
                setOnClickListener {
                    createDialog(
                            element,
                            index
                    )
                }
                find<FrameLayout>(R.id.taskProgress).scaleY = element.progress / 100f * dip(47)
            }

            val position: Int
            when (element.priority) {
                0 -> {
                    position = mostPriorityNum++ % showTaskNum
                    mostPriorityCardLinear
                }

                1 -> {
                    position = highPriorityNum++ % showTaskNum
                    if (highPriorityNum <= showTaskNum) {
                        highPriorityCardLinear1
                    } else {
                        highPriorityCardLinear2
                    }
                }

                2 -> {
                    position = middlePriorityNum++ % showTaskNum
                    if (middlePriorityNum <= showTaskNum) {
                        middlePriorityCardLinear1
                    } else {
                        middlePriorityCardLinear2
                    }
                }

                else -> {
                    position = lowPriorityNum++ % showTaskNum
                    if (lowPriorityNum <= showTaskNum) {
                        lowPriorityCardLinear1
                    } else {
                        lowPriorityCardLinear2
                    }
                }
            }.addView(cardLinearLayout, position)
        }
    }

    private fun createDialog(
            element: TaskInfo,
            index: Int
    ) {
        val listDialog = arrayOf(
                getString(R.string.start),
                getString(R.string.change),
                getString(R.string.complete),
                getString(R.string.delete),
                getString(R.string.progress)
        )

        AlertDialog.Builder(context).apply {
            setTitle(element.task_name)
            setItems(listDialog) { _, which ->
                when (which) {
                    0 -> {
                        TimeSetActivity.start(
                                this@TaskListFragment.context,
                                index = index
                        )
                    }

                    1 -> {
                        AdditionActivity.start(
                                this@TaskListFragment.context,
                                isAdd = false,
                                isTask = true,
                                index = index
                        )
                    }

                    2 -> {
                        AlertDialog.Builder(context).apply {
                            setTitle(element.task_name)
                            setMessage(getString(R.string.complicated_massage))
                            setPositiveButton(getString(R.string.yes_en)) { _, _ ->
                                deleteTask(element)
                            }
                            setNegativeButton(
                                    getString(R.string.no_en),
                                    null
                            )
                            show()
                        }
                    }

                    3 -> {
                        AlertDialog.Builder(context).apply {
                            setTitle(element.task_name)
                            setMessage(getString(R.string.delete_massage))
                            setPositiveButton(getString(R.string.ok)) { _, _ ->
                                deleteTask(element)
                            }
                            setNegativeButton(getString(
                                    R.string.cancel_en),
                                    null
                            )
                            show()
                        }
                    }

                    4 -> {
                        InputProgressActivity.start(
                                this@TaskListFragment.context,
                                index = index
                        )
                    }

                    else -> {
                    }
                }
            }
            show()
        }
    }

    private fun deleteTask(element: TaskInfo) {
        DeleteTaskInfoAsync().execute(element)

        try {
            GlobalValue.taskInfoArrayList.remove(element)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Utils().saveTaskInfoList(context)
    }
}
