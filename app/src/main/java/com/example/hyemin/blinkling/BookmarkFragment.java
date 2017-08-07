package com.example.hyemin.blinkling;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

public class BookmarkFragment extends ListFragment {
    ListViewAdapter adapter;
    public BookmarkFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Adapter 생성 및 Adapter 지정.
        adapter = new ListViewAdapter() ;
        setListAdapter(adapter) ;

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.bookmark)
                , "Box", "Account Box Black 36dp") ;

        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.bookmark)
                ,"Circle", "Account Circle Black 36dp") ;

        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.bookmark)
                , "Ind", "Assignment Ind Black 36dp") ;

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.bookmark_btn).setVisible(false);
        menu.findItem(R.id.voice_btn).setVisible(false);
        menu.findItem(R.id.eye_btn).setVisible(false);
        menu.findItem(R.id.light_btn).setVisible(false);
        menu.findItem(R.id.notebook_add).setVisible(false);
        menu.findItem(R.id.notebook_delete).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    public void onListItemClick(ListView l, View vm, int position, long id) {
        ListViewItem item = (ListViewItem) l.getItemAtPosition(position);

        String titleStr = item.getTitle();
        String descStr = item.getDesc();
        Drawable iconDrawable = item.getIcon();
        Toast.makeText(getContext(),titleStr+"   "+descStr+"    "+iconDrawable,Toast.LENGTH_SHORT).show();

    }

    public void addItem(Drawable icon, String title, String desc){
        adapter.addItem(icon, title, desc);
    }
}
