package com.example.hyemin.blinkling.Setting;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.hyemin.blinkling.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetBackgroundFragment extends DialogFragment {

    private SharedPreferences intPref;
    private SharedPreferences.Editor editor1;


    public SetBackgroundFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        intPref = this.getActivity().getSharedPreferences("mPred", Activity.MODE_PRIVATE);
        editor1 = intPref.edit();

    }


    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("배경색 지정");
        builder.setMessage("문서를 볼 때의 배경색을 지정합니다.");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_set_background, null);

        // seekbar 설정

        final int[] nCurrent = {intPref.getInt("background", 5)};

        builder.setView(view)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        editor1.putInt("background", nCurrent[0]);
                        editor1.commit();

                    }

                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        /* User clicked Cancel so do some stuff */
                    }
                });

        return builder.create();

    }
}


