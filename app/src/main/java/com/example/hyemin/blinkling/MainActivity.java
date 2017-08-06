package com.example.hyemin.blinkling;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.folder);

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
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeToText(){
        fragment = new TextViewFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();


    }
}