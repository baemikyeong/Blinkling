package com.example.hyemin.blinkling.Webview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyemin.blinkling.MainActivity;
import com.example.hyemin.blinkling.R;
import com.example.hyemin.blinkling.event.EyeSettingEvent;
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

import java.io.IOException;

import static android.support.v4.app.ActivityCompat.invalidateOptionsMenu;
import static java.lang.Thread.sleep;

public class WebviewFragment extends Fragment {

    public WebView webView;
    private FaceDetector mFaceDetector;                     // 얼굴 인식
    private CameraSource mCameraSource;                     // 카메라 객체
    private FaceTracker face_tracker;                       // 눈 파악
    private static final int REQUEST_CAMERA_PERM = 69;      // 카메라 퍼미션을 위한 코드
    private int[] location = new int[2];
    EditText url_String;
    View main_view;
    String checkURL = null;
    private SharedPreferences intPref;
    Boolean eyesetting = true;

    public WebviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        eyesetting = ((MainActivity)getActivity()).eyesetting;
        intPref = this.getActivity().getSharedPreferences("mPred", Activity.MODE_PRIVATE);//이거
        main_view = inflater.inflate(R.layout.fragment_webview, container, false);
        webView = (WebView) main_view.findViewById(R.id.webView1);
        WebSettings set = webView.getSettings();
        ImageButton button = (ImageButton)main_view.findViewById(R.id.btnGo);
        ImageButton back_button = (ImageButton)main_view.findViewById(R.id.back);
        url_String = (EditText)main_view.findViewById(R.id.txtURL);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        PlayServicesUtil.isPlayServicesAvailable(getActivity(), 69);

        webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                webView.getLocationOnScreen(location);
                return false;
            }
        });
        // permission granted...?
        if (isCameraPermissionGranted()) {
            // ...create the camera resource
            createCameraResources();
        } else {
            // ...else request the camera permission
            requestCameraPermission();
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        @Override
        public void onPageFinished(WebView view, String url) {
            url_String.setText(url);
            super.onPageFinished(view, url);
        }
        });
        if(((MainActivity)getActivity()).web_bookmark_url == null){
           // Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
            webView.loadUrl("http://www.naver.com");
        }
        else{
            webView.loadUrl(((MainActivity)getActivity()).web_bookmark_url);
            ((MainActivity)getActivity()).web_bookmark_url = null;
        }


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String urlString = url_String.getText().toString();

                if ( urlString.startsWith("http") != true )
                    urlString = "http://"+urlString;

                webView.loadUrl(urlString);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                webView.goBack();
            }
        });

        url_String.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ( keyCode == KeyEvent.KEYCODE_ENTER )
                {
                    String urlString = url_String.getText().toString();

                    if ( urlString.startsWith("http") != true )
                        urlString = "http://"+urlString;

                    webView.loadUrl(urlString);
                    return true;
                }

                return false;
            }
        });

        set.setCacheMode(WebSettings.LOAD_NO_CACHE);
        set.setSupportZoom(false);
        return main_view;
    }

    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.bookmark_btn).setVisible(false);
        menu.findItem(R.id.notebook_add).setVisible(false);

        if(eyesetting == false)
            menu.findItem(R.id.eye_btn).setIcon(R.drawable.ic_eye_pressed);
        else
            menu.findItem(R.id.eye_btn).setIcon(R.drawable.ic_eye_default);

        if((((MainActivity)getActivity()).lightsetting) == true){
           menu.findItem(R.id.light_btn).setIcon(R.drawable.ic_bluelight_default);
        }
        else
            menu.findItem(R.id.light_btn).setIcon(R.drawable.ic_bluelight_pressed);

        if((((MainActivity)getActivity()).recordingsetting) == true){
            menu.findItem(R.id.voice_btn).setIcon(R.drawable.ic_mic_pressed);
        }
        else
            menu.findItem(R.id.voice_btn).setIcon(R.drawable.ic_mic_pressed_on);
        super.onPrepareOptionsMenu(menu);
    }

    public void goURL(WebView view) {
        TextView tvURL = (TextView) main_view.findViewById(R.id.txtURL);
        String url = tvURL.getText().toString();
        Log.i("URL", "Opening URL :" + url);

        view.loadUrl(url);

    }

    public void goPage(String url){
        checkURL = url;
        Toast.makeText(getActivity(),url,Toast.LENGTH_SHORT).show();
    }


    public String getCurrentURL(){
        //현재 페이지의 url를 리턴하는 메소드
        return url_String.getText().toString();
    }


    private class WebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("sms:")) {
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                startActivity(i);
                return true;
            }

            if (url.startsWith("kakaolink:")) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
                return true;
            }

            if (url.startsWith("tel")) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse(url));
                startActivity(i);
            } else {
                view.loadUrl(url);
            }

            return true;
        }
    }

    //눈깜박임에 따른 페이지 down 함수
    public void change_down_location(){
        // 절대값을 통해 text뷰의 스크롤뷰에서의 위치 파악
        if(location[1] < 0)
            location[1] = (-1)*location[1];

        // 위치 변경
        webView.scrollTo(0, location[1]+300);
        location[1] += 300;

    }

    // 눈깜박임에 따른 페이지 up 함수
    public void change_up_location(){
        // 절대값을 통해 text뷰의 스크롤뷰에서의 위치 파악
        if(location[1] < 0)
            location[1] = (-1)*location[1];
        // 기존의 위치에서 60 이동
        webView.scrollTo(0, location[1]-300);
        location[1] -= 300;
    }

    /**
     * Check camera permission
     *
     * @return <code>true</code> if granted
     */
    private boolean isCameraPermissionGranted() {
        return ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
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
                getActivity().finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("EyeControl")
                .setMessage("No camera permission")
                .setPositiveButton("Ok", listener)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (MainActivity.bottomNavigation.getSelectedItemId() != R.id.navigation_friends)
            MainActivity.bottomNavigation.getMenu().findItem(R.id.navigation_friends).setChecked(true);

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

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // release them all...
        if (mFaceDetector != null) {
            mFaceDetector.release();
        } else {

        }
        if (mCameraSource != null) {
            mCameraSource.release();
        } else {

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRightEyeClosed(RightEyeClosedEvent e) {
       if(eyesetting == true) {
           change_down_location();

           try {
               sleep(100);
           } catch (InterruptedException e1) {
               e1.printStackTrace();
           }
       }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNeutralFace(NeutralFaceEvent e) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEyeSetting(EyeSettingEvent e) {
        eyesetting = ((MainActivity)getActivity()).eyesetting;
    }

    private void createCameraResources() {
        Context context = getActivity().getApplicationContext();

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
        face_tracker = new FaceTracker();

        float right_eye = intPref.getFloat("LValue", 0.6f);
        float left_eye = intPref.getFloat("RValue", 0.6f);
        long blink_time = intPref.getLong("time_blink", 900);
        face_tracker.set_indi(left_eye, right_eye, blink_time);

        mFaceDetector.setProcessor(new LargestFaceFocusingProcessor(mFaceDetector, face_tracker));

        // operational...?
        if (!mFaceDetector.isOperational()) {

        } else {

        }

        // Create camera source that will capture video frames
        // Use the front camera
        mCameraSource = new CameraSource.Builder(getActivity(), mFaceDetector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(15f)
                .build();
    }

}
