package com.example.hyemin.blinkling.Bookmark;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hyemin.blinkling.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Web_Tab_Fragment extends ListFragment {
    ListView listview ;
    ListViewAdapter adapter;


    public Web_Tab_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web__tab_, container, false);

        // Adapter 생성 및 Adapter 지정.
        adapter = new ListViewAdapter();
        listview = (ListView)view.findViewById(android.R.id.list);
        listview.setAdapter(adapter) ;

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mic_black_24dp)
                , "Box", "Account Box Black 36dp") ;

        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_turned_in_black_24dp)
                ,"Circle", "Account Circle Black 36dp") ;

        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mic_black_24dp)
                , "Ind", "Assignment Ind Black 36dp") ;

        return view;
    }

    public void onListItemClick(ListView l, View vm, int position, long id) {
        ListViewItem item = (ListViewItem) l.getItemAtPosition(position);

        String titleStr = item.getTitle();
        String descStr = item.getDesc();
        Drawable iconDrawable = item.getIcon();
        Toast.makeText(getContext(),titleStr+"   "+descStr+"    "+iconDrawable,Toast.LENGTH_SHORT).show();

    }


}
