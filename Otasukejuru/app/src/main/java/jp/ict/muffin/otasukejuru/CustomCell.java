package jp.ict.muffin.otasukejuru;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CustomCell extends LinearLayout{
    private TextView date;
    private TextView viewTask1;
    private TextView viewTask2;
    private TextView viewTask3;

    public CustomCell(Context context, AttributeSet attr) {
        super(context, attr);

        View layout = LayoutInflater.from(context).inflate(R.layout.cell, this);

        viewTask1 = (TextView)layout.findViewById(R.id.task_view1);
        viewTask2 = (TextView)layout.findViewById(R.id.task_view2);
        viewTask3 = (TextView)layout.findViewById(R.id.task_view3);
    }

}
