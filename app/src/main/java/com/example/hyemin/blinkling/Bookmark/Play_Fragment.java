package com.example.hyemin.blinkling.Bookmark;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hyemin.blinkling.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Play_Fragment extends DialogFragment {


    public Play_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_, container);

        return view;
    }

}
