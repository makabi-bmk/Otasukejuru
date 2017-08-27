package jp.ict.muffin.otasukejuru;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class TaskAddition extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection);

        Button plan = (Button) findViewById(R.id.plan);
        plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan1();
            }
        });

        Button task = (Button) findViewById(R.id.task);
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task1();
            }
        });
    }

    private void plan1(){
        setContentView(R.layout.plan1);
        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan2();
            }
        });

    }

    private void plan2(){
        setContentView(R.layout.plan2);

        NumberPicker numberPicker1 = (NumberPicker)findViewById(R.id.month);
        numberPicker1.setMaxValue(12);
        numberPicker1.setMinValue(1);

        NumberPicker numberPicker2 = (NumberPicker)findViewById(R.id.day);
        numberPicker2.setMaxValue(31);
        numberPicker2.setMinValue(1);

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan3();
            }
        });

    }
    private void plan3(){
        setContentView(R.layout.plan3);

        NumberPicker numberPicker1 = (NumberPicker)findViewById(R.id.start_hours);
        numberPicker1.setMaxValue(23);
        numberPicker1.setMinValue(0);

        NumberPicker numberPicker2 = (NumberPicker)findViewById(R.id.start_minits);
        numberPicker2.setMaxValue(59);
        numberPicker2.setMinValue(0);

        NumberPicker numberPicker3 = (NumberPicker)findViewById(R.id.finish_hours);
        numberPicker3.setMaxValue(23);
        numberPicker3.setMinValue(0);

        NumberPicker numberPicker4 = (NumberPicker)findViewById(R.id.finish_minits);
        numberPicker4.setMaxValue(59);
        numberPicker4.setMinValue(0);

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan4();
            }
        });

    }
    private void plan4(){
        setContentView(R.layout.plan4);
        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan5();
            }
        });

    }

    private void plan5(){
        setContentView(R.layout.plan5);
        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void task1(){
        setContentView(R.layout.task1);
        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task2();
            }
        });

    }

    private void task2(){
        setContentView(R.layout.task2);
        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan3();
            }
        });

        Button noPeriod = (Button) findViewById(R.id.no_period);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan3();
            }
        });

    }

    private void task3(){
        setContentView(R.layout.task3);
        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task4();
            }
        });

    }

    private void task4(){
        setContentView(R.layout.task4);
        Button no = (Button) findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task5();
            }
        });

        Button yes = (Button) findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task5();
            }
        });
    }

    private void task5(){
        setContentView(R.layout.task5);
        Button no = (Button) findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task6();
            }
        });

        Button yes = (Button) findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task6();
            }
        });

    }

    private void task6(){
        setContentView(R.layout.task6);
        Button no = (Button) findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task7();
            }
        });

        Button yes = (Button) findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task7();
            }
        });

    }

    private void task7(){
        setContentView(R.layout.task7);
        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
