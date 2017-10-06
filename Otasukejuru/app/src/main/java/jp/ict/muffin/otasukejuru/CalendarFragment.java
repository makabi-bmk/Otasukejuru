package jp.ict.muffin.otasukejuru;


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

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class CalendarFragment extends Fragment {

    Calendar cal = Calendar.getInstance();
    private View view;

    //カレンダーに表示される年月日
    int year;
    int month;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

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

        int[] xml = new int[]{R.id.date0, R.id.date1, R.id.date2, R.id.date3, R.id.date4, R.id.date5, R.id.date6,
                R.id.date7, R.id.date8, R.id.date9, R.id.date10, R.id.date11, R.id.date12, R.id.date13,
                R.id.date14, R.id.date15, R.id.date16, R.id.date17, R.id.date18, R.id.date19, R.id.date20,
                R.id.date21, R.id.date22, R.id.date23, R.id.date24, R.id.date25, R.id.date26, R.id.date27,
                R.id.date28, R.id.date29, R.id.date30, R.id.date31, R.id.date32, R.id.date33, R.id.date34,
                R.id.date35, R.id.date36, R.id.date37, R.id.date38, R.id.date39, R.id.date40, R.id.date41};

        View[] layout = new View[42];
        TextView[] textView = new TextView[42];
        for (int i = 0; i < 42; i++){
            layout[i] = (View) view.findViewById(xml[i]);
            textView[i] = (TextView)layout[i].findViewById(R.id.date_view);
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
        //第一土曜日
        int firstSaturday = 8 - firstWeekday;
        //土日かどうか判別するための変数
        int saturday = firstSaturday % 7;
        int sunday = (firstSaturday + 1) % 7;

        //日付の文字の大きさ(sp)
        int textFontSize = 25;
        int cnt = 0;

        //空白スペースの表示
        for (int i = 1; i < firstWeekday; i++) {
            layout[cnt].setClickable(false);
            textView[cnt].setText("");
            cnt++;
        }

        //日付の表示
        for (int date = 1; date <= lastDayOfMonth; date++) {
            //textView[cnt].setTextSize(COMPLEX_UNIT_SP, textFontSize);
            //土日の日付に色を追加
            if (date % 7 == sunday)
                textView[cnt].setTextColor(getResources().getColor(R.color.sundayColor));
            else if (date % 7 == saturday)
                textView[cnt].setTextColor(getResources().getColor(R.color.saturdayColor));

            textView[cnt].setText(String.valueOf(date));
            textView[cnt].setClickable(true);

            final int finalDate = date;
            layout[cnt].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage(String.valueOf(finalDate) + "日")
                            .setPositiveButton("OK", null)
                            .show();
                }
            });
            cnt++;
        }

        //空白スペースの表示
        for (int i = cnt; i < 42; i++) {
            textView[cnt].setText("");
            layout[cnt].setClickable(false);
            cnt++;

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
                        if (month == 11){
                            year++;
                            month = 0;
                        } else {
                            month++;
                        }
                        calendarView();
                        break;

                    case FlickCheck.RIGHT_FLICK:
                        // 右フリック
                        Log.d("hoge", "右フリック");
                        if (month == 0){
                            year--;
                            month = 11;
                        } else {
                            month--;
                        }
                        calendarView();
                        break;

                    case FlickCheck.UP_FLICK:
                        // 上フリック
                        break;

                    case FlickCheck.DOWN_FLICK:
                        // 下フリック
                        break;
                }
            }
        };
    }


}