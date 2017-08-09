package com.example.hyemin.blinkling.Webview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hyemin.blinkling.R;
import com.example.hyemin.blinkling.event.NeutralFaceEvent;
import com.example.hyemin.blinkling.event.RightEyeClosedEvent;
import com.example.hyemin.blinkling.tracker.FaceTracker;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

public class WebviewFragment extends Fragment {

    private WebView webView;
    private ProgressBar mPBar;
    private FaceDetector mFaceDetector;                     // 얼굴 인식
    private CameraSource mCameraSource;                     // 카메라 객체
    private FaceTracker face_tracker;                       // 눈 파악
    private double left_thres = 0;                          // 사용자의 초기값
    private double right_thres = 0;
    private static final int REQUEST_CAMERA_PERM = 69;      // 카메라 퍼미션을 위한 코드
    private int[] location = new int[2];
    View main_view;

    public WebviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_webview, container, false);
        webView = (WebView) main_view.findViewById(R.id.webView1);
        mPBar = (ProgressBar) main_view.findViewById(R.id.progress01);
        WebSettings set = webView.getSettings();

        webView.getLocationOnScreen(location);
        webView.getSettings().setJavaScriptEnabled(true);
        goURL(webView);


        set.setCacheMode(WebSettings.LOAD_NO_CACHE);
        set.setSupportZoom(false);
        return main_view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.notebook_add).setVisible(false);
        menu.findItem(R.id.notebook_delete).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    public void goURL(View view) {
        TextView tvURL = (TextView) main_view.findViewById(R.id.txtURL);
        String url = tvURL.getText().toString();
        Log.i("URL", "Opening URL :" + url);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                webView.clearCache(false);
                getActivity().finish();
            }
            return true;
        }
        return super.getActivity().onKeyDown(keyCode, event);
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
        webView.scrollTo(0, location[1]+60);
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
        webView.scrollTo(0, location[1]-60);
        location[1] -= 60;
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

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNeutralFace(NeutralFaceEvent e) {

        change_down_location();

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

        mFaceDetector.setProcessor(new LargestFaceFocusingProcessor(mFaceDetector, face_tracker = new FaceTracker()));

        // operational...?
        if (!mFaceDetector.isOperational()) {

        } else {

        }

        // Create camera source that will capture video frames
        // Use the front camera
        mCameraSource = new CameraSource.Builder(getActivity(), mFaceDetector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30f)
                .build();
    }

}
