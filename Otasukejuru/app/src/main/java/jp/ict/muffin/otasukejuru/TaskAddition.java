package jp.ict.muffin.otasukejuru;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TaskAddition extends Fragment {

    //@Override
    public View onCreateView(Bundle savedInstanceState) {

        View fragment = new View(getActivity());

        TextView message = (TextView) fragment.findViewById(R.id.message);
        message.setText("何を追加しますか？");

        // はい ボタンのリスナ
        fragment.findViewById(R.id.plan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }

        });

        // いいえ ボタンのリスナ
        fragment.findViewById(R.id.task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        int a = 0;

        return fragment;
    }

}