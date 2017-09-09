package com.example.hyemin.blinkling;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.hyemin.blinkling.Bookmark.CustomAdapter;
import com.example.hyemin.blinkling.Bookmark.DateFormatter;
import com.example.hyemin.blinkling.Bookmark.DbOpenHelper;
import com.example.hyemin.blinkling.Bookmark.ExamDbFacade;
import com.example.hyemin.blinkling.Bookmark.InfoClass;
import com.example.hyemin.blinkling.Service.AudioService;
import com.example.hyemin.blinkling.Service.ScreenFilterService;
import com.example.hyemin.blinkling.Setting.SettingFragment;
import com.example.hyemin.blinkling.Webview.WebviewFragment;

import java.io.File;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "AppPermission";
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;
    DbOpenHelper db_helper;
    TextViewFragment txt_fragment;
    private String book_title;
    private int bookmark_pos;
    private String T_date;
    private boolean isEditing = false;
    BookTab_Fragment bookTab_fragment = new BookTab_Fragment();
    ExamDbFacade mFacade;
    ArrayList<InfoClass> insertResult;
    CustomAdapter mAdapter;
    BookTab_Fragment bf;
    Fragment current_fragment;

    private boolean init = true;

    private boolean isRecording = false;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    String InStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Blinkling";

    public static BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    FragmentTransaction transaction;
    private Toolbar toolbar;
    public static boolean light;//초기상태는 불이 꺼진 상태
    public static FrameLayout aframe;

    public String mBookName_main = "";

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        //    FrameLayout fl = (FrameLayout) findViewById(R.id.main_container);
        //    fl.removeAllViews();
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction ft = manager.beginTransaction();
//        ft.hide(current_fragment);
        transaction.hide(current_fragment);
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFacade = new ExamDbFacade(getApplicationContext());
        mAdapter = new CustomAdapter(getApplicationContext(), mFacade.getCursor(), false);

        makeDirectory(InStoragePath);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        aframe = (FrameLayout) findViewById(R.id.main_container);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.folder);

        light = false;
        fragmentManager = getSupportFragmentManager();
        fragment = new BookshelfFragment();
        current_fragment = fragment;
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, fragment,"fragBookshelf").commit();



        //current_fragment = fragment; 
        getSupportActionBar().setTitle("블링클링");
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                fragment = new BookshelfFragment();

                                break;

                            case R.id.navigation_write:

                                fragment = new BookmarkFragment();
                                break;

                            case R.id.navigation_friends:
                                fragment = new WebviewFragment();
                                break;

                            case R.id.navigation_foodbank:
                                fragment = new SettingFragment();
                                break;

                        }
                        replaceFragment(fragment);
                        return true;
                    }
                });

    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);

        //  FrameLayout fl = (FrameLayout) findViewById(R.id.main_container);
        //  fl.removeAllViews();

        RelativeLayout l = (RelativeLayout) findViewById(R.id.activity_main);


        if (!fragmentPopped) {                  //fragment not in back stack, create it.
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.hide(current_fragment);
//            ft.replace(R.id.main_container, fragment);
//            current_fragment = fragment;
//            ft.addToBackStack(backStateName);
//            ft.commit();

            transaction = manager.beginTransaction();
            transaction.hide(current_fragment);
            transaction.replace(R.id.main_container, fragment);
            current_fragment = fragment;
            transaction.addToBackStack(backStateName);
            transaction.commit();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                //FragmentManager fm = getSupportFragmentManager();

                //  FrameLayout fl = (FrameLayout) findViewById(R.id.main_container);
                //  fl.removeAllViews();

                transaction.hide(current_fragment);

                fragmentManager.popBackStack();
                return true;
            }
            case R.id.notebook_add: {/////

                checkPermission(READ_EXTERNAL_STORAGE);
            }
            case R.id.notebook_delete: {
                Toast toast;
                toast = Toast.makeText(this, item.getTitle() + " Clicked delete button!", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
            case R.id.eye_btn: {
                Toast toast;
                toast = Toast.makeText(this, item.getTitle() + " Clicked eye button!", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
            case R.id.voice_btn: {

                Intent intent = new Intent(this, AudioService.class);
                Toast toast;
                // Requesting permission to RECORD_AUDIO

                //ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
                checkPermission(RECORD_AUDIO);

                if (isRecording == false) {
                    startService(intent);
                    //   Toast.makeText(this, "녹음시작", Toast.LENGTH_SHORT).show();
                    isRecording = true;
                } else {
                    stopService(intent);
                    // Toast.makeText(this, "녹음종료", Toast.LENGTH_SHORT).show();
                    isRecording = false;
                }


                toast = Toast.makeText(this, item.getTitle() + " Clicked voice button!", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
            case R.id.bookmark_btn: {
                addBookmark();
                return true;
            }
            case R.id.bookmark_delete: {
                bookTab_fragment.checkBookmarkDelete();
                return true;
            }
            case R.id.bookmark_edit: {

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
                        if (!light) {
                            startService(service);
                            light = true;
                        } else {
                            stopService(service);
                            light = false;
                        }
                    }
                }
                Toast toast;
                toast = Toast.makeText(this, item.getTitle() + " Clicked light button!", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);

    }

    public void changeToText(String valueBookName) {

        Fragment frag = new TextViewFragment();
        Bundle bundle = new Bundle();

        bundle.putString("bookname", valueBookName);//번들에 값을 넣음
        frag.setArguments(bundle);

//        FrameLayout fl = (FrameLayout) findViewById(R.id.main_container);
//        fl.removeAllViews();

        //   final FragmentTransaction transaction = fragmentManager.beginTransaction();
        replaceFragment(frag);
        // transaction.replace(R.id.main_container, frag).commit();

        book_title = valueBookName;

    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission(String requestCode) {
        // Log.i(TAG, "CheckPermission : " +  ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE));
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE:
                if (checkSelfPermission(READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                        // Explain to the user why we need to write the permission.
                        Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
                    }

                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSION_REQUEST_STORAGE);

                    // MY_PERMISSION_REQUEST_STORAGE is an
                    // app-defined int constant

                } else {
                    InnerStorageFragment_start();

                }


            case RECORD_AUDIO:
                if (checkSelfPermission(RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {


                    // Should we show an explanation?
                    if (shouldShowRequestPermissionRationale(RECORD_AUDIO)) {
                        // Explain to the user why we need to write the permission.
                        Toast.makeText(this, "Record audio", Toast.LENGTH_SHORT).show();
                    }

                    requestPermissions(new String[]{RECORD_AUDIO, android.Manifest.permission.RECORD_AUDIO},
                            MY_PERMISSION_REQUEST_STORAGE);

                    // MY_PERMISSION_REQUEST_STORAGE is an
                    // app-defined int constant

                } else {
                    //실행

                }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    InnerStorageFragment_start();

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {

                    Log.d(TAG, "Permission always deny");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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

    public void addBookmark() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        T_date = DateFormatter.format(cal, "yyyy-MM-dd HH:mm:ss");

        txt_fragment = (TextViewFragment) getSupportFragmentManager().findFragmentById(R.id.main_container);
        TextView txt = txt_fragment.getTxtBook();

/*        bookTab_fragment = (BookTab_Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_web__tab_);
        mFacade = bookTab_fragment.getFacade();
        mAdapter = bookTab_fragment.getAdapter();*/

        bookmark_pos = txt_fragment.book_mark_add(txt); //북마크로 저장 할 좌표를 bookmark_pos에 저장함

        //  bf = (BookTab_Fragment)getSupportFragmentManager().findFragmentByTag("BK");

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
                              /*  if (db_helper == null) {
                                    db_helper = new DbOpenHelper(getApplicationContext());
                                    db_helper.open();
                                }*/

                                BookTab_Fragment bookTab_fragment = new BookTab_Fragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("title", title);
                                bundle.putString("document", document);
                                bundle.putString("time_date", time_date);
                                bundle.putString("position", Integer.toString(position));
                                bookTab_fragment.setArguments(bundle);

                                insertResult = mFacade.insert(title, document, time_date, time_date, Integer.toString(position));
                                mAdapter.changeCursor(mFacade.getCursor());

                                //입력된 데이터를 insertColumn을 통해 add
                                //  db_helper.insertColumn(title,document,time_date,time_date,Integer.toString(position));


                                //   bf.getInsertValue();
                            }
                        }).setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialog.create().show();

    }

    public void sendBookname(String mBookName) {
//        BookshelfFragment tf = (BookshelfFragment) getSupportFragmentManager().findFragmentById(R.id.main_container);
//        tf.setBookshelf(mBookName);

        ((BookshelfFragment) getSupportFragmentManager().findFragmentByTag("fragBookshelf")).setBookshelf(mBookName);

    }

}