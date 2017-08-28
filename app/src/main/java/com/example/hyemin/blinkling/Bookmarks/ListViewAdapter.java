package com.example.hyemin.blinkling.Bookmarks;


/**
 * Created by mac on 2017. 8. 7..
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.hyemin.blinkling.Bookmarks.Bookmark_Info;
import com.example.hyemin.blinkling.R;

import java.util.ArrayList;

/**
 * Created by Hyemin on 2017-07-13.
 */

public class ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Bookmark_Info> mListData;
    private LayoutInflater inflater;
    private Holder viewHolder;

    private Context context = null;

    // ListViewAdapter의 생성자
    public ListViewAdapter(Context mContext, ArrayList<Bookmark_Info> array) {
        inflater = LayoutInflater.from(mContext);
        mListData = array;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return mListData.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            viewHolder = new Holder();
            v = inflater.inflate(R.layout.listview_item, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.textView1);
            viewHolder.type = (TextView) convertView.findViewById(R.id.textView2);
            viewHolder.docu = (TextView) convertView.findViewById(R.id.textView3);
            v.setTag(viewHolder);
        } else {
            viewHolder = (Holder) v.getTag();
        }

        viewHolder.title.setText(mListData.get(position).title);
        viewHolder.type.setText(mListData.get(position).type);
        viewHolder.docu.setText(mListData.get(position).doc);

        return v;


/*



        Holder holder;

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            holder = new Holder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, null);

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
          *//*  ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
            TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;
            TextView descTextView = (TextView) convertView.findViewById(R.id.textView2) ;*//*

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            holder.type = (ImageView) convertView.findViewById(R.id.imageView1) ;
            holder.title = (TextView) convertView.findViewById(R.id.textView1) ;
            holder.docu = (TextView) convertView.findViewById(R.id.textView2) ;


            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
          //  ListViewItem listViewItem = listViewItemList.get(position);

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
           *//* ListViewItem listViewItem = (ListViewItem) bm.get(pos);*//*

         *//*   // 아이템 내 각 위젯에 데이터 반영
            iconImageView.setImageDrawable(listViewItem.getIcon());
            titleTextView.setText(listViewItem.getTitle());
            descTextView.setText(listViewItem.getDesc());*//*

            convertView.setTag(holder);
        }else {
            holder = (Holder)convertView.getTag();
        }
        // 아이템 내 각 위젯에 데이터 반영
        ListViewItem listViewItem = (ListViewItem) bm.get(position);

        if(listViewItem.iconDrawable != null){
            holder.type.setVisibility(View.VISIBLE);
            holder.type.setImageDrawable(listViewItem.iconDrawable);

        }else{
            holder.type.setVisibility(View.GONE);
        }
        holder.title.setText(listViewItem.getTitle());
        holder.docu.setText(listViewItem.getDesc());

        return convertView;*/
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    public void setArrayList(ArrayList<Bookmark_Info> arrays){
        this.mListData = arrays;
    }

    public ArrayList<Bookmark_Info> getArrayList(){
        return mListData;
    }


    class Holder {
        public TextView title;
        public TextView docu;
        public TextView type;
    }


}
