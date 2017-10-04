package jp.ict.muffin.otasukejuru;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
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
        //flickCheck();

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

        int[] xml = new int[]{R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6,
                R.id.button7, R.id.button8, R.id.button9, R.id.button10, R.id.button11, R.id.button12, R.id.button13,
                R.id.button14, R.id.button15, R.id.button16, R.id.button17, R.id.button18, R.id.button19, R.id.button20,
                R.id.button21, R.id.button22, R.id.button23, R.id.button24, R.id.button25, R.id.button26, R.id.button27,
                R.id.button28, R.id.button29, R.id.button30, R.id.button31, R.id.button32, R.id.button33, R.id.button34,
                R.id.button35, R.id.button36, R.id.button37, R.id.button38, R.id.button39, R.id.button40, R.id.button41};

        Button[] button = new Button[42];
        for (int i = 0; i < 42; i++){
            button[i] = (Button) view.findViewById(xml[i]);
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
        int saturDay = firstSaturday % 7;
        int sunDay = (firstSaturday + 1) % 7;

        //日付の文字の大きさ(sp)
        int textFontSize = 25;


        int cnt = 0;
        //空白スペースの表示
        for (int i = 1; i < firstWeekday; i++) {
            button[cnt].setTextSize(COMPLEX_UNIT_SP, textFontSize);
            button[cnt].setTag("");
            button[cnt].setText("");
            // Listnerをセット
            button[cnt].setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("空")
                            .setPositiveButton("OK", null)
                            .show();
                }
            });

            cnt++;


        }

        //日付の表示
        for (int date = 1; date <= lastDayOfMonth; date++) {
            button[cnt].setTextSize(COMPLEX_UNIT_SP, textFontSize);
            button[cnt].setTag(String.valueOf(date));

            //土日の日付に色を追加
            if (date % 7 == sunDay)
                button[cnt].setTextColor(getResources().getColor(R.color.sundayColor));
            else if (date % 7 == saturDay)
                button[cnt].setTextColor(getResources().getColor(R.color.saturdayColor));
            button[cnt].setText(String.valueOf(date));

            // Listnerをセット
            button[date].setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Button Tapped: " + view.getTag().toString())
                            .setPositiveButton("OK", null)
                            .show();
                }
            });
            cnt++;
        }

        //空白スペースの表示
        for (int i = cnt; i < 42; i++) {
            button[cnt].setTextSize(COMPLEX_UNIT_SP, textFontSize);
            button[cnt].setText("");
            button[cnt].setTag("");
            // Listnerをセット
            button[cnt].setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("空")
                            .setPositiveButton("OK", null)
                            .show();
                }
            });
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
                        Log.d("hoge", "left");
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
                        Log.d("hoge", "right");
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
                        Log.d("hoge", "up");
                        break;

                    case FlickCheck.DOWN_FLICK:
                        // 下フリック
                        Log.d("hoge", "down");
                        break;

                }
            }
        };

    }

}