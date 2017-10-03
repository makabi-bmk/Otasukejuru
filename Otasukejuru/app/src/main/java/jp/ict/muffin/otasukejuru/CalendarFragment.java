package jp.ict.muffin.otasukejuru;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

    //カレンダーに表示される年月日
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView(view);

        return view;
    }

    void calendarView(View view) {

        //タイトル(カレンダーの年月日)
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(String.valueOf(year) + "年" + String.valueOf(month + 1) + "月");

        int[] ids = new int[]{R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6,
                R.id.button7, R.id.button8, R.id.button9, R.id.button10, R.id.button11, R.id.button12, R.id.button13,
                R.id.button14, R.id.button15, R.id.button16, R.id.button17, R.id.button18, R.id.button19, R.id.button20,
                R.id.button21, R.id.button22, R.id.button23, R.id.button24, R.id.button25, R.id.button26, R.id.button27,
                R.id.button28, R.id.button29, R.id.button30, R.id.button31, R.id.button32, R.id.button33, R.id.button34,
                R.id.button35, R.id.button36, R.id.button37, R.id.button38, R.id.button39, R.id.button40, R.id.button41};

        Button[] button = new Button[42];

        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.table_layout);

        for (int i = 0; i < 42; i++){
            button[i] = (Button) view.findViewById(ids[i]);
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
        for (int i = lastDayOfMonth + 1; i <= 42; i++) {
            button[cnt].setTextSize(COMPLEX_UNIT_SP, textFontSize);
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


//
//        GridLayout gridLayout = (GridLayout) view.findViewById(R.id.grid_layout);
//        gridLayout.setPadding(0, 5, 0, 0);
//        int backgroundColor = getResources().getColor(R.color.back);
//        gridLayout.removeAllViews();
//
//        //今日の年月日
//        int todayYear = cal.get(Calendar.YEAR);
//        int todayMonth = cal.get(Calendar.MONTH);
//        int todayDay = cal.get(Calendar.DAY_OF_MONTH);
//
//        cal.clear();
//        cal.set(year, month, 1);
//
//        //月末日
//        int lastDayOfMonth = cal.getActualMaximum(Calendar.DATE);
//        //月の初めの曜日
//        int firstWeekday = cal.get(Calendar.DAY_OF_WEEK);
//        //第一土曜日
//        int firstSaturday = 8 - firstWeekday;
//        //土日かどうか判別するための変数
//        int saturDay = firstSaturday % 7;
//        int sunDay = (firstSaturday + 1) % 7;
//
//        //日付の文字の大きさ(sp)
//        int textFontSize = 25;
//
//        int cnt = 0;
//        //空白スペースの表示
//        for (int i = 1; i < firstWeekday; i++) {
//            textView[cnt] = new TextView(getContext());
//
//            textView[cnt].setBackground(getResources().getDrawable(R.drawable.border));
//            textView[cnt].setWidth(width / 7);
//            textView[cnt].setHeight(height / 9);
//            textView[cnt].setTextSize(COMPLEX_UNIT_SP, textFontSize);
//            textView[cnt].setText("");
//            gridLayout.addView(textView[i]);
//            cnt++;
//        }
//        //日付の表示
//        for (int date = 1; date <= lastDayOfMonth; date++) {
//            textView[cnt] = new TextView(getContext());
//            textView[cnt].setBackground(getResources().getDrawable(R.drawable.border));
//            textView[cnt].setWidth(width / 7);
//            textView[cnt].setHeight(height / 9);
//            textView[cnt].setTextSize(COMPLEX_UNIT_SP, textFontSize);
//            textView[cnt].setTextSize(COMPLEX_UNIT_SP, textFontSize);
//            textView[cnt].setTag(String.valueOf(date));
//
//            //土日の日付に色を追加
//            if (date % 7 == sunDay)
//                textView[cnt].setTextColor(getResources().getColor(R.color.sundayColor));
//            else if (date % 7 == saturDay)
//                textView[cnt].setTextColor(getResources().getColor(R.color.saturdayColor));
//            textView[cnt].setText(String.valueOf(date));
//            gridLayout.addView(textView[cnt]);
//            cnt++;
//        }
//
//        //空白スペースの表示
//        for (int i = lastDayOfMonth + 1; i <= 42; i++) {
//            textView[cnt] = new TextView(getContext());
//            textView[cnt].setBackground(getResources().getDrawable(R.drawable.border));
//            textView[cnt].setWidth(width / 7);
//            textView[cnt].setHeight(height / 9);
//            textView[cnt].setTextSize(COMPLEX_UNIT_SP, textFontSize);
//            textView[cnt].setText("");
//            gridLayout.addView(textView[cnt]);
//            cnt++;
//        }
//


    }

}