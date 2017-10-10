package jp.ict.muffin.otasukejuru;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toolbar;

import java.util.Calendar;

import jp.ict.muffin.otasukejuru.Object.GlobalValue;


public class TaskAddition extends Activity {

    //common
    boolean isPlan;
    String taskTitleName;
    int startMonth, startDay, startHour, startMinute;
    int finishMonth, finishDay, finishHour, finishMinute;
    String taskRepeat;
    int dateLimit;
    int timeLimit;

    //plan
    int messageTime;
    //task
    boolean isMust, isShould, isWant;

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectAddType();
    }

    private void selectAddType() {
        setContentView(R.layout.selection);

        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        Button planButton = (Button) findViewById(R.id.button_plan);
        planButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlan = true;
                inputPlanName();
            }
        });

        Button taskButton = (Button) findViewById(R.id.button_task);
        taskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlan = false;
                inputTaskName();
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void inputPlanName() {
        setContentView(R.layout.input_plan_name);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        final EditText inputNameEdit = (EditText) findViewById(R.id.plan_name);

        Button nextButton = (Button) findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskTitleName = inputNameEdit.getText().toString();
                if (taskTitleName.equals("")) taskTitleName = "無題";

                startPlanTime();
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAddType();
            }
        });

    }

    //TODO:CHANGE XML
    private void startPlanTime() {
        setContentView(R.layout.start_plan_time);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        startMonth = calendar.get(Calendar.MONTH);
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        startHour = calendar.get(Calendar.HOUR_OF_DAY);
        startMinute = calendar.get(Calendar.MINUTE);

        NumberPicker startMonthNumPick = (NumberPicker) findViewById(R.id.start_month_num_pick);
        startMonthNumPick.setMaxValue(12);
        startMonthNumPick.setMinValue(1);
        startMonthNumPick.setValue(startMonth);
        startMonthNumPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                startMonth = newVal;
            }
        });

        NumberPicker startDayNumPick = (NumberPicker) findViewById(R.id.start_day_num_pick);
        startDayNumPick.setMaxValue(31);
        startDayNumPick.setMinValue(1);
        startDayNumPick.setValue(startDay);
        startDayNumPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                startDay = newVal;
            }
        });

        NumberPicker startHourNumPick = (NumberPicker) findViewById(R.id.start_hour_num_pick);
        startHourNumPick.setMaxValue(23);
        startHourNumPick.setMinValue(0);
        startHourNumPick.setValue(startHour);
        startHourNumPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                startHour = newVal;
            }
        });

        NumberPicker startMinuteNumPick = (NumberPicker) findViewById(R.id.start_minute_num_pick);
        startMinuteNumPick.setMaxValue(59);
        startMinuteNumPick.setMinValue(0);
        startMinuteNumPick.setValue(startMinute);
        startMinuteNumPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                startMinute = newVal;
            }
        });

        Button next = (Button) findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishPlanTime();
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPlanName();
            }
        });

    }

    private void finishPlanTime() {
        setContentView(R.layout.finish_plan_time);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        finishMonth = startMonth;
        finishDay = startDay;
        finishHour = startHour;
        finishMinute = startMinute;

        NumberPicker finishMonthNumPick = (NumberPicker) findViewById(R.id.finish_month_num_pick);
        finishMonthNumPick.setMaxValue(12);
        finishMonthNumPick.setMinValue(1);
        finishMonthNumPick.setValue(finishMonth);
        finishMonthNumPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                finishMonth = newVal;
            }
        });

        NumberPicker finishDayNumPick = (NumberPicker) findViewById(R.id.finish_day_num_pick);
        finishDayNumPick.setMaxValue(31);
        finishDayNumPick.setMinValue(1);
        finishDayNumPick.setValue(finishDay);
        finishDayNumPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                finishDay = newVal;
            }
        });

        NumberPicker finishHourNumPick = (NumberPicker) findViewById(R.id.finish_hour_num_pick);
        finishHourNumPick.setMaxValue(23);
        finishHourNumPick.setMinValue(0);
        finishHourNumPick.setValue(finishHour);
        finishHourNumPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                finishHour = newVal;
            }
        });

        NumberPicker finishMinuteNumPick = (NumberPicker) findViewById(R.id.finish_minute_num_pick);
        finishMinuteNumPick.setMaxValue(59);
        finishMinuteNumPick.setMinValue(0);
        finishMinuteNumPick.setValue(finishMinute);
        finishMinuteNumPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                finishMinute = newVal;
            }
        });

        Button next = (Button) findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlanRepeat();
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlanTime();
            }
        });

    }

    private void setPlanRepeat() {
        setContentView(R.layout.set_plan_repeat);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        final RadioGroup planRepeatRadioGroup = (RadioGroup) findViewById(R.id.plan_repeat_radio_group);

        Button next = (Button) findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = planRepeatRadioGroup.getCheckedRadioButtonId();

                if (num != -1) {
                    taskRepeat = String.valueOf(((RadioButton) findViewById(num)).getText());
                } else {
                    taskRepeat = "選択されていない";
                }
                setPlanMessageTime();
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishPlanTime();
            }
        });

    }

    private void setPlanMessageTime() {
        setContentView(R.layout.set_plan_message_time);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        final EditText setMessageTimeEdit = (EditText) findViewById(R.id.set_message_time_edit);
        setMessageTimeEdit.setText("5");
        messageTime = 5;

        Button next = (Button) findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str;
                str = setMessageTimeEdit.getText().toString();
                messageTime = Integer.parseInt(str);

                Log.d("plan", "タイトル名:" + taskTitleName + "\n予定開始の日付:" + startMonth + "月" + startDay + "日" + startHour + "時" + startMinute + "分"
                        + "\n予定終了の時間:" + finishMonth + "月" + finishMonth + "似り" + finishHour + "時" + finishMinute + "分" + "\n繰り返し:" + taskRepeat + "\n何分前に通知するか:" + messageTime);

                finish();
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlanRepeat();
            }
        });

    }

    private void inputTaskName() {
        setContentView(R.layout.input_task_name);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        final EditText inputTaskNameEdit = (EditText) findViewById(R.id.input_task_name_edit);

        Button next = (Button) findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskTitleName = inputTaskNameEdit.getText().toString();
                if (taskTitleName.equals("")) taskTitleName = "無題";

                finishTaskTime();
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAddType();
            }
        });
    }

    private void finishTaskTime() {
        setContentView(R.layout.finish_task_time);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        finishMonth = calendar.get(Calendar.MONTH);
        finishDay = calendar.get(Calendar.DAY_OF_MONTH);
        finishHour = calendar.get(Calendar.HOUR_OF_DAY);
        finishMinute = calendar.get(Calendar.MINUTE);

        NumberPicker finishMonthNumPick = (NumberPicker) findViewById(R.id.finish_month_num_pick);
        finishMonthNumPick.setMaxValue(12);
        finishMonthNumPick.setMinValue(1);
        finishMonthNumPick.setValue(finishMonth);
        finishMonthNumPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                finishMonth = newVal;
            }
        });

        NumberPicker finishDayNumPick = (NumberPicker) findViewById(R.id.finish_day_num_pick);
        finishDayNumPick.setMaxValue(31);
        finishDayNumPick.setMinValue(1);
        finishDayNumPick.setValue(finishDay);
        finishDayNumPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                finishDay = newVal;
            }
        });

        NumberPicker finishHourNumPick = (NumberPicker) findViewById(R.id.finish_hour_num_pick);
        finishHourNumPick.setMaxValue(23);
        finishHourNumPick.setMinValue(0);
        finishHourNumPick.setValue(finishHour);
        finishHourNumPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                finishHour = newVal;
            }
        });

        NumberPicker finishMinuteNumPick = (NumberPicker) findViewById(R.id.finish_minute_num_pick);
        finishMinuteNumPick.setMaxValue(59);
        finishMinuteNumPick.setMinValue(0);
        finishMinuteNumPick.setValue(finishMinute);
        finishMinuteNumPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                finishMinute = newVal;
            }
        });

        Button next = (Button) findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTaskRepeat();
            }
        });

        Button noLimit = (Button) findViewById(R.id.no_limit);
        noLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMonth = -1;
                setTaskRepeat();
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputTaskName();
            }
        });

    }

    private void setTaskRepeat() {
        setContentView(R.layout.set_task_repeat);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        final RadioGroup planRepeatRadioGroup = (RadioGroup) findViewById(R.id.plan_repeat_radio_group);

        Button next = (Button) findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = planRepeatRadioGroup.getCheckedRadioButtonId();

                if (num != -1) {
                    taskRepeat = String.valueOf(((RadioButton) findViewById(num)).getText());
                } else {
                    taskRepeat = "選択されてない";
                }
                setMust();
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishTaskTime();
            }
        });

    }

    private void setMust() {
        setContentView(R.layout.set_must);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        Button noMust = (Button) findViewById(R.id.no_must);
        noMust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMust = false;
                setShould();
            }
        });

        Button yesMust = (Button) findViewById(R.id.yes_must);
        yesMust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMust = true;
                setShould();
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTaskRepeat();
            }
        });
    }

    private void setShould() {
        setContentView(R.layout.set_should);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        Button noShould = (Button) findViewById(R.id.no_should);
        noShould.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShould = false;
                setWantTo();
            }
        });

        Button yesShould = (Button) findViewById(R.id.yes_should);
        yesShould.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShould = true;
                setWantTo();
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMust();
            }
        });

    }

    private void setWantTo() {
        setContentView(R.layout.set_want_to);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        Button noWantTo = (Button) findViewById(R.id.no_want_to);
        noWantTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWant = false;
                setTaskMessageTime();
            }
        });

        Button yes = (Button) findViewById(R.id.yes_w);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWant = true;
                setTaskMessageTime();
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.button_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShould();
            }
        });

    }

    private void setTaskMessageTime() {
        setContentView(R.layout.set_task_message_time);
        setActionBar((Toolbar) findViewById(R.id.toolbar_back));

        final EditText finishHourEdit = (EditText) findViewById(R.id.finish_hour_num_pick);
        finishHourEdit.setText("0");
        finishHour = 0;

        final EditText finishMinutesEdit = (EditText) findViewById(R.id.finish_minute_num_pick);
        finishMinutesEdit.setText("5");
        finishMinute = 5;

        Button next = (Button) findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishHour = Integer.parseInt(finishHourEdit.getText().toString());
                finishMinute = Integer.parseInt(finishMinutesEdit.getText().toString());

                if (startMonth == -1) {
                    dateLimit = -1;
                } else {
                    dateLimit = startMonth * 100 + startDay;
                    timeLimit = TaskAddition.this.startHour * 100 + startDay;
                }

                Log.d("task", "タイトル名:" + taskTitleName + "\n期限の開始:" + dateLimit + "\n繰り返し:" + taskRepeat
                        + "\nisMust:" + isMust + "\nisShould:" + isShould + "\nisWant to:" + isWant + "\n終了目安:" + finishHour + "時間" + finishMinute + "分");

                setTaskInformation();

                finish();
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWantTo();
            }
        });
    }

    void setTaskInformation() {
        TaskInformation taskInformation = new TaskInformation();
        taskInformation.setName(taskTitleName);
        taskInformation.setLimitDate(dateLimit);
        taskInformation.setLimitTime(timeLimit);
        taskInformation.setRepeat(taskRepeat);
        taskInformation.setMust(isMust);
        taskInformation.setShould(isShould);
        taskInformation.setWant(isWant);
        taskInformation.setFinishTimeMinutes(finishHour * 100 + finishMinute);
        taskInformation.setPriority(0);

        GlobalValue.INSTANCE.getTaskInformationArrayList().add(0, taskInformation);

    }
}