package jp.ict.muffin.otasukejuru;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toolbar;
import java.util.Calendar;

public class TaskAddition extends Activity {

    //common
    boolean select;
    String name;
    int month, day, hour, minute;
    int finishHour, finishMinute;
    String repeat;

    //plan
    int messageTime;
    //task
    boolean must, should, want;

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selection();
    }

    private void selection(){
        setContentView(R.layout.selection);

        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        Button plan = (Button) findViewById(R.id.plan);
        plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = true;
                plan1();
            }
        });

        Button task = (Button) findViewById(R.id.task);
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = false;
                task1();
            }
        });

        ImageButton imageButton = (ImageButton)findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void plan1(){
        setContentView(R.layout.plan1);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        final EditText editText = (EditText) findViewById(R.id.plan_name);

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editText.getText().toString();
                if (name.equals("")) name = "無題";

                plan2();
            }
        });

        ImageButton imageButton = (ImageButton)findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection();
            }
        });

    }

    private void plan2(){
        setContentView(R.layout.plan2);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        NumberPicker numberPicker1 = (NumberPicker)findViewById(R.id.month);
        numberPicker1.setMaxValue(12);
        numberPicker1.setMinValue(1);
        numberPicker1.setValue(month);
        numberPicker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                month = newVal;
            }
        });

        NumberPicker numberPicker2 = (NumberPicker)findViewById(R.id.day);
        numberPicker2.setMaxValue(31);
        numberPicker2.setMinValue(1);
        numberPicker2.setValue(day);
        numberPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                day = newVal;
            }
        });

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan3();
            }
        });

        ImageButton imageButton = (ImageButton)findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan1();
            }
        });

    }

    private void plan3(){
        setContentView(R.layout.plan3);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        finishHour = hour = calendar.get(Calendar.HOUR_OF_DAY);
        finishMinute = minute = calendar.get(Calendar.MINUTE);

        NumberPicker numberPicker1 = (NumberPicker)findViewById(R.id.start_hour);
        numberPicker1.setMaxValue(23);
        numberPicker1.setMinValue(0);
        numberPicker1.setValue(hour);
        numberPicker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hour = newVal;
            }
        });

        NumberPicker numberPicker2 = (NumberPicker)findViewById(R.id.start_minute);
        numberPicker2.setMaxValue(59);
        numberPicker2.setMinValue(0);
        numberPicker2.setValue(minute);
        numberPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                minute = newVal;
            }
        });

        NumberPicker numberPicker3 = (NumberPicker)findViewById(R.id.finish_hour);
        numberPicker3.setMaxValue(23);
        numberPicker3.setMinValue(0);
        numberPicker3.setValue(finishHour);
        numberPicker3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                finishHour = newVal;
            }
        });

        NumberPicker numberPicker4 = (NumberPicker)findViewById(R.id.finish_minute);
        numberPicker4.setMaxValue(59);
        numberPicker4.setMinValue(0);
        numberPicker4.setValue(finishMinute);
        numberPicker4.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                finishMinute = newVal;
            }
        });

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan4();
            }
        });

        ImageButton imageButton = (ImageButton)findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan2();
            }
        });

    }

    private void plan4(){
        setContentView(R.layout.plan4);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radio_plan);

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = radioGroup.getCheckedRadioButtonId();

                if (num != -1){
                    repeat = String.valueOf(((RadioButton) findViewById(num)).getText());
                } else {
                    repeat = "選択されていない";
                }
                plan5();
            }
        });

        ImageButton imageButton = (ImageButton)findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan3();
            }
        });

    }

    private void plan5(){
        setContentView(R.layout.plan5);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        final EditText m = (EditText) findViewById(R.id.message_time);
        m.setText("5");
        messageTime = 5;

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str;
                str = m.getText().toString();
                messageTime = Integer.parseInt(str);

                AlertDialog.Builder alert = new AlertDialog.Builder(TaskAddition.this);
                alert.setMessage("タイトル名:" + name + "\n予定開始の日付:" + month + "月" + day + "日" + "\n予定開始の時間:" + hour + "時" + minute + "分"
                        + "\n予定終了の時間:" + finishHour + "時" + finishMinute + "分" + "\n繰り返し:" + repeat + "\n何分前に通知するか:" + messageTime).show();

                finish();
            }
        });

        ImageButton imageButton = (ImageButton)findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan4();
            }
        });

    }

    private void task1(){
        setContentView(R.layout.task1);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));
        
        final EditText editText = (EditText) findViewById(R.id.task_name); 

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editText.getText().toString();
                if (name.equals("")) name = "無題";

                task2();
            }
        });

        ImageButton imageButton = (ImageButton)findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection();
            }
        });
    }

    private void task2(){
        setContentView(R.layout.task2);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        NumberPicker numberPicker1 = (NumberPicker)findViewById(R.id.limit_month);
        numberPicker1.setMaxValue(12);
        numberPicker1.setMinValue(1);
        numberPicker1.setValue(month);
        numberPicker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                month = newVal;
            }
        });

        NumberPicker numberPicker2 = (NumberPicker)findViewById(R.id.limit_day);
        numberPicker2.setMaxValue(31);
        numberPicker2.setMinValue(1);
        numberPicker2.setValue(day);
        numberPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                day = newVal;
            }
        });

        NumberPicker numberPicker3 = (NumberPicker)findViewById(R.id.limit_hour);
        numberPicker3.setMaxValue(23);
        numberPicker3.setMinValue(0);
        numberPicker3.setValue(hour);
        numberPicker3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hour = newVal;
            }
        });

        NumberPicker numberPicker4 = (NumberPicker)findViewById(R.id.limit_minute);
        numberPicker4.setMaxValue(59);
        numberPicker4.setMinValue(0);
        numberPicker4.setValue(minute);
        numberPicker4.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                minute = newVal;
            }
        });

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task3();
            }
        });

        Button noPeriod = (Button) findViewById(R.id.no_period);
        noPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month = -1;
                task3();
            }
        });

        ImageButton imageButton = (ImageButton)findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task1();
            }
        });

    }

    private void task3(){
        setContentView(R.layout.task3);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_task);

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = radioGroup.getCheckedRadioButtonId();

                if (num != -1){
                    repeat = String.valueOf(((RadioButton) findViewById(num)).getText());
                } else {
                    repeat = "選択されてない";
                }
                task4();
            }
        });

        ImageButton imageButton = (ImageButton)findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task2();
            }
        });

    }

    private void task4(){
        setContentView(R.layout.task4);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        Button no = (Button) findViewById(R.id.no_m);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                must = false;
                task5();
            }
        });

        Button yes = (Button) findViewById(R.id.yes_m);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                must = true;
                task5();
            }
        });

        ImageButton imageButton = (ImageButton)findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task3();
            }
        });
    }

    private void task5(){
        setContentView(R.layout.task5);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        Button no = (Button) findViewById(R.id.no_s);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                should = false;
                task6();
            }
        });

        Button yes = (Button) findViewById(R.id.yes_s);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                should = true;
                task6();
            }
        });

        ImageButton imageButton = (ImageButton)findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task4();
            }
        });

    }

    private void task6(){
        setContentView(R.layout.task6);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        Button no = (Button) findViewById(R.id.no_w);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                want = false;
                task7();
            }
        });

        Button yes = (Button) findViewById(R.id.yes_w);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                want = true;
                task7();
            }
        });

        ImageButton imageButton = (ImageButton)findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task5();
            }
        });

    }

    private void task7(){
        setContentView(R.layout.task7);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        final EditText h = (EditText) findViewById(R.id.finish_hour);
        h.setText("0");
        finishHour = 0;

        final EditText m = (EditText) findViewById(R.id.finish_minute);
        m.setText("5");
        finishMinute = 5;

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str;
                str = h.getText().toString();
                finishHour = Integer.parseInt(str);

                str = m.getText().toString();
                finishMinute = Integer.parseInt(str);

                if (month == -1){
                    str = "期限なし";
                } else {
                    str = month + "月" + day + "日" + hour + "時" + day + "分";
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(TaskAddition.this);
                alert.setMessage("タイトル名:" + name + "\n期限の開始:" + str + "\n繰り返し:" + repeat
                                 + "\nmust:" + must + "\nshould:" + should + "\nwant to:" + want + "\n終了目安:" + finishHour + "時間" + finishMinute + "分").show();

                finish();
            }
        });

        ImageButton imageButton = (ImageButton)findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task6();
            }
        });
    }
}
