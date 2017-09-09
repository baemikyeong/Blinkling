package com.example.hyemin.blinkling.Bookmark;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hyemin.blinkling.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AudioTab_Fragment extends Fragment {


    public AudioTab_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_tab_, container, false);
    }

}
