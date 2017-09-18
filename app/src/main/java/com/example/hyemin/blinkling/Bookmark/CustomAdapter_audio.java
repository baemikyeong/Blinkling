package com.example.hyemin.blinkling.Bookmark;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hyemin.blinkling.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by seohyemin on 2017. 8. 29..
 */

public class CustomAdapter_audio extends CursorAdapter {
    private LayoutInflater mInflater;
    ArrayList<InfoClass_audio> InfoArr = new ArrayList<InfoClass_audio>();
    RecordingItem item;


    /**
     * 커서 어댑터의 여러가지 생성자 중 가장 많이 사용되는 생성자 입니다.
     *
     * @param context     : 컨텍스트
     * @param c           : 데이터로 사용할 커서 객체
     * @param autoRequery : false
     */

    public CustomAdapter_audio(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        View v = mInflater.inflate(R.layout.listview_item_audio, parent, false);
        holder.title = (TextView) v.findViewById(R.id.tv_title);
        holder.date = (TextView) v.findViewById(R.id.tv_date);
        holder.image = (ImageView) v.findViewById(R.id.iv_img);
        holder.document = (TextView) v.findViewById(R.id.tv_doc);
        holder.pos = (TextView) v.findViewById(R.id.tv_pos);
       holder.path = (TextView) v.findViewById(R.id.file_length_text);

        v.setTag(holder);

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String title = cursor.getString(cursor.getColumnIndexOrThrow(ExamDbContract_audio.ExamDbEntry.COLUMN_NAME_RECORDING_NAME));
        String pos = cursor.getString(cursor.getColumnIndexOrThrow(ExamDbContract_audio.ExamDbEntry.POS));
        String creat = cursor.getString(cursor.getColumnIndexOrThrow(ExamDbContract_audio.ExamDbEntry.CREATED_AT));
        //  String update = cursor.getString(cursor.getColumnIndexOrThrow(ExamDbContract_audio.ExamDbEntry.UPDATED_AT));
        String document = cursor.getString(cursor.getColumnIndexOrThrow(ExamDbContract_audio.ExamDbEntry.DOCUMENT));
       String path = cursor.getString(cursor.getColumnIndexOrThrow(ExamDbContract_audio.ExamDbEntry.COLUMN_NAME_RECORDING_FILE_PATH));

      //  long minutes = TimeUnit.MILLISECONDS.toMinutes(Integer.parseInt(length));
      //  long seconds = TimeUnit.MILLISECONDS.toSeconds(Integer.parseInt(length)) - TimeUnit.MINUTES.toSeconds(minutes);

        viewHolder.title.setText(title);
        viewHolder.date.setText(creat);
        viewHolder.document.setText(document);
        viewHolder.pos.setText(pos);
        viewHolder.path.setText(path);

    }

    //ArrayList Getter And Setter
    public void setArrayList(ArrayList<InfoClass_audio> arrays) {
        this.InfoArr = arrays;
    }

    public ArrayList<InfoClass_audio> getArrayList() {
        return InfoArr;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

    }

    /**
     * ViewHolder Class 생성
     */
    private class ViewHolder {
        TextView title;
        TextView date;
        ImageView image;
        TextView pos;
        TextView document;
        TextView path;

    }

}
