package com.example.hyemin.blinkling;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class TextViewFragment extends Fragment {
    String bookName = "";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            bookName = getArguments().getString("bookname");
        }
    }

    public TextViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_text_view, container, false);

        Activity root = getActivity();
        Toast toast = Toast.makeText(root, bookName, Toast.LENGTH_SHORT);
         toast.show();


        TextView tv = (TextView)rootView.findViewById(R.id.txtview);




        File dir = Environment.getRootDirectory().getAbsoluteFile();
        //File yourFile = new File(dir, "path/to/the/file/inside/the/sdcard.ext");

        //Get the text file
        //Intent intent = getIntent();


      //  String bookName = getArguments().getString("bookname");
        //Toast.makeText(this, textName, Toast.LENGTH_SHORT).show();
        File file = new File(dir,bookName);
        // i have kept text.txt in the sd-card

        if(file.exists())   // check if file exist
        {
            //Read text from file
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('n');
                }
            }
            catch (IOException e) {
                //You'll need to add proper error handling here
            }
            //Set the text
            tv.setText(text);
        }
        else
        {
            tv.setText("Sorry file doesn't exist!!");
        }



        return rootView;
    }

}