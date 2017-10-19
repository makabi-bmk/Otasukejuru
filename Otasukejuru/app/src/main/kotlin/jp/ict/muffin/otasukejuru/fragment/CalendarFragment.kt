package jp.ict.muffin.otasukejuru.fragment


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import jp.ict.muffin.otasukejuru.R
import jp.ict.muffin.otasukejuru.activity.DateActivity
import jp.ict.muffin.otasukejuru.view.FlickCheck
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*

class CalendarFragment : Fragment() {
    
    private var cal = Calendar.getInstance()
    
    //カレンダーに表示される年月日
    internal var year: Int = 0
    internal var month: Int = 0
    
    //第何月曜日・日曜日かカウントするための変数
    private var countMonday: Int = 0
    private var countSunday: Int = 0
    //春分の日、秋分の日
    internal var vernalEquinoxDay: Int = 0
    internal var autumnalEquinoxDay: Int = 0
    //振替休日かどうかを判断する
    private var substituteHoliday = false
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_calendar, container, false)
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH)
        
        calendarView()
        flickCheck()
    }
    
    fun calendarView() {
        
        //タイトル(カレンダーの年月日)
//        val title = view?.findViewById(R.id.title) as TextView
//        title.text = "${year}年${(month + 1)}月"
        title.text = "${year}年${month + 1}月"
        
        
        //月を切り替えるボタン
        val buttonNext = view!!.findViewById(R.id.next_month) as Button
        buttonNext.setOnClickListener {
            if (month == 11) {
                year++
                month = 0
            } else {
                month++
            }
            calendarView()
        }
        
        val buttonBack = view!!.findViewById(R.id.back_month) as Button
        buttonBack.setOnClickListener {
            if (month == 0) {
                year--
                month = 11
            } else {
                month--
            }
            calendarView()
        }
        
        val xml = intArrayOf(R.id.date0, R.id.date1, R.id.date2, R.id.date3, R.id.date4, R.id.date5, R.id.date6, R.id.date7, R.id.date8, R.id.date9, R.id.date10, R.id.date11, R.id.date12, R.id.date13, R.id.date14, R.id.date15, R.id.date16, R.id.date17, R.id.date18, R.id.date19, R.id.date20, R.id.date21, R.id.date22, R.id.date23, R.id.date24, R.id.date25, R.id.date26, R.id.date27, R.id.date28, R.id.date29, R.id.date30, R.id.date31, R.id.date32, R.id.date33, R.id.date34, R.id.date35, R.id.date36, R.id.date37, R.id.date38, R.id.date39, R.id.date40, R.id.date41)
        
        val layout = arrayOfNulls<View>(42)
        val textView = arrayOfNulls<TextView>(42)
        for (i in 0..41) {
            layout[i] = view?.findViewById(xml[i])
            textView[i] = layout[i]?.findViewById(R.id.date_view) as TextView
            textView[i]?.setTextColor(Color.BLACK)
            textView[i]?.isClickable = true
        }
        
        
        //今日の年月日
        val todayYear = cal.get(Calendar.YEAR)
        val todayMonth = cal.get(Calendar.MONTH)
        val todayDay = cal.get(Calendar.DAY_OF_MONTH)
        
        cal.clear()
        cal.set(year, month, 1)
        
        //月末日
        val lastDayOfMonth = cal.getActualMaximum(Calendar.DATE)
        //月の初めの曜日
        val firstWeekday = cal.get(Calendar.DAY_OF_WEEK)
        //TextViewの要素番号
        var num = 0
        
        //空白スペースの表示
        for (i in 1 until firstWeekday) {
            layout[num]?.isClickable = false
            textView[num]?.text = ""
            num++
        }
        
        //初期化
        countSunday = 0
        countMonday = countSunday
        //日付の表示
        for (date in 1..lastDayOfMonth) {
            //月曜日ならtrue
            var judgeMonDay = false
            //日曜日ならtrue
            var judgeSunDay = false
            
            
            //月曜日かどうか判定
            if (num % 7 == 1) {
                //第何月曜日かカウントする
                countMonday++
                judgeMonDay = true
            } else if (num % 7 == 0 || num == 0 && date == 1) {
                textView[num]?.setTextColor(ContextCompat.getColor(context,
                        R.color.sundayColor))
                countSunday++
                judgeSunDay = true
            } else if (num % 7 == 6) {
                textView[num]?.setTextColor(ContextCompat.getColor(context,
                        R.color.saturdayColor))
            }//土曜日かどうか判定
            //日曜日かどうか判定
            
            var addMessage = false
            //祝日・イベント名の取得
            var eventName = judgePublicHoliday(date, judgeSunDay, judgeMonDay)
            
            //祝日の判定
            if (eventName != "NotPublicHoliday") {
                //リバーシブルデイの判定
                if (eventName == "敬老の日" && date == 21) {
                    if (judgePublicHoliday(23, false, false) == "秋分の日") {
                        textView[num + 1]?.setTextColor(ContextCompat.getColor(context,
                                R.color.sundayColor))
                    }
                }
                textView[num]?.setTextColor(ContextCompat.getColor(context,
                        R.color.sundayColor))
                addMessage = true
            } else {
                eventName = judgeAnnualEvent(date, judgeSunDay)
                if (eventName != "NotUnusualEvent") {
                    addMessage = true
                }
            }//年間行事の判定
            
            textView[num]?.setText(date.toString())
            textView[num]?.setClickable(true)
            textView[num]?.setTextSize(20f)
            
            
            //タッチイベントの設定
            val finalEventName = eventName
            val finalAddMessage = addMessage
            layout[num]?.setOnClickListener(View.OnClickListener {
                //                    Log.d("hoge", "poyo");
                //                    if (finalAddMessage) {
                //                        new AlertDialog.Builder(getActivity())
                //                                .setMessage(String.valueOf(finalDate) + "日" + "\n" +
                //                                        finalEventName)
                //                                .setPositiveButton("OK", null)
                //                                .show();
                //                    } else {
                //                        new AlertDialog.Builder(getActivity())
                //                                .setMessage(String.valueOf(finalDate) + "日")
                //                                .setPositiveButton("OK", null)
                //                                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                //                                    @Override
                //                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                val intent = Intent(context, DateActivity::class.java)
                startActivity(intent)
                //                                    }
                //
                //                                    @Override
                //                                    public void onNothingSelected(AdapterView<?> parent) {
                //
                //                                    }
                //                                })
                //                                .show();
                //                    }
            })
            textView[num]?.setOnClickListener(View.OnClickListener {
                Log.d("hoge", "poyo")
                if (finalAddMessage) {
                    AlertDialog.Builder(activity)
                            .setMessage(date.toString() + "日" + "\n" +
                                    finalEventName)
                            .setPositiveButton("OK", null)
                            .show()
                } else {
                    AlertDialog.Builder(activity)
                            .setMessage(date.toString() + "日")
                            .setPositiveButton("OK", null)
                            .show()
                }
            })
            num++
        }
        
        //空白スペースの表示
        for (i in num..41) {
            textView[num]?.setText("")
            layout[num]?.setClickable(false)
            num++
            
        }
        
        flickCheck()
        
    }
    
    fun flickCheck() {
        val adjustX = 150.0f
        val adjustY = 150.0f
        
        object : FlickCheck(view, adjustX, adjustY) {
            
            override fun getFlick(flickData: Int) {
                when (flickData) {
                    LEFT_FLICK -> {
                        // 左フリック
                        Log.d("hoge", "左フリック")
                        if (month == 11) {
                            year++
                            month = 0
                            //年が変わったとき、春分の日と秋分の日を定義しなおす
                            vernalEquinoxDay = (20.8431 + 0.242194 * (year - 1980) - (year - 1980) / 4).toInt()
                            autumnalEquinoxDay = (23.2488 + 0.242194 * (year - 1980) - (year - 1980) / 4).toInt()
                            
                        } else {
                            month++
                        }
                        calendarView()
                    }
                    
                    RIGHT_FLICK -> {
                        // 右フリック
                        Log.d("hoge", "右フリック")
                        if (month == 0) {
                            year--
                            month = 11
                            //年が変わったとき、春分の日と秋分の日を定義しなおす
                            vernalEquinoxDay = (20.8431 + 0.242194 * (year - 1980) - (year - 1980) / 4).toInt()
                            autumnalEquinoxDay = (23.2488 + 0.242194 * (year - 1980) - (year - 1980) / 4).toInt()
                            
                        } else {
                            month--
                        }
                        calendarView()
                    }
                }
            }
        }
    }
    
    fun judgePublicHoliday(date: Int, judgeSunday: Boolean, judgeMonday: Boolean): String {
        
        val month = this.month + 1
        
        if (month == 1 && date == 1) {
            if (judgeSunday) substituteHoliday = true
            return "元日"
        } else if (month == 1 && countMonday == 1 && judgeMonday) {
            return "成人の日"
        } else if (month == 2 && date == 11) {
            if (judgeSunday) substituteHoliday = true
            return "建国記念日"
        } else if (month == 3 && date == vernalEquinoxDay) {
            if (judgeSunday) substituteHoliday = true
            return "春分の日"
        } else if (month == 4 && date == 29) {
            if (judgeSunday) substituteHoliday = true
            return "昭和の日"
        } else if (month == 5 && date == 3) {
            if (judgeSunday) substituteHoliday = true
            return "憲法記念日"
        } else if (month == 5 && date == 4) {
            if (judgeSunday) substituteHoliday = true
            return "みどりの日"
        } else if (month == 5 && date == 5) {
            if (judgeSunday) substituteHoliday = true
            return "こどもの日"
        } else if (month == 7 && countMonday == 3 && judgeMonday) {
            return "海の日"
        } else if (month == 9 && date == autumnalEquinoxDay) {
            if (judgeSunday) substituteHoliday = true
            return "秋分の日"
        } else if (month == 10 && countMonday == 2 && judgeMonday) {
            return "体育の日"
        } else if (month == 11 && date == 3) {
            if (judgeSunday) substituteHoliday = true
            return "文化の日"
        } else if (month == 11 && date == 23) {
            if (judgeSunday) substituteHoliday = true
            return "勤労感謝の日"
        } else if (month == 12 && date == 23) {
            if (judgeSunday) substituteHoliday = true
            return "天皇誕生日"
        } else if (substituteHoliday) {
            substituteHoliday = false
            return "振替休日"
        } else {
            return "NotPublicHoliday"
        }
    }
    
    fun judgeAnnualEvent(date: Int, judgeSunday: Boolean): String {
        
        val month = this.month + 1
        
        return if (month == 2 && date == 3) {
            "節分の日"
        } else if (month == 2 && date == 14) {
            "バレンタインデー"
        } else if (month == 3 && date == 3) {
            "ひなまつり"
        } else if (month == 3 && date == 14) {
            "ホワイトデー"
        } else if (month == 4 && date == 1) {
            "エイプリルフール"
        } else if (month == 5 && countSunday == 2 && judgeSunday) {
            "母の日"
        } else if (month == 6 && countSunday == 3 && judgeSunday) {
            "父の日"
        } else if (month == 7 && date == 7) {
            "七夕"
        } else if (month == 8 && date == 15) {
            "終戦記念日"
        } else if (month == 12 && date == 24) {
            "クリスマスイブ"
        } else if (month == 12 && date == 25) {
            "クリスマス"
        } else if (month == 12 && date == 31) {
            "大晦日"
        } else {
            "NotUnusualEvent"
        }
    }
}