package jp.ict.muffin.otasukejuru;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
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

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView(view);
        flickCheck(view);

        return view;
    }

    void calendarView(View view){
        //タイトル(表示されているカレンダーの年月日)
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(String.valueOf(year) + "年" + String.valueOf(month + 1) + "月");

        GridLayout gridLayout = (GridLayout)view.findViewById(R.id.grid_layout);
        int backgroundColor = getResources().getColor(R.color.back);

        //今日の年月日
        int todayYear = cal.get(Calendar.YEAR);
        int todayMonth = cal.get(Calendar.MONTH);
        int todayDay = cal.get(Calendar.DAY_OF_MONTH);

        cal.clear();
        cal.set(year, month, 1);

        //月末日
        int lastDayOfMonth = cal.getActualMaximum(Calendar.DATE);

        int firstWeekday = cal.get(Calendar.DAY_OF_WEEK);

        //第一土曜日の日
        int firstSaturday = 8 - firstWeekday;
        int saturDay = firstSaturday % 7;
        int sunDay = (firstSaturday + 1) % 7;


        int textFontSize = 25;

        for (int i = 1; i < firstWeekday; i++){
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.LEFT | Gravity.TOP);
            textView.setBackgroundColor(backgroundColor);
            textView.setTextSize(COMPLEX_UNIT_SP, textFontSize);
            textView.setText("");
            gridLayout.addView(textView);
        }

        for (int i = 1; i <= lastDayOfMonth; i++){
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.LEFT | Gravity.TOP);
            textView.setBackgroundColor(backgroundColor);
            textView.setTextSize(COMPLEX_UNIT_SP, textFontSize);
            //土日の日付に色を追加
            if (i % 7 == sunDay) textView.setTextColor(getResources().getColor(R.color.sundayColor));
            else if (i % 7 == saturDay) textView.setTextColor(getResources().getColor(R.color.saturdayColor));
            textView.setText(String.valueOf(i));
            gridLayout.addView(textView);
        }

        for (int i = lastDayOfMonth + 1; i <= 42; i++){
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.LEFT | Gravity.TOP);
            textView.setBackgroundColor(backgroundColor);
            textView.setTextSize(COMPLEX_UNIT_SP, textFontSize);
            textView.setText("");
            gridLayout.addView(textView);
        }


    }

    void flickCheck(View view){
        final View flickView = view;
        float adjustX = 150.0f;
        float adjustY = 150.0f;

        new FlickCheck(flickView, adjustX, adjustY) {
            @Override
            public void getFlick(int flickData) {
                switch (flickData) {
                    case FlickCheck.LEFT_FLICK:
                        // 左フリック
                        if(month == 11){
                            month = 0;
                            year++;
                        } else month++;
                        break;

                    case FlickCheck.RIGHT_FLICK:
                        // 右フリック
                        if(month == 0){
                            month = 11;
                            year--;
                        } else month--;
                        break;
                }


                calendarView(flickView);
            }

        };
    }

}