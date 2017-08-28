/*
package com.example.hyemin.blinkling.Bookmark;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hyemin.blinkling.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by seohyemin on 2017. 8. 24..
 *//*


public class BookmarkListAdapter extends ArrayAdapter {

    private List<Bookmark_Info> bm = new ArrayList<Bookmark_Info>();
    private Context context;

    public BookmarkListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }


    @Override
    public int getCount() {
        return bm.size();
    }

    @Override
    public Object getItem(int position) {
        return this.bm.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {

        Holder holder = null;

        if (convertview == null) {
            // "listview_item" Layout을 inflate하여 convertView 참조 획득.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertview = inflater.inflate(R.layout.bookmark_listview_item, parent, false);

            //convertview = new LinearLayout(context);
            //((LinearLayout) convertview).setOrientation(LinearLayout.HORIZONTAL);

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            TextView textView1 = (TextView)convertview.findViewById(R.id.textView1);
            TextView textView2 = (TextView)convertview.findViewById(R.id.textView2);
            TextView textView3 = (TextView)convertview.findViewById(R.id.textView3);
           // TextView textView4 = (TextView)convertview.findViewById(R.id.textView4);

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            Bookmark_Info bookmark_info = (Bookmark_Info)bm.get(position);


*/
/*
            TextView id = new TextView(context);
            TextView title = new TextView(context);
            TextView pos = new TextView(context);
            TextView doc = new TextView(context);*//*

*/
/*
            ((LinearLayout) convertview).addView(id);
            ((LinearLayout) convertview).addView(title);
            ((LinearLayout) convertview).addView(pos);
            ((LinearLayout) convertview).addView(doc);*//*


            holder = new Holder();
            holder.id = textView1;
            holder.title = textView2;
            holder.pos = textView3;
           // holder.doc = textView4;

            convertview.setTag(holder);
        } else {
            holder = (Holder) convertview.getTag();
        }

        // 아이템 내 각 위젯에 데이터 반영
        Bookmark_Info bi = (Bookmark_Info) getItem(position);
        holder.id.setText(bi.get_id() + "");
        holder.title.setText(bi.getTitle() + "");
       // holder.pos.setText(bi.get_pos()+"");
        holder.doc.setText(bi.getDoc());

        return convertview;
    }


    private class Holder {
        public TextView id;
        public TextView title;
        public TextView pos;
        public TextView doc;

    }

}*/
