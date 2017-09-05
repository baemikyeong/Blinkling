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

/**
 * Created by seohyemin on 2017. 8. 29..
 */

public class CustomAdapter extends CursorAdapter {
    private LayoutInflater mInflater;
    ArrayList<InfoClass> InfoArr = new ArrayList<InfoClass>();
    //private ViewHolder holder;
    int layoutResourceId;
    Context context;
    private SQLiteDatabase mDBHelper;
    private static final String DATABASE_NAME = "Bookmark_DB"; //DB이름
    private static final int DATABASE_VERSION = 1;//db 업데이트 할 때 사용함
    private Context mCtx;
    /**
     * 커서 어댑터의 여러가지 생성자 중 가장 많이 사용되는 생성자 입니다.
     * @param context : 컨텍스트
     * @param c : 데이터로 사용할 커서 객체
     * @param autoRequery : false
     */
    public CustomAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context,c,autoRequery);

        mInflater = LayoutInflater.from(context);
    }
/*
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
            holder.image = (ImageView) v.findViewById(R.id.iv_img);

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

       // holder.image.setImageDrawable(info.getImage());

        return v;
    }*/

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


/*
        final int position = cursor.getPosition();
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int idSpeaker;

             //   mDBHelper = new DbOpenHelper.DataBaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
                mDB = mDBHelper.getWritableDatabase();

                return false;
            }
        });
*/

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
        ImageView image;

    }

}
