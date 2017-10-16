package jp.ict.muffin.otasukejuru;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

public class CalendarFragment extends Fragment {

    Calendar cal = Calendar.getInstance();
    private View view;

    //カレンダーに表示される年月日
    int year;
    int month;

    //第何月曜日・日曜日かカウントするための変数
    int countMonday, countSunday;
    //春分の日、秋分の日
    int vernalEquinoxDay, autumnalEquinoxDay;
    //振替休日かどうかを判断する
    boolean substituteHoliday = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_calendar, container, false);

        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);

        calendarView();
        flickCheck();

        return view;
    }

    public void calendarView() {

        //タイトル(カレンダーの年月日)
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(String.valueOf(year) + "年" + String.valueOf(month + 1) + "月");


        //月を切り替えるボタン

//        Button buttonNext = (Button) view.findViewById(R.id.next_month);
//        buttonNext.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                if (month == 11){
//                    year++;
//                    month = 0;
//                } else {
//                    month++;
//                }
//                calendarView();
//            }
//        });
//
//        Button buttonBack = (Button) view.findViewById(R.id.back_month);
//        buttonBack.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                if (month == 0){
//                    year--;
//                    month = 11;
//                } else {
//                    month--;
//                }
//                calendarView();
//            }
//        });

        int[] xml = new int[]{R.id.date0, R.id.date1, R.id.date2, R.id.date3, R.id.date4,
                R.id.date5, R.id.date6, R.id.date7, R.id.date8, R.id.date9, R.id.date10,
                R.id.date11, R.id.date12, R.id.date13, R.id.date14, R.id.date15, R.id.date16,
                R.id.date17, R.id.date18, R.id.date19, R.id.date20, R.id.date21, R.id.date22,
                R.id.date23, R.id.date24, R.id.date25, R.id.date26, R.id.date27, R.id.date28,
                R.id.date29, R.id.date30, R.id.date31, R.id.date32, R.id.date33, R.id.date34,
                R.id.date35, R.id.date36, R.id.date37, R.id.date38, R.id.date39, R.id.date40,
                R.id.date41};

        View[] layout = new View[42];
        TextView[] textView = new TextView[42];
        for (int i = 0; i < 42; i++) {
            layout[i] = view.findViewById(xml[i]);
            textView[i] = (TextView) layout[i].findViewById(R.id.date_view);
            textView[i].setTextColor(Color.BLACK);
        }


        //今日の年月日
        int todayYear = cal.get(Calendar.YEAR);
        int todayMonth = cal.get(Calendar.MONTH);
        int todayDay = cal.get(Calendar.DAY_OF_MONTH);

        cal.clear();
        cal.set(year, month, 1);

        //月末日
        int lastDayOfMonth = cal.getActualMaximum(Calendar.DATE);
        //月の初めの曜日
        int firstWeekday = cal.get(Calendar.DAY_OF_WEEK);
        //TextViewの要素番号
        int num = 0;

        //空白スペースの表示
        for (int i = 1; i < firstWeekday; i++) {
            layout[num].setClickable(false);
            textView[num].setText("");
            num++;
        }

        //初期化
        countMonday = countSunday = 0;
        //日付の表示
        for (int date = 1; date <= lastDayOfMonth; date++) {
            //月曜日ならtrue
            boolean judgeMonDay = false;
            //日曜日ならtrue
            boolean judgeSunDay = false;


            //月曜日かどうか判定
            if (num % 7 == 1) {
                //第何月曜日かカウントする
                countMonday++;
                judgeMonDay = true;
            }
            //日曜日かどうか判定
            else if (num % 7 == 0 || (num == 0 && date == 1)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView[num].setTextColor(getContext().getColor(R.color.sundayColor));
                }
                countSunday++;
                judgeSunDay = true;
            }
            //土曜日かどうか判定
            else if (num % 7 == 6) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView[num].setTextColor(getContext().getColor(R.color.saturdayColor));
                }
            }

            boolean addMessage = false;
            //祝日・イベント名の取得
            String eventName = judgePublicHoliday(date, judgeSunDay, judgeMonDay);

            //祝日の判定
            if (!(eventName.equals("NotPublicHoliday"))) {
                //リバーシブルデイの判定
                if (eventName.equals("敬老の日") && date == 21) {
                    if ((judgePublicHoliday(23, false, false)).equals("秋分の日")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textView[num + 1].
                                    setTextColor(getContext().getColor(R.color.sundayColor));
                        }
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView[num].setTextColor(getContext().getColor(R.color.sundayColor));
                }
                addMessage = true;
            }
            //年間行事の判定
            else {
                eventName = judgeUnsualEvent(date, judgeSunDay);
                if (!(eventName.equals("NotUnusualEvent"))) {
                    addMessage = true;
                }
            }

            textView[num].setText(String.valueOf(date));
            textView[num].setClickable(true);

            //タッチイベントの設定
            final int finalDate = date;
            final String finalEventName = eventName;
            final boolean finalAddMessage = addMessage;
            layout[num].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalAddMessage) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage(String.valueOf(finalDate) + "日" + "\n" +
                                        finalEventName)
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setMessage(String.valueOf(finalDate) + "日")
                                .setPositiveButton("OK", null)
                                .show();
                    }

                }
            });
            num++;
        }

        //空白スペースの表示
        for (int i = num; i < 42; i++) {
            textView[num].setText("");
            layout[num].setClickable(false);
            num++;

        }

        flickCheck();

    }

    public void flickCheck() {
        float adjustX = 150.0f;
        float adjustY = 150.0f;

        new FlickCheck(view, adjustX, adjustY) {

            @Override
            public void getFlick(int flickData) {
                switch (flickData) {
                    case FlickCheck.LEFT_FLICK:
                        // 左フリック
                        Log.d("hoge", "左フリック");
                        if (month == 11) {
                            year++;
                            month = 0;
                            //年が変わったとき、春分の日と秋分の日を定義しなおす
                            vernalEquinoxDay = (int) (20.8431 + 0.242194 * (year - 1980) -
                                    (year - 1980) / 4);
                            autumnalEquinoxDay = (int) (23.2488 + 0.242194 * (year - 1980) -
                                    (year - 1980) / 4);

                        } else {
                            month++;
                        }
                        calendarView();
                        break;

                    case FlickCheck.RIGHT_FLICK:
                        // 右フリック
                        Log.d("hoge", "右フリック");
                        if (month == 0) {
                            year--;
                            month = 11;
                            //年が変わったとき、春分の日と秋分の日を定義しなおす
                            vernalEquinoxDay = (int) (20.8431 + 0.242194 * (year - 1980) -
                                    (year - 1980) / 4);
                            autumnalEquinoxDay = (int) (23.2488 + 0.242194 * (year - 1980) -
                                    (year - 1980) / 4);

                        } else {
                            month--;
                        }
                        calendarView();
                        break;

                }
            }
        };
    }

    public String judgePublicHoliday(int date, boolean judgeSunday, boolean judgeMonday) {

        int month = this.month + 1;

        if (month == 1 && date == 1) {
            if (judgeSunday) substituteHoliday = true;
            return "元日";
        } else if (month == 1 && countMonday == 1 && judgeMonday) {
            return "成人の日";
        } else if (month == 2 && date == 11) {
            if (judgeSunday) substituteHoliday = true;
            return "建国記念日";
        } else if (month == 3 && date == vernalEquinoxDay) {
            if (judgeSunday) substituteHoliday = true;
            return "春分の日";
        } else if (month == 4 && date == 29) {
            if (judgeSunday) substituteHoliday = true;
            return "昭和の日";
        } else if (month == 5 && date == 3) {
            if (judgeSunday) substituteHoliday = true;
            return "憲法記念日";
        } else if (month == 5 && date == 4) {
            if (judgeSunday) substituteHoliday = true;
            return "みどりの日";
        } else if (month == 5 && date == 5) {
            if (judgeSunday) substituteHoliday = true;
            return "こどもの日";
        } else if (month == 7 && countMonday == 3 && judgeMonday) {
            return "海の日";
        } else if (month == 9 && date == autumnalEquinoxDay) {
            if (judgeSunday) substituteHoliday = true;
            return "秋分の日";
        } else if (month == 10 && countMonday == 2 && judgeMonday) {
            return "体育の日";
        } else if (month == 11 && date == 3) {
            if (judgeSunday) substituteHoliday = true;
            return "文化の日";
        } else if (month == 11 && date == 23) {
            if (judgeSunday) substituteHoliday = true;
            return "勤労感謝の日";
        } else if (month == 12 && date == 23) {
            if (judgeSunday) substituteHoliday = true;
            return "天皇誕生日";
        } else if (substituteHoliday) {
            substituteHoliday = false;
            return "振替休日";
        } else {
            return "NotPublicHoliday";
        }
    }

    public String judgeUnsualEvent(int date, boolean judgeSunday) {

        int month = this.month + 1;

        if (month == 2 && date == 3) {
            return "節分の日";
        } else if (month == 2 && date == 14) {
            return "バレンタインデー";
        } else if (month == 3 && date == 3) {
            return "ひなまつり";
        } else if (month == 3 && date == 14) {
            return "ホワイトデー";
        } else if (month == 4 && date == 1) {
            return "エイプリルフール";
        } else if (month == 5 && countSunday == 2 && judgeSunday) {
            return "母の日";
        } else if (month == 6 && countSunday == 3 && judgeSunday) {
            return "父の日";
        } else if (month == 7 && date == 7) {
            return "七夕";
        } else if (month == 8 && date == 15) {
            return "終戦記念日";
        } else if (month == 12 && date == 24) {
            return "クリスマスイブ";
        } else if (month == 12 && date == 25) {
            return "クリスマス";
        } else if (month == 12 && date == 31) {
            return "大晦日";
        } else {
            return "NotUnusualEvent";
        }
    }
}