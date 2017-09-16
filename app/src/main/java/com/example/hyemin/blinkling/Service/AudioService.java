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

import com.example.hyemin.blinkling.MainActivity;

import java.io.File;
import java.io.IOException;

import static android.app.PendingIntent.getActivity;

public class AudioService extends Service{
    MediaRecorder recorder;
    File audiofile = null;
    static final String TAG = "MediaRecording";
    MainActivity mainActivity;
    public static Context mContext;
    File temp;

    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

      //  File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/Blinkling", bookName);
        // i have kept text.txt in the sd-card

        //File 생성자를 통해 다음과 같이 file을 저장할 directory를 생성한다.
        File storeDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"AudioDirectory");
        if(!storeDir.exists()){
            if(!storeDir.mkdirs()){
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
            Toast.makeText(this,"recording started", Toast.LENGTH_SHORT).show();
            startRecording();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }



   @Override
    public void onDestroy() {
        Toast.makeText(this,"recording terminated", Toast.LENGTH_SHORT).show();
        stopRecording();
        stopSelf();
      //  ((MainActivity)MainActivity.mContext).getSavedAudioFilePath();

        super.onDestroy();
        // 서비스가 종료될 때 실행

    }




    public void startRecording() throws IOException {
        //Creating file
        File dir = Environment.getExternalStorageDirectory();
        try {
            audiofile = File.createTempFile("sound", ".3gp", dir);
        } catch (IOException e) {
            Log.e(TAG, "external storage access error");
            return;
        }
        //Creating MediaRecorder and specifying audio source, output format, encoder & output format
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        recorder.prepare();
        recorder.start();
    }


    public void stopRecording() {

        if(recorder != null){
           // addRecorder();
            recorder.stop();
            recorder.release();
            recorder = null;
            addRecordingToMediaLibrary();
        }
    }

    public void addRecorder(){
        recorder.stop();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("audio_path", audiofile.getAbsolutePath());
        startActivity(intent);
        ((MainActivity)MainActivity.mContext).addAudiomark();
    }




    protected void addRecordingToMediaLibrary() {
        //creating content values of size 4
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());

        //creating content resolver and storing it in the external content uri
        ContentResolver contentResolver = getContentResolver();
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);

        //sending broadcast message to scan the media file so that it can be available
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));

      //  Toast.makeText(this, audiofile.getAbsolutePath(),Toast.LENGTH_SHORT).show();
    //    Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();
    }

 /*   public void getAudioFilePath(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("audio_path",MediaStore.Audio.Media.DATA);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }*/
}