package com.example.hyemin.blinkling.Bookmarks;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hyemin.blinkling.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Book_Tab_Fragment extends Fragment {


    public Book_Tab_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book__tab_, container, false);
    }

}
