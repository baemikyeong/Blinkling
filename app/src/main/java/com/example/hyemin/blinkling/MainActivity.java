package com.example.hyemin.blinkling;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hyemin.blinkling.BookShelf.BookshelfFragment;
import com.example.hyemin.blinkling.Bookmark.BookmarkFragment;
import com.example.hyemin.blinkling.Bookmark.Bookmark_DB;
import com.example.hyemin.blinkling.Service.AudioService;
import com.example.hyemin.blinkling.Service.ScreenFilterService;
import com.example.hyemin.blinkling.Setting.SettingFragment;
import com.example.hyemin.blinkling.Webview.WebviewFragment;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = "AppPermission";
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;
    private boolean isRecording = false;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    public static BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    public static boolean light;//초기상태는 불이 꺼진 상태
    private Bookmark_DB bookmark_db;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.folder);

        light = false;
        fragmentManager = getSupportFragmentManager();
        fragment = new BookshelfFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, fragment).commit();

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
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) {                  //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_container, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
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
                //데이터베이스이름을 블링클링으로 함
                bookmark_db = new Bookmark_DB(MainActivity.this, "Blinkling", null, 1);
                bookmark_db.testDB();

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
                toast = Toast.makeText(this, item.getTitle() + " Clicked delete button!", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);

    }



    public void changeToText(String valueBookName) {

        Fragment frag = new TextViewFragment();
        Bundle bundle = new Bundle();

        bundle.putString("bookname", valueBookName);
        frag.setArguments(bundle);


        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, frag).commit();


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
                    start();

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

                    start();

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

    public void start() {
        fragment = new InnerStorageFragment();
        replaceFragment(fragment);
    }
}