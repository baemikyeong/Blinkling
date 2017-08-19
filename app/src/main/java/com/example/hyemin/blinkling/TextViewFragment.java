package com.example.hyemin.blinkling;



import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyemin.blinkling.event.NeutralFaceEvent;
import com.example.hyemin.blinkling.event.RightEyeClosedEvent;
import com.example.hyemin.blinkling.tracker.FaceTracker;
import com.example.hyemin.blinkling.util.PlayServicesUtil;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;




/**
 * A simple {@link Fragment} subclass.
 *
 */
public class TextViewFragment extends Fragment {

    private static final int REQUEST_CAMERA_PERM = 69;      // 카메라 퍼미션을 위한 코드
    private TextView tv;                              // 텍스트 뷰를 띄워줄 뷰
    private ScrollView scrollView;                          // 텍스트 뷰를 스크롤 뷰를 이용해 화면에 출력
    private int[] location = new int[2];                    // 사용자가 현재 보고 있는 화면의 위치 저장
    private FaceDetector mFaceDetector;                     // 얼굴 인식
    private CameraSource mCameraSource;                     // 카메라 객체
    private FaceTracker face_tracker;                       // 눈 파악
    private double left_thres = 0;                          // 사용자의 초기값
    private double right_thres = 0;
    private SharedPreferences bookmarkPref;
    private SharedPreferences.Editor bookEdit;
    private int book_mark;
    private String bookName = "";



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            bookName = getArguments().getString("bookname");
        }
    }

    public TextViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_text_view, container, false);

        Activity root = getActivity();
//        Toast toast = Toast.makeText(root, bookName, Toast.LENGTH_SHORT);
//         toast.show();


        tv = (TextView)rootView.findViewById(R.id.txtview);
        scrollView = (ScrollView) rootView.findViewById(R.id.scroll_text);

        readTxt();


        return rootView;
    }



    private void readTxt() {

        File dir = Environment.getExternalStorageDirectory().getAbsoluteFile();
        //File yourFile = new File(dir, "path/to/the/file/inside/the/sdcard.ext");

        //Get the text file
        //Intent intent = getIntent();


        //  String bookName = getArguments().getString("bookname");
        //Toast.makeText(this, textName, Toast.LENGTH_SHORT).show();
        File file = new File(dir, bookName);
        // i have kept text.txt in the sd-card

        if (file.exists())   // check if file exist
        {
            //Read text from file
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('n');
                }
            } catch (IOException e) {
                //You'll need to add proper error handling here
            }
            //Set the text
            tv.setText(text);
        } else {
            tv.setText("Sorry file doesn't exist!!");
        }

    }















}
