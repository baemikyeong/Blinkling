package com.example.hyemin.blinkling;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyemin.blinkling.BookShelf.BookshelfFragment;
import com.example.hyemin.blinkling.Bookmark.BookmarkFragment;
import com.example.hyemin.blinkling.Bookmark.DateFormatter;
import com.example.hyemin.blinkling.Bookmark.DbOpenHelper;
import com.example.hyemin.blinkling.Bookmark.a;
import com.example.hyemin.blinkling.Service.ScreenFilterService;
import com.example.hyemin.blinkling.Setting.SettingFragment;
import com.example.hyemin.blinkling.Webview.WebviewFragment;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = "AppPermission";
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;
    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    boolean light;//초기상태는 불이 꺼진 상태
    DbOpenHelper db_helper;
    private TextView tv;
    TextViewFragment txt_fragment;
    private String book_title;
    private int bookmark_pos;
    private String T_date;


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

        if (!fragmentPopped) { //fragment not in back stack, create it.
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
               Intent intent = new Intent(MainActivity.this, a.class);
                startActivity(intent);

                return true;
            }
            case R.id.notebook_add: {
                checkPermission();
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
                Toast toast;
                toast = Toast.makeText(this, item.getTitle() + " Clicked voice button!", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
            case R.id.bookmark_btn: {
                addBookmark();
                return true;
            }

            case R.id.light_btn: {
                Intent service = new Intent(this, ScreenFilterService.class);
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!Settings.canDrawOverlays(this)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 1234);
                    }
                    if (!light) {
                        startService(service);
                        light = true;
                    } else {
                        stopService(service);
                        light = false;
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

        bundle.putString("bookname", valueBookName);
        frag.setArguments(bundle);


        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, frag).commit();

        book_title = valueBookName;

    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        Log.i(TAG, "CheckPermission : " + ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE));
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_STORAGE);

            // MY_PERMISSION_REQUEST_STORAGE is an
            // app-defined int constant

        } else {

            start();
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
                                if (db_helper == null) {
                                    db_helper = new DbOpenHelper(getApplicationContext());
                                    db_helper.open();
                                }

                                db_helper.insertColumn(title,document,time_date,time_date,Integer.toString(position));
                                //InfoClass infoClass = new InfoClass()
                              //  Bookmarnfo bi = new Bookmark_Info(title,type,document);
/*

                                Bookmark_Info bi = new Bookmark_Info();
                                bi.setTitle(title);
                                bi.setType(type);
                                bi.setDoc(document);
                                bookmark_db.addBookmark(bi);*/


                            }
                        }).setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialog.create().show();

    }
}