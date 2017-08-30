package com.example.hyemin.blinkling.Bookmark;


import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hyemin.blinkling.R;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Web_Tab_Fragment extends ListFragment {
    private static final String TAG = "TestDataBase";
    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;
    private InfoClass mInfoClass;
    private ArrayList<InfoClass> mInfoArr;
    private CustomAdapter mAdapter;
    private ListView mListView;


    public Web_Tab_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_web__tab_,container, false);
       View view = inflater.inflate(R.layout.fragment_web__tab_, container, false);

        mListView = (ListView)view.findViewById(android.R.id.list);

        //데이터베이스 생성 및 오픈
        mDbOpenHelper = new DbOpenHelper(getActivity());
        try{
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mInfoArr = new ArrayList<InfoClass>();

        //dowhile문을 이용하여 Cursor의 내용을 다 InfoClass에 입력 후 infoclass를 arraylist에 add
        doWhileCursorToArray();

        //로그로 확인
        for(InfoClass i : mInfoArr){
            Log.d(TAG,"ID =" + i._id+"\n");
            Log.d(TAG,"TITLE ="+i.title+"\n");
            Log.d(TAG,"DOCUMENT ="+i.document+"\n");
            Log.d(TAG,"CREATED_AT = "+i.created_at+"\n");
            Log.d(TAG,"UPDATED_AT = "+i.updated_at+"\n");
            Log.d(TAG, "POSITION = "+i.position+"\n");
        }

        //리스트뷰에 사용할 어댑터 초기화
        mAdapter = new CustomAdapter(getActivity(),mInfoArr);

        mListView.setAdapter(mAdapter);

        //리스트뷰의 아이템을 길게 눌렀을 경우 삭제하기 위해 롱클릭 리스너 따로 설정
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                                           View view, int position, long id) {
                Log.i(TAG, "position = " + position);
                //리스트뷰의 position은 0부터 시작하므로 1을 더함
                boolean result = mDbOpenHelper.deleteColumn(position + 1);
                Log.i(TAG, "result = " + result);

                if (result) {
                    //정상적인 position을 가져왔을 경우 ArrayList의 position과 일치하는 index 정보를 remove
                    mInfoArr.remove(position);
                    //어댑터에 ArrayList를 다시 세팅 후 값이 변경됬다고 어댑터에 알림
                    mAdapter.setArrayList(mInfoArr);
                    mAdapter.notifyDataSetChanged();
                } else {
                    //잘못된 position을 가져왔을 경우 다시 확인 요청
                    Toast.makeText(getActivity(), "INDEX를 확인해 주세요", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        return view;

    }


    //dowhile문을 이용하여 Cursor의 내용을 다 InfoClass에 입력 후 infoclass를 arraylist에 add
    private void doWhileCursorToArray(){
        mCursor = null;
        mCursor = mDbOpenHelper.getAllColumns();

        //칼럼의 갯수 확인
       Log.i(TAG, "Count = "+ mCursor.getCount());

        while (mCursor.moveToNext()){
            //infoclass에 입력된 값을 입력
            mInfoClass = new InfoClass(
                    mCursor.getInt(mCursor.getColumnIndex("_id")),
                    mCursor.getString(mCursor.getColumnIndex("title")),
                    mCursor.getString(mCursor.getColumnIndex("document")),
                    mCursor.getString(mCursor.getColumnIndex("created_at")),
                    mCursor.getString(mCursor.getColumnIndex("updated_at")),
                    mCursor.getString(mCursor.getColumnIndex("position"))
                    );
            //입력된 값을 가지고 있는 infoclass를 infoarray에 add
            mInfoArr.add(mInfoClass);
        }

        mCursor.close();
    }


}
