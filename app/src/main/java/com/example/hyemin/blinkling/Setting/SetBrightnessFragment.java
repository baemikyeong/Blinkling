package com.example.hyemin.blinkling.Setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.hyemin.blinkling.R;

public class SetBrightnessFragment extends DialogFragment {

    private SharedPreferences intPref;
    private SharedPreferences.Editor editor1;

    public SetBrightnessFragment() {
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

        builder.setTitle("블루라이트 조절");
        builder.setMessage("블루라이트 제어 기능을 실핼할 경우, 어느정도의 밝기 조정을 할 지, 조정하는 기능입니다.");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_set_bluelight, null);

        final SeekBar bluelight_seekbar = (SeekBar) view.findViewById(R.id.seekBar);
        final TextView seekbar_gauge = (TextView) view.findViewById(R.id.textView3);

// seekbar 설정

        final int[] nCurrent = {intPref.getInt("bluelight_edit", 5)};
        bluelight_seekbar.setProgress(nCurrent[0]);
        seekbar_gauge.setText("gauge : " + nCurrent[0]);
        bluelight_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekbar_gauge.setText("gauge : " + nCurrent[0]);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekbar_gauge.setText("gauge : " + nCurrent[0]);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbar_gauge.setText("gauge : " + progress);
                nCurrent[0] = progress;

            }

        });


        builder.setView(view)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        editor1.putInt("bluelight_edit", nCurrent[0]);
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