package com.example.hyemin.blinkling;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hyemin.blinkling.BookShelf.BookshelfFragment;
import com.example.hyemin.blinkling.Bookmark.BookmarkFragment;
import com.example.hyemin.blinkling.Service.ScreenFilterService;
import com.example.hyemin.blinkling.Webview.WebviewFragment;

public class MainActivity extends ActionBarActivity {
    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    boolean light;//초기상태는 불이 꺼진 상태

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
                                fragment = new WebviewFragment();
                                break;

                            case R.id.navigation_friends:
                                fragment = new BookmarkFragment();
                                break;

                            case R.id.navigation_foodbank:
                                fragment = new SettingFragment();
                                break;

                        }
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_container, fragment).commit();
                        return true;
                    }
                });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                Toast toast;
                toast = Toast.makeText(this, item.getTitle() + " Clicked folder button!", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
            case R.id.notebook_add: {
                fragment = new InnerStorageFragment();
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
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
                Toast toast;
                toast = Toast.makeText(this, item.getTitle() + " Clicked bk button!", Toast.LENGTH_SHORT);
                toast.show();
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
                toast = Toast.makeText(this, item.getTitle() + " Clicked delete button!", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);

}


    public void changeToText() {
        fragment = new TextViewFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();


    }
}