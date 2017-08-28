package com.example.hyemin.blinkling.Bookmarks;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyemin.blinkling.Bookmark.ListViewItem;
import com.example.hyemin.blinkling.R;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Web_Tab_Fragment extends ListFragment {
    private DbOpenHelper bookmark_DB;
    private ArrayList<Bookmark_Info> mInfoArray;
    private Bookmark_Info bookmark_info;
    private ListViewAdapter mAdapter;
    private DbOpenHelper mDbOpenHelper;
    private TextView tv1;
    private ListView mListView;

    ListView listview ;
    ListViewAdapter adapter;
    Cursor mCursor;
    String sql;

    final static String dbName = "BOOKMARK_DB";
    final static int dbVersion = 1;

    List<Bookmark_Info> bm_list;
    private Object mMyData;
    SQLiteDatabase db;

   // private static String DB_PATH = "/data/data/com.example.hyemin.blinkling/databases/"
    private final String DB = "/data/com.example.hyemin.blinkling/databases/BOOKMARK_DB";
    private final String TABLE_NAME = "BOOKMARK_TABLE";
    public Web_Tab_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web__tab_, container, false);

        //데이터베이스 생
        bookmark_DB = new DbOpenHelper(getActivity());
        bookmark_DB.open();

        mInfoArray = new ArrayList<Bookmark_Info>();
        doWhileCursorToArray();

        for(Bookmark_Info i : mInfoArray){
            Log.d(TAG,"ID =" + i._id);
            Log.d(TAG,"TITLE ="+i.title);
            Log.d(TAG,"TYPE ="+i.type);
            Log.d(TAG,"DOCUMENT = "+i.doc);
        }

        mAdapter = new ListViewAdapter(getContext(),mInfoArray);

        listview = (ListView)getActivity().findViewById(android.R.id.list);
        listview.setAdapter(mAdapter);
       // listview.setOnItemLongClickListener(longClickListener);


        //데이터베이스 생성
     //   bookmark_db = new Bookmark_DB(MainActivity.this, "BOOKMARK_DB", null, 1);



        selectDB();
      //  bm_list = new ArrayList<>();
/*
        File outFile = new File(Environment.getDataDirectory(),DB);
        outFile.setWritable(true);
        mSampleDB = SQLiteDatabase.openDatabase(outFile.getAbsolutePath(),null,SQLiteDatabase.OPEN_READWRITE);
        mCursor = mSampleDB.rawQuery("SELECT _ID, TITLE, POSITION, DOCUMENT FROM "+TABLE_NAME,null);

        if(mCursor.moveToFirst()){
            do{
                String title,position,doc;
                int _id;
                _id = mCursor.getInt(0);
                title = mCursor.getString(1);
                position = mCursor.getString(2);
                doc = mCursor.getString(3);
            }while (mCursor.moveToNext());
        }*/
     /*   adapter = new ListViewAdapter(getActivity());
      //listview = (ListView)view.findViewById(android.R.id.list);
        listview.setAdapter(adapter);

*/
        return view;

/*



        bm_list = bookmark_db.getAllBookmarkData();

        // Adapter 생성 및 Adapter 지정.
        //adapter = new ListViewAdapter();
        adapter = new BookmarkListAdapter(bm_list,getActivity());
        listview = (ListView)view.findViewById(android.R.id.list);

        //리스트뷰를 보여준다
        listview.setVisibility(View.VISIBLE);

        if (bookmark_db == null) {
            bookmark_db = new Bookmark_DB(getActivity(), "TEST", null, 1);
        }


        listview.setAdapter(adapter);
       // listview.setAdapter(new ListViewAdapter());


     //   listview.setAdapter(adapter) ;


        return view;*/
        /* // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mic_black_24dp)
                , "Box", "Account Box Black 36dp") ;

        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_turned_in_black_24dp)
                ,"Circle", "Account Circle Black 36dp") ;

        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mic_black_24dp)
                , "Ind", "Assignment Ind Black 36dp") ;
*/
    }

/*
    *//**
     * ListView의 Item을 롱클릭 할때 호출 ( 선택한 아이템의 DB 컬럼과 Data를 삭제 한다. )
     *//*
    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

            Log.d(TAG, "position = "+position);

            boolean result = mDbOpenHelper.deleteColumn(position + 1);
            Log.e(TAG, "result = " + result);

            if(result){
                mInfoArray.remove(position);
                mAdapter.setArrayList(mInfoArray);
                mAdapter.notifyDataSetChanged();
            }else {
                Toast.makeText(getContext(), "INDEX를 확인해 주세요.",
                        Toast.LENGTH_LONG).show();
            }

            return false;
        }
    };*/

    //DB에서 받아온 값들을 ArrayList에 Add
    private void doWhileCursorToArray(){
        mCursor = null;
        mCursor = bookmark_DB.getAllColumns();

        while (mCursor.moveToNext()){
            bookmark_info = new Bookmark_Info(
                 /*   mCursor.getInt(mCursor.getColumnIndex("_id")),*/
                    mCursor.getString(mCursor.getColumnIndex("title")),
                    mCursor.getString(mCursor.getColumnIndex("type")),
                    mCursor.getString(mCursor.getColumnIndex("document"))
                    );
            mInfoArray.add(bookmark_info);

        }

        mCursor.close();
    }


    public void selectDB(){

        //db = bookmark_db.getReadableDatabase();
        sql = "SELECT TITLE, DOCUMENT FROM BOOKMARK_TABLE;";

        mCursor = db.rawQuery(sql,null);
        if(mCursor.getCount() > 0){
            DBAdapter dbAdapter = new DBAdapter(getActivity(),mCursor);
            listview.setAdapter(dbAdapter);

        }
    }

    private class ViewHolder {
        public ImageView mIcon;
        public TextView mText;
        public TextView mDate;
    }

    public void init(View v) {

        // Setup the listAdapter
     /*   adapter = new BookmarkListAdapter();
        listview.setAdapter(adapter);*/

    }


    public void onListItemClick(ListView l, View vm, int position, long id) {
        ListViewItem item = (ListViewItem) l.getItemAtPosition(position);

        String titleStr = item.getTitle();
        String descStr = item.getDesc();
        Drawable iconDrawable = item.getIcon();
        Toast.makeText(getContext(),titleStr+"   "+descStr+"    "+iconDrawable,Toast.LENGTH_SHORT).show();

    }


}
