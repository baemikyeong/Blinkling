package com.example.hyemin.blinkling.Bookmark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hyemin.blinkling.R;

import java.util.ArrayList;

/**
 * Created by seohyemin on 2017. 8. 29..
 */

public class CustomAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<InfoClass> InfoArr;
    private ViewHolder holder;

    public CustomAdapter(Context c, ArrayList<InfoClass> array) {
        mInflater = LayoutInflater.from(c);
        InfoArr = array;
    }

    @Override
    public int getCount() {
        return InfoArr.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            holder = new ViewHolder();
            v = mInflater.inflate(R.layout.listview_item, null);

            holder.title = (TextView) v.findViewById(R.id.tv_title);
            holder.document = (TextView) v.findViewById(R.id.tv_doc);
            holder.date = (TextView) v.findViewById(R.id.tv_date);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        //InfoClass를 생성하여 각 뷰의 포지션에 맞는 데이터를 가져옴
        InfoClass info = InfoArr.get(position);

        //리스트뷰의 아이템에 맞는 String값을 입력
        holder.title.setText(info.title);
        holder.document.setText(info.document);
        holder.date.setText(info.created_at);

        return v;
    }

    //ArrayList Getter And Setter
    public void setArrayList(ArrayList<InfoClass> arrays) {
        this.InfoArr = arrays;
    }

    public ArrayList<InfoClass> getArrayList() {
        return InfoArr;
    }


    /**
     * ViewHolder Class 생성
     */
    private class ViewHolder {
        TextView title;
        TextView document;
        TextView date;
    }
}
