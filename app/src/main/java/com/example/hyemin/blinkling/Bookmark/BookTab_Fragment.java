package com.example.hyemin.blinkling.Bookmark;


import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyemin.blinkling.MainActivity;
import com.example.hyemin.blinkling.R;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookTab_Fragment extends ListFragment {
    public static final String TAG = "BK";
    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;
    private InfoClass mInfoClass;
    private ArrayList<InfoClass> mInfoArr;
    private CustomAdapter mAdapter;
    private ListView mListView;
    private ExamDbFacade mFacade;
    MainActivity mainActivity;
    // Long click된 item의 index(position)을 기록한다.
    int selectedPos = -1;
    ArrayList<InfoClass> insertResult;
    ArrayList<InfoClass> selectResult;

    public BookTab_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web__tab_, container, false);

        mFacade = new ExamDbFacade(getActivity());
        mAdapter = new CustomAdapter(getActivity(), mFacade.getCursor(), false);
        mCursor = mFacade.getCursor();
        mListView = (ListView) view.findViewById(android.R.id.list);

        /*//데이터베이스 생성 및 오픈
        mDbOpenHelper = new DbOpenHelper(getActivity());
        try {
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mInfoArr = new ArrayList<InfoClass>();

        //dowhile문을 이용하여 Cursor의 내용을 다 InfoClass에 입력 후 infoclass를 arraylist에 add
        doWhileCursorToArray();

        //로그로 확인
        for (InfoClass i : mInfoArr) {
            Log.d(TAG, "ID =" + i._id + "\n");
            Log.d(TAG, "TITLE =" + i.title + "\n");
            Log.d(TAG, "DOCUMENT =" + i.document + "\n");
            Log.d(TAG, "CREATED_AT = " + i.created_at + "\n");
            Log.d(TAG, "UPDATED_AT = " + i.updated_at + "\n");
            Log.d(TAG, "POSITION = " + i.position + "\n");
        }

        //리스트뷰에 사용할 어댑터 초기화
        mAdapter = new CustomAdapter(getActivity(), mInfoArr);*/

        mListView.setAdapter(mAdapter);

        //리스트뷰로 db에 저장된 북마크들을 내보냄
        selectResult = mFacade.select();
        mAdapter.changeCursor(mFacade.getCursor());

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View v, int position, long arg3) {
                final int pos = position;

                mCursor.moveToPosition(pos);
                mFacade.delete(mCursor.getInt(mCursor.getColumnIndex(ExamDbContract.ExamDbEntry.ID)));
                mCursor = mFacade.getAll();

                mAdapter.swapCursor(mCursor);
                mListView.setAdapter(mAdapter);


                // selectResult.remove(pos);
                // mAdapter.notifyDataSetChanged();

                return false;
            }
        });
   /*    //리스트뷰의 아이템을 길게 눌렀을 경우 삭제하기 위해 롱클릭 리스너 따로 설정
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG, "position = " + position);

                //리스트뷰의 position은 0부터 시작하므로 1을 더함
                boolean result = mFacade.deleteColumn(position + 1);
                Log.i(TAG, "result = " + result);

                if (result) {
                    //정상적인 position을 가져왔을 경우 ArrayList의 position과 일치하는 index 정보를 remove
                    selectResult.remove(position);
                    //어댑터에 ArrayList를 다시 세팅 후 값이 변경됬다고 어댑터에 알림
                    mAdapter.setArrayList(selectResult);
                    mAdapter.notifyDataSetChanged();
                } else {
                    //잘못된 position을 가져왔을 경우 다시 확인 요청
                    Toast.makeText(getActivity(), "INDEX를 확인해 주세요", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
*/
        //mListView.setOnItemLongClickListener(new ListViewItemLongClickListener());


        return view;

    }

    private boolean checkString(String str) {
        boolean result = false;

        if (TextUtils.isEmpty(str)) {
            result = true;
        }

        return result;
    }

    public void getInsertValue() {
        Bundle extra = getArguments();
        String title = extra.getString("title");
        String document = extra.getString("document");
        String time_date = extra.getString("time_date");
        String position = extra.getString("position");
        insertResult = mFacade.insert(title, document, time_date, time_date, position);
        mAdapter.changeCursor(mFacade.getCursor());

    }

    /* @Override
     public void onClick(View v) {

         String str = mInputEditText.getText().toString();
         boolean isNull = checkString(str);

         if (v.getId() != R.id.select_btn) {
             if (isNull == true) {
                 Toast.makeText(MainActivity.this, "입력해주세요!", Toast.LENGTH_SHORT).show();
             }
         }

         switch (v.getId()) {
             // DB에 데이터를 삽입하는 버튼입니다.
             case R.id.insert_btn:
                 if (isNull == false) {
                     ArrayList<String> insertResult = mFacade.insert(str);
                     mAdapter.changeCursor(mFacade.getCursor());
                 }
                 break;
             // DB에서 모든 데이터를 조회하여 출력하는 버튼입니다.
             case R.id.select_btn:
                 ArrayList<String> selectResult = mFacade.select();
                 mAdapter.changeCursor(mFacade.getCursor());
                 break;
             // DB의 모든 데이터를 입력한 값으로 변경하는 버튼입니다.
             case R.id.update_btn:
                 if (isNull == false) {
                     ArrayList<String> updateResult = mFacade.update(str);
                     mAdapter.changeCursor(mFacade.getCursor());
                 }
                 break;
             // 입력한 값을 가지는 데이터를 삭제하는 버튼입니다.
             case R.id.delete_btn:
                 if (isNull == false) {
                     ArrayList<String> deleteResult = mFacade.delete(str);
                     mAdapter.changeCursor(mFacade.getCursor());
                 }
                 break;
         }

     }
     */
    public void checkBookmarkDelete() {
        int count, checked;
        count = mAdapter.getCount();

        if (count > 0) {
            //현재 선택된 아이템의 position 획득
            checked = mListView.getCheckedItemPosition();

            if (checked > -1 && checked < count) {
                // 아이템 삭제
                mInfoArr.remove(checked);

                //listview 선택 초기화
                mListView.clearChoices();

                //listview 갱신
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    //dowhile문을 이용하여 Cursor의 내용을 다 InfoClass에 입력 후 infoclass를 arraylist에 add
    private void doWhileCursorToArray() {
        mCursor = null;
        mCursor = mDbOpenHelper.getAllColumns();

        //칼럼의 갯수 확인
        Log.i(TAG, "Count = " + mCursor.getCount());

        while (mCursor.moveToNext()) {
            //infoclass에 입력된 값을 입력
            mInfoClass = new InfoClass(
                    mCursor.getInt(mCursor.getColumnIndex("_id")),
                    mCursor.getString(mCursor.getColumnIndex("title")),
                    mCursor.getString(mCursor.getColumnIndex("document")),
                    mCursor.getString(mCursor.getColumnIndex("created_at")),
                    mCursor.getString(mCursor.getColumnIndex("updated_at")),
                    mCursor.getString(mCursor.getColumnIndex("pos"))
            );
            //입력된 값을 가지고 있는 infoclass를 infoarray에 add
            mInfoArr.add(mInfoClass);
        }

        //mCursor.close();
    }


    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
            Log.i(TAG, "position = " + position);
            selectedPos = position;
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
            alertDlg.setTitle(R.string.alert_title_question);

            // '예' 버튼이 클릭되면
            alertDlg.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDbOpenHelper.deleteColumn(id);
                    mInfoArr.remove(selectedPos);
                    //mAdapter.setArrayList(mInfoArr);


                    // mListView.setAdapter(mAdapter);
                    // 아래 method를 호출하지 않을 경우, 삭제된 item이 화면에 계속 보여진다.
                    mAdapter.notifyDataSetChanged();

                    // mListView.clearChoices();
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
            });

            // '아니오' 버튼이 클릭되면
            alertDlg.setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
            });


            alertDlg.setMessage(String.format(getString(R.string.alert_msg_delete),
                    mInfoArr.get(position)));
            alertDlg.show();
            return false;
        }

    }

}
