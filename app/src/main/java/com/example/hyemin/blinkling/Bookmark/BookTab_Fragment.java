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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyemin.blinkling.MainActivity;
import com.example.hyemin.blinkling.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookTab_Fragment extends ListFragment {
    public static final String TAG = "BK";
    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;
    private CustomAdapter mAdapter;
    private ListView mListView;
    private ExamDbFacade mFacade;
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

        //데이터베이스 생성 및 오픈
        mFacade = new ExamDbFacade(getActivity());
        mAdapter = new CustomAdapter(getActivity(), mFacade.getCursor(), false);
        mCursor = mFacade.getCursor();
        mListView = (ListView) view.findViewById(android.R.id.list);

        mListView.setAdapter(mAdapter);

        //스와이핑해서 아이템 삭제하기
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(mListView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    final int pos = position;

                                    mCursor.moveToPosition(pos);
                                    mFacade.delete(mCursor.getInt(mCursor.getColumnIndex(ExamDbContract.ExamDbEntry.ID)));

                                }
                                mCursor = mFacade.getAll();
                                mAdapter.swapCursor(mCursor);
                                mListView.setAdapter(mAdapter);
                            }
                        });
        mListView.setOnTouchListener(touchListener);
        mListView.setOnScrollListener(touchListener.makeScrollListener());


        //리스트뷰로 db에 저장된 북마크들을 내보냄
        selectResult = mFacade.select();
        mAdapter.changeCursor(mFacade.getCursor());

        //롱클릭했을때 북마크 이름 편집
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View v, int position, long arg3) {
                final int pos = position;

                mCursor.moveToPosition(pos);

                final EditText editText = new EditText(getActivity());
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(v.getContext());
                alertDlg.setTitle(R.string.alert_title_question)
                        .setView(editText)
                        .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newTitle = editText.getText().toString();//새롭게 설정할 북마크의 이름

                                if (newTitle != null) {
                                    mFacade.editTitle(mCursor.getInt(mCursor.getColumnIndex(ExamDbContract.ExamDbEntry.ID)), newTitle);

                                    mCursor = mFacade.getAll();

                                    mAdapter.swapCursor(mCursor);
                                    mListView.setAdapter(mAdapter);

                                    dialog.dismiss();  // AlertDialog를 닫는다.
                                } /*else {
                                    AlertDialog.Builder ab = null;
                                    ab = new AlertDialog.Builder( v.getContext() );
                                    ab.setMessage("이름을 입력해주세요");
                                    ab.setPositiveButton(android.R.string.ok, null);
                                    ab.show();
                                }*/

                            }
                        });

                // '아니오' 버튼이 클릭되면
                alertDlg.setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();  // AlertDialog를 닫는다.
                    }
                });

                alertDlg.create().show();
                return false;

            }
        });


        return view;

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
      /*          final int pos = position;

                mCursor.moveToPosition(pos);

                mFacade.delete(mCursor.getInt(mCursor.getColumnIndex(ExamDbContract.ExamDbEntry.ID)));
                mCursor = mFacade.getAll();

                mAdapter.swapCursor(mCursor);
                mListView.setAdapter(mAdapter);

                Toast.makeText(getActivity(), "클릭한 position : " + position, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "클릭한 position : " + position);*/
            }
        });
    }

/*
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
*/
}
