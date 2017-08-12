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

import static android.support.v4.widget.EdgeEffectCompatIcs.finish;


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
        tv.getLocationOnScreen(location);

        bookmarkPref = getSharedPreferences("bookPred", Activity.MODE_PRIVATE);
        bookEdit = bookmarkPref.edit();

        PlayServicesUtil.isPlayServicesAvailable(getActivity(), 69);//this를 getActivity()로바꿈

        readTxt();//사용자가 선택한 텍스트를 불러오는함수



        // permission granted...?
        if (isCameraPermissionGranted()) {
            // ...create the camera resource
            createCameraResources();
        } else {
            // ...else request the camera permission
            requestCameraPermission();
        }

        // 사용자가 화면을 터치하여 스크롤 뷰의 위치 변경시, 체크
        scrollView.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                tv.getLocationOnScreen(location);
                return false;
            }
        });


        return rootView;
    }



    private void readTxt() {

        File dir = Environment.getRootDirectory().getAbsoluteFile();
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


    public void change_down_location(){
        // 절대값을 통해 text뷰의 스크롤뷰에서의 위치 파악
        if(location[1] < 0)
            location[1] = (-1)*location[1];

        // 위치 변경
        scrollView.scrollTo(0, location[1]+60);
        location[1] += 60;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    // 눈깜박임에 따른 페이지 up 함수
    public void change_up_location(){
        // 절대값을 통해 text뷰의 스크롤뷰에서의 위치 파악
        if(location[1] < 0)
            location[1] = (-1)*location[1];
        // 기존의 위치에서 60 이동
        scrollView.scrollTo(0, location[1]-60);
        location[1] -= 60;
    }

    // 책갈피 추가함수
    public void book_mark_add(){

        //book_mark의 전역변수 설정 후, db에 저장 필요
        // book_mark는 int형 변수
        tv.getLocationOnScreen(location);
        book_mark = location[1];
        if(book_mark < 0 ) book_mark = (-1)*book_mark;

    }



    private boolean isCameraPermissionGranted() {
        return ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request the camera permission
     */
    private void requestCameraPermission() {
        final String[] permissions = new String[]{android.Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CAMERA_PERM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != REQUEST_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createCameraResources();
            return;
        }

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("EyeControl")
                .setMessage("No camera permission")
                .setPositiveButton("Ok", listener)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();

        // register the event bus
        EventBus.getDefault().register(this);

        // start the camera feed
        if (mCameraSource != null && isCameraPermissionGranted()) {
            try {
                //noinspection MissingPermission
                mCameraSource.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //   Log.e(TAG, "onResume: Camera.start() error");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // unregister from the event bus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        // stop the camera source
        if (mCameraSource != null) {
            mCameraSource.stop();
        } else {
            //    Log.e(TAG, "onPause: Camera.stop() error");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // release them all...
        if (mFaceDetector != null) {
            mFaceDetector.release();
        } else {
            // Log.e(TAG, "onDestroy: FaceDetector.release() error");
        }
        if (mCameraSource != null) {
            mCameraSource.release();
        } else {
            // Log.e(TAG, "onDestroy: Camera.release() error");
        }
    }

    /*   @Subscribe(threadMode = ThreadMode.MAIN)
       public void onLeftEyeClosed(LeftEyeClosedEvent e) {
           change_down_location();
       }
   */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRightEyeClosed(RightEyeClosedEvent e) {
        // change_up_location();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNeutralFace(NeutralFaceEvent e) {

        change_down_location();

    }

    private void createCameraResources() {
        Context context = getContext(); // getApplicationContext()를 바꿈

        // create and setup the face detector
        mFaceDetector = new FaceDetector.Builder(context)
                .setProminentFaceOnly(true) // optimize for single, relatively large face
                .setTrackingEnabled(true) // enable face tracking
                .setClassificationType(/* eyes open and smile */ FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE) // for one face this is OK
                .build();

        // now that we've got a detector, create a processor pipeline to receive the detection
        // results
        mFaceDetector = new FaceDetector.Builder(context)
                .setProminentFaceOnly(true) // optimize for single, relatively large face
                .setTrackingEnabled(true) // enable face tracking
                .setClassificationType(/* eyes open and smile */ FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE) // for one face this is OK
                .build();

        // now that we've got a detector, create a processor pipeline to receive the detection
        // results

        mFaceDetector.setProcessor(new LargestFaceFocusingProcessor(mFaceDetector, face_tracker = new FaceTracker()));

        // operational...?
        if (!mFaceDetector.isOperational()) {
            //  Log.w(TAG, "createCameraResources: detector NOT operational");
        } else {
            //   Log.d(TAG, "createCameraResources: detector operational");
        }

        // Create camera source that will capture video frames
        // Use the front camera
        mCameraSource = new CameraSource.Builder(this, mFaceDetector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30f)
                .build();
    }







}
