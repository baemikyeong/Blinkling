package com.example.hyemin.blinkling.Setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyemin.blinkling.R;

import java.nio.channels.SeekableByteChannel;

public class SetBluelightFragment extends DialogFragment {

    private SharedPreferences intPref;
    private SharedPreferences.Editor editor1;

    public SetBluelightFragment() {

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
        builder.setMessage("블루라이트 제어 기능을 실행할 경우, 블루라이트의 정도를 조정합니다.");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_set_bluelight, null);

        final SeekBar bluelight_seekbar = (SeekBar) view.findViewById(R.id.seekBar);
        final TextView seekbar_gauge = (TextView) view.findViewById(R.id.textView3);

        // seekbar 설정

        final int[] nCurrent = {intPref.getInt("bluelight_edit",5)};
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

                        editor1.putInt("bluelight_edit",nCurrent[0]);
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


