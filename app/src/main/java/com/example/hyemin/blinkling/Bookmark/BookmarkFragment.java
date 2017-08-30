package com.example.hyemin.blinkling.Bookmark;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.hyemin.blinkling.Bookmarks.ListViewAdapter;
import com.example.hyemin.blinkling.R;

import java.util.ArrayList;
import java.util.List;

public class BookmarkFragment extends Fragment {
    private FragmentTabHost mTabHost;
    private EditText et_searchText;
    ListViewAdapter adapter;
    ViewPager viewPager;
    ListView listview;

    public BookmarkFragment() {
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    public void onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.voice_btn).setVisible(false);
        menu.findItem(R.id.eye_btn).setVisible(false);
        menu.findItem(R.id.light_btn).setVisible(false);
        menu.findItem(R.id.notebook_add).setVisible(false);
        menu.findItem(R.id.notebook_delete).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark,container, false);
        /*adapter = new ListViewAdapter();

        listview = (ListView) viewPager.findViewById(R.id.listView1);
        listview.setAdapter(adapter);
        */

        // Setting ViewPager for each Tabs
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Spinner s = (Spinner)view.findViewById(R.id.spinner1);
        /*s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv.setText("position : " + position + parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });*/

       /* et_searchText = (EditText)view.findViewById(R.id.et_searchText);
        et_searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString() ;
                if (filterText.length() > 0) {
                    listview.setFilterText(filterText) ;
                } else {
                    listview.clearTextFilter() ;
                }

            }
        });*/


        return view;
    }

    private void setupViewPager(ViewPager viewPager) {

        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new Web_Tab_Fragment(), "WEB");
        adapter.addFragment(new Book_Tab_Fragment(), "BOOK");
        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
