package jp.ict.muffin.otasukejuru;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Calendar;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class CalendarFragment extends Fragment {

    Calendar cal = Calendar.getInstance();
    TextView textView[] = new TextView[42];

    //カレンダーに表示される年月日
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView(view);

        float adjustX = 150.0f;
        float adjustY = 150.0f;

        //フリックされたときのイベント
        new FlickCheck(view, adjustX, adjustY) {

            @Override
            public void getFlick(int flickData) {
                switch (flickData) {
                    case FlickCheck.LEFT_FLICK:
                        // 左フリック
                        if (month == 1) year--;
                        else month--;
                        calendarView(view);
                        break;

                    case FlickCheck.RIGHT_FLICK:
                        // 右フリック
                        if (month == 12) year++;
                        else month++;
                        calendarView(view);
                        break;
                }
            }
        };


        return view;
    }

    void calendarView(View view){

        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        int width = disp.getWidth();
        int height = disp.getHeight();


        //タイトル(カレンダーの年月日)
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(String.valueOf(year) + "年" + String.valueOf(month + 1) + "月");

        GridLayout gridLayout = (GridLayout)view.findViewById(R.id.grid_layout);
        gridLayout.setPadding(0, 5, 0, 0);
        int backgroundColor = getResources().getColor(R.color.back);
        gridLayout.removeAllViews();

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
        for (int i = 1; i < firstWeekday; i++){
            textView[cnt]= new TextView(getContext());

            textView[cnt].setBackground(getResources().getDrawable(R.drawable.border));
            textView[cnt].setWidth(width / 7);
            textView[cnt].setHeight(height / 9);
            textView[cnt].setTextSize(COMPLEX_UNIT_SP, textFontSize);
            textView[cnt].setText("");
            gridLayout.addView(textView[i]);
            cnt++;
        }
        //日付の表示
        for (int date = 1; date <= lastDayOfMonth; date++){
            textView[cnt]= new TextView(getContext());
            textView[cnt].setBackground(getResources().getDrawable(R.drawable.border));
            textView[cnt].setWidth(width / 7);
            textView[cnt].setHeight(height / 9);
            textView[cnt].setTextSize(COMPLEX_UNIT_SP, textFontSize);
            textView[cnt].setTextSize(COMPLEX_UNIT_SP, textFontSize);
            textView[cnt].setTag(String.valueOf(date));

            //土日の日付に色を追加
            if (date % 7 == sunDay) textView[cnt].setTextColor(getResources().getColor(R.color.sundayColor));
            else if (date % 7 == saturDay) textView[cnt].setTextColor(getResources().getColor(R.color.saturdayColor));
            textView[cnt].setText(String.valueOf(date));
            gridLayout.addView(textView[cnt]);
            cnt++;
        }

        //空白スペースの表示
        for (int i = lastDayOfMonth + 1; i <= 42; i++){
            textView[cnt] = new TextView(getContext());
            textView[cnt].setBackground(getResources().getDrawable(R.drawable.border));
            textView[cnt].setWidth(width / 7);
            textView[cnt].setHeight(height / 9);
            textView[cnt].setTextSize(COMPLEX_UNIT_SP, textFontSize);
            textView[cnt].setText("");
            gridLayout.addView(textView[cnt]);
            cnt++;
        }


    }

}