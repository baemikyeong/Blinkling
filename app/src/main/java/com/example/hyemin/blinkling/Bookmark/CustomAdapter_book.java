package com.example.hyemin.blinkling.Bookmark;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hyemin.blinkling.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by seohyemin on 2017. 8. 29..
 */

public class CustomAdapter_book extends CursorAdapter {
    private LayoutInflater mInflater;
    ArrayList<InfoClass> InfoArr = new ArrayList<InfoClass>();
    /**
     * 커서 어댑터의 여러가지 생성자 중 가장 많이 사용되는 생성자 입니다.
     * @param context : 컨텍스트
     * @param c : 데이터로 사용할 커서 객체
     * @param autoRequery : false
     */
    public CustomAdapter_book(Context context, Cursor c, boolean autoRequery) {
        super(context,c,autoRequery);

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        View v = mInflater.inflate(R.layout.listview_item, parent, false);
        holder.title = (TextView) v.findViewById(R.id.tv_title);
        holder.document = (TextView) v.findViewById(R.id.tv_doc);
        holder.date = (TextView) v.findViewById(R.id.tv_date);
        holder.image = (ImageView) v.findViewById(R.id.iv_img);

        v.setTag(holder);

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();
        String title = cursor.getString(cursor.getColumnIndexOrThrow(ExamDbContract.ExamDbEntry.TITLE));
        String document = cursor.getString(cursor.getColumnIndexOrThrow(ExamDbContract.ExamDbEntry.DOCUMENT));
       // String pos = cursor.getString(cursor.getColumnIndexOrThrow(ExamDbContract.ExamDbEntry.POS));
       // String creat = cursor.getString(cursor.getColumnIndexOrThrow(ExamDbContract.ExamDbEntry.CREATED_AT));
        String update = cursor.getString(cursor.getColumnIndexOrThrow(ExamDbContract.ExamDbEntry.UPDATED_AT));

        viewHolder.title.setText(title);
        viewHolder.document.setText(document);
        viewHolder.date.setText(update);

    }

    //ArrayList Getter And Setter
    public void setArrayList(ArrayList<InfoClass> arrays) {
        this.InfoArr = arrays;
    }

    public ArrayList<InfoClass> getArrayList() {
        return InfoArr;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());

    }

    /**
     * ViewHolder Class 생성
     */
    private class ViewHolder {
        TextView title;
        TextView document;
        TextView date;
        ImageView image;

    }

}
