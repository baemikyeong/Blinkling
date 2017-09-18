package com.example.hyemin.blinkling.Service;

import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.hyemin.blinkling.Bookmark.CustomAdapter_audio;
import com.example.hyemin.blinkling.Bookmark.CustomAdapter_book;
import com.example.hyemin.blinkling.Bookmark.DateFormatter;
import com.example.hyemin.blinkling.Bookmark.ExamDbFacade;
import com.example.hyemin.blinkling.Bookmark.ExamDbFacade_audio;
import com.example.hyemin.blinkling.Bookmark.MySharedPreferences;
import com.example.hyemin.blinkling.MainActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.PendingIntent.getActivity;

public class AudioService extends Service {
    private static final String LOG_TAG = "RecordingService";
    MediaRecorder recorder;
    File audiofile = null;
    static final String TAG = "MediaRecording";
    MainActivity mainActivity;
    public static Context mContext;
    public String path;
    File temp;
    ExamDbFacade_audio mFacade;
    CustomAdapter_audio mAdapter;
    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;
    private int mElapsedSeconds = 0;
    //   private OnTimerChangedListener onTimerChangedListener = null;
    private static final SimpleDateFormat mTimerFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());

    private Timer mTimer = null;
    private TimerTask mIncrementTimerTask = null;

    private String mFileName = null;
    private String mFilePath = null;

    public IBinder onBind(Intent intent) {
        return null;
    }

    private String posi;
    private String book_name;
    private String created_date;

    @Override
    public void onCreate() {
        super.onCreate();
        mFacade = new ExamDbFacade_audio(getApplicationContext());
        mAdapter = new CustomAdapter_audio(getApplicationContext(), mFacade.getCursor(), false);


        //File 생성자를 통해 다음과 같이 file을 저장할 directory를 생성한다.
        File storeDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "AudioDirectory");
        if (!storeDir.exists()) {
            if (!storeDir.mkdirs()) {
                Log.d("MyDocApp", "failed to create directory");
                return;

            }
        }
        mContext = this;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        try {
            Toast.makeText(this, "recording started", Toast.LENGTH_SHORT).show();
            startRecording();

            if (intent != null && intent.getExtras() != null) {
                posi = intent.getStringExtra("b_pos");
                book_name = intent.getStringExtra("b_name");
                created_date = intent.getStringExtra("b_date");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    public void onDestroy() {
        if (recorder != null) {
            Toast.makeText(this, "recording terminated", Toast.LENGTH_SHORT).show();
            stopRecording();
        }
        stopSelf();
        // ((MainActivity)MainActivity.mContext).getSavedAudioFilePath();
        super.onDestroy();
        // 서비스가 종료될 때 실행

    }

    public void setFileNameAndPath() {
        int count = 0;
        File f;

        do {
            count++;

            mFileName = "My Recording"
                    + "_" + (mFacade.getCount() + count) + ".mp4";
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFilePath += "/Audio_Dir/" + mFileName;

            f = new File(mFilePath);
        } while (f.exists() && !f.isDirectory());
    }

    public void startRecording() throws IOException {
        setFileNameAndPath();

        //Creating MediaRecorder and specifying audio source, output format, encoder & output format
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(mFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setAudioChannels(1);
        if (MySharedPreferences.getPrefHighQuality(this)) {
            recorder.setAudioSamplingRate(44100);
            recorder.setAudioEncodingBitRate(192000);
        }

        try {
            recorder.prepare();
            recorder.start();
            mStartingTimeMillis = System.currentTimeMillis();


        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

    }


    public void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
            } catch (RuntimeException stopException) {

            }
            mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);

         //   Toast.makeText(this, getString(R.sting.toast_recording_finish) + " " + mFilePath, Toast.LENGTH_LONG).show();


            //remove notification
            if (mIncrementTimerTask != null) {
                mIncrementTimerTask.cancel();
                mIncrementTimerTask = null;
            }

            recorder = null;

            try {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                created_date = DateFormatter.format(cal, "yyyy-MM-dd HH:mm:ss");
                mFacade.addRecording(mFileName, mFilePath, mElapsedMillis, book_name, created_date, posi);
                recorder.release();


            } catch (Exception e) {
                Log.e(LOG_TAG, "exception", e);
            }

        }

    }

    }