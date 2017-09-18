package com.example.hyemin.blinkling;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyemin.blinkling.BookShelf.BookshelfFragment;
import com.example.hyemin.blinkling.Book_Viewer.InnerStorageFragment;
import com.example.hyemin.blinkling.Book_Viewer.TextViewFragment;
import com.example.hyemin.blinkling.Bookmark.BookTab_Fragment;
import com.example.hyemin.blinkling.Bookmark.BookmarkFragment;
import com.example.hyemin.blinkling.Bookmark.CustomAdapter_audio;
import com.example.hyemin.blinkling.Bookmark.CustomAdapter_book;
import com.example.hyemin.blinkling.Bookmark.CustomAdapter_web;
import com.example.hyemin.blinkling.Bookmark.DateFormatter;
import com.example.hyemin.blinkling.Bookmark.ExamDbFacade;
import com.example.hyemin.blinkling.Bookmark.ExamDbFacade_audio;
import com.example.hyemin.blinkling.Bookmark.ExamDbFacade_web;
import com.example.hyemin.blinkling.Bookmark.InfoClass;

import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.hyemin.blinkling.Book_Viewer.InnerStorageFragment;
import com.example.hyemin.blinkling.Book_Viewer.TextViewFragment;
import com.example.hyemin.blinkling.Bookmark.InfoClass_audio;
import com.example.hyemin.blinkling.Bookmark.InfoClass_web;

