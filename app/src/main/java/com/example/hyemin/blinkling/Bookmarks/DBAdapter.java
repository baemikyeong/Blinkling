package com.example.hyemin.blinkling.Bookmarks;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hyemin.blinkling.R;

/**
 * Created by seohyemin on 2017. 8. 28..
 */

public class DBAdapter extends CursorAdapter {
    public DBAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.listview_item,viewGroup,false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
    //    final ImageView image = (ImageView)view.findViewById(R.id.imageView1);
        final TextView text1 = (TextView)view.findViewById(R.id.textView1);
        final TextView text2 = (TextView)view.findViewById(R.id.textView2);
        final TextView text3 = (TextView)view.findViewById(R.id.textView3);
        final TextView text4 = (TextView)view.findViewById(R.id.textView4);


      //  image.setImageResource(R.drawable.ic_turned_in_black_24dp);
        text4.setText(cursor.getString(cursor.getColumnIndex("ID")));
        text1.setText(cursor.getString(cursor.getColumnIndex("TITLE")));
        text2.setText(cursor.getString(cursor.getColumnIndex("TYPE")));
        text3.setText(cursor.getString(cursor.getColumnIndex("DOCUMENT")));

    }
}