import com.example.hyemin.blinkling.Service.AudioService;
import com.example.hyemin.blinkling.Service.ScreenFilterService;
import com.example.hyemin.blinkling.Setting.SettingFragment;
import com.example.hyemin.blinkling.Webview.WebviewFragment;
import com.example.hyemin.blinkling.event.EyeSettingEvent;
import com.example.hyemin.blinkling.event.RightEyeClosedEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends ActionBarActivity {
    private static final String LOG_TAG = "PlaybackFragment";

    private static final String TAG = "AppPermission";
    public static Context mContext;
    TextViewFragment txt_fragment;
    WebviewFragment webview_fragment;
    private String book_title;
    private int bookmark_pos;
    private String T_date;
    private String T_date_web;
    ExamDbFacade mFacade;
    ExamDbFacade_web mFacade_web;
    ExamDbFacade_audio mFacade_audio;
    ArrayList<InfoClass> insertResult;
    ArrayList<InfoClass_audio> insertResult_audio;
    ArrayList<InfoClass_web> insertResult_web;
    CustomAdapter_book mAdapter;
    CustomAdapter_web mAdapter_web;
    CustomAdapter_audio mAdapter_audio;
    String url;
    Fragment current_fragment;
    int fragment_type;
    private boolean isRecording = false;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    String InStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Blinkling";
    String AudioStorage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Audio_Dir";

    public static BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private Fragment fragment2;
    private FragmentManager fragmentManager;
    FragmentTransaction transaction;
    private Toolbar toolbar;
    public static boolean light;//초기상태는 불이 꺼진 상태
    public static FrameLayout aframe;
    public String web_bookmark_url;
    String audio_path;
    public Boolean eyesetting = true;
    public Boolean lightsetting = true;
    public Boolean recordingsetting = true;
    FloatingActionButton fab;
    public boolean floating_setter = true;
    private boolean isPlaying = false;
    private MediaPlayer mMediaPlayer = null;
    String receivedPath;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        transaction.hide(current_fragment);
        super.onConfigurationChanged(newConfig);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mContext = this;
        Intent intent = getIntent();
        audio_path = intent.getStringExtra("audio_path");

        mFacade = new ExamDbFacade(getApplicationContext());
        mAdapter = new CustomAdapter_book(getApplicationContext(), mFacade.getCursor(), false);

        makeDirectory(InStoragePath);
        makeDirectory(AudioStorage);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        aframe = (FrameLayout) findViewById(R.id.main_container);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_default);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        }

        light = false;
        fragmentManager = getSupportFragmentManager();
        fragment = new BookshelfFragment();
        fragment_type = 1;
        current_fragment = fragment;
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, fragment, "fragBookshelf").commit();

         getSupportActionBar().setTitle("");
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                fragment = new BookshelfFragment();
                                fragment_type = 1;
                                break;

                            case R.id.navigation_write:

                                fragment = new BookmarkFragment();
                                fragment_type = 2;
                                break;

                            case R.id.navigation_friends:
                                fragment = new WebviewFragment();
                                fragment_type = 3;
                                break;

                            case R.id.navigation_foodbank:
                                fragment = new SettingFragment();
                                fragment_type = 4;
                                break;

                        }
                        replaceFragment(fragment);
                        return true;
                    }
                });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlay(isPlaying);
                isPlaying = !isPlaying;

            }
        });

        floatingBtnHide();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


        public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                transaction.hide(current_fragment);

                fragmentManager.popBackStack();
                return true;
            }
            case R.id.notebook_add: {
                InnerStorageFragment_start();
            }
            case R.id.eye_btn: {
                eyesetting = !eyesetting;
                EventBus.getDefault().post(new EyeSettingEvent());
                invalidateOptionsMenu(); //cause a redraw
                return true;
            }
            case R.id.voice_btn: {
                recordingsetting = !recordingsetting;
                audioService();
                return true;
            }
            case R.id.bookmark_btn: {
                addBookmark();
                return true;
            }
            case R.id.webmark_add: {
                addWebmark();
                return true;
            }

            case R.id.light_btn: {
                Intent service = new Intent(this, ScreenFilterService.class);
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!Settings.canDrawOverlays(this)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 1234);
                    } else {
                        if (lightsetting) {
                            startService(service);
                            lightsetting = false;
                        } else {
                            stopService(service);
                            lightsetting = true;
                        }
                    }
                }
                invalidateOptionsMenu();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //start audio recording or whatever you planned to do
            }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECORD_AUDIO)) {
                    //Show an explanation to the user *asynchronously*
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("This permission is important to record audio.")
                            .setTitle("Important permission required");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
                        }
                    });
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
                }else{
                    Toast.makeText(this, "permission이 필요합니다", Toast.LENGTH_SHORT).show();
                    //Never ask again and handle your app without permission.
                }
            }
        }
    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) {                  //fragment not in back stack, create it.
            transaction = manager.beginTransaction();
            transaction.hide(current_fragment);
            transaction.replace(R.id.main_container, fragment);
            current_fragment = fragment;
            transaction.addToBackStack(backStateName);
            transaction.commit();
        }
    }

    //디비 목록에서 책 열때 사용
    public void changeToText(String valueBookName, int pos){
        Fragment frag = new TextViewFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("book_position", pos);

        bundle.putString("bookname", valueBookName);//번들에 값을 넣음
        frag.setArguments(bundle);

        fragmentManager.popBackStackImmediate(frag.getClass().getName(), fragmentManager.POP_BACK_STACK_INCLUSIVE);
        replaceFragment(frag);

        book_title = valueBookName;
    }


    public void audioService(){

        java.util.Calendar cal = java.util.Calendar.getInstance();
        T_date = DateFormatter.format(cal, "yyyy-MM-dd HH:mm:ss");

        txt_fragment = (TextViewFragment) getSupportFragmentManager().findFragmentById(R.id.main_container);
        TextView txt = txt_fragment.getTxtBook();
        bookmark_pos = txt_fragment.book_mark_add(txt);

        Intent intent = new Intent(this, AudioService.class);
        intent.putExtra("b_pos", Integer.toString(bookmark_pos));
        intent.putExtra("b_name", book_title);
        intent.putExtra("b_date", T_date);

        if (recordingsetting == false) {
            startService(intent);


        } else {
            stopService(intent);

        }
        invalidateOptionsMenu();
    }

    private File makeDirectory(String dir_path) {
        File dir = new File(dir_path);
        if (!dir.exists()) {
            dir.mkdirs();
            Log.i(TAG, "!dir.exists");
        } else {
            Log.i(TAG, "dir.exists");
        }

        return dir;
    }

    public void InnerStorageFragment_start() {
        fragment = new InnerStorageFragment();
        replaceFragment(fragment);
    }

    public void goWebview(String url) {
        fragment2 = new WebviewFragment();

        web_bookmark_url = url;

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment2, "web_frag").commit();

        if (webview_fragment != null && webview_fragment.isAdded()) {
            webview_fragment.goPage(url);
        }
    }

    public void addWebmark() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        T_date_web = DateFormatter.format(cal, "yyyy-MM-dd HH:mm:ss");

        webview_fragment = (WebviewFragment) getSupportFragmentManager().findFragmentById(R.id.main_container);
        url = webview_fragment.getCurrentURL();

        final EditText editText_web = new EditText(this);
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("웹마크 등록")
                .setMessage("웹마크 이름을 입력하세요")
                .setView(editText_web)
                .setPositiveButton("등록",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = editText_web.getText().toString();//웹마크의 이름
                                String position = url; //북마크 좌표
                                String time_date = T_date_web; //저장된 시각

                                if (mFacade_web == null) {
                                    mFacade_web = new ExamDbFacade_web(getApplicationContext());
                                    mAdapter_web = new CustomAdapter_web(getApplicationContext(), mFacade_web.getCursor(), false);
                                }

                                insertResult_web = mFacade_web.insert(title, time_date, time_date, position);
                                mAdapter_web.changeCursor(mFacade_web.getCursor());

                            }
                        }).setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.create().show();
    }

    public void addBookmark() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        T_date = DateFormatter.format(cal, "yyyy-MM-dd HH:mm:ss");

        txt_fragment = (TextViewFragment) getSupportFragmentManager().findFragmentById(R.id.main_container);
        TextView txt = txt_fragment.getTxtBook();
        bookmark_pos = txt_fragment.book_mark_add(txt); //북마크로 저장 할 좌표를 bookmark_pos에 저장함

        final EditText editText = new EditText(this);
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("북마크 등록")
                .setMessage("북마크 이름을 입력하세요")
                .setView(editText)
                .setPositiveButton("등록",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = editText.getText().toString();//북마크의 이름
                                int position = bookmark_pos; //북마크 좌표
                                String document = book_title; //문서의 이름
                                String time_date = T_date; //저장된 시각

                                if (mFacade == null) {
                                    mFacade = new ExamDbFacade(getApplicationContext());
                                    mAdapter = new CustomAdapter_book(getApplicationContext(), mFacade.getCursor(), false);
                                }


                                insertResult = mFacade.insert(title, document, time_date, time_date, Integer.toString(position));
                                mAdapter.changeCursor(mFacade.getCursor());

                            }
                        }).setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialog.create().show();

    }

    public void sendBookname(String mBookName) {
        ((BookshelfFragment) getSupportFragmentManager().findFragmentByTag("fragBookshelf")).setBookshelf(mBookName);
    }

    public void floatingBtnHide(){
        fab.hide();
    }

    public void floatingBtnShow(String audio_path){
        fab.show();
        receivedPath = audio_path;


    }

    // Play start/stop
    private void onPlay(boolean isPlaying){
        if (!isPlaying) {
            //currently MediaPlayer is not playing audio
            if(mMediaPlayer == null) {
                startPlaying(); //start from beginning
            } else {
                resumePlaying(); //resume the currently paused MediaPlayer
            }

        } else {
            //pause the MediaPlayer
            pausePlaying();
        }
    }

    private void startPlaying() {
        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(receivedPath);
            mMediaPlayer.prepare();

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
            }
        });

    }

    private void pausePlaying() {
        mMediaPlayer.pause();
    }

    private void resumePlaying() {
        mMediaPlayer.start();
    }

    private void stopPlaying() {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;

        isPlaying = !isPlaying;
        floatingBtnHide();

    }

}