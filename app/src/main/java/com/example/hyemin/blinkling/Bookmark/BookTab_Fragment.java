package com.example.hyemin.blinkling.Bookmark;


import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hyemin.blinkling.MainActivity;
import com.example.hyemin.blinkling.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookTab_Fragment extends ListFragment {
    public static final String TAG = "BK";
    private Cursor mCursor;
    private CustomAdapter_book mAdapter;
    private ListView mListView;
    private ExamDbFacade mFacade;
    EditText editSearch;
    // Long click된 item의 index(position)을 기록한다.
    int selectedPos = -1;
    ArrayList<InfoClass> insertResult;
    ArrayList<InfoClass> selectResult;
    Spinner s;

    public BookTab_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_tab, container, false);


        //데이터베이스 생성 및 오픈
        mFacade = new ExamDbFacade(getActivity());
        mAdapter = new CustomAdapter_book(getActivity(), mFacade.getCursor(), false);
        mCursor = mFacade.getCursor();
        mListView = (ListView) view.findViewById(android.R.id.list);

        mListView.setAdapter(mAdapter);


        s = (Spinner) view.findViewById(R.id.spinner1);//스피너 설정

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position){
                   /* case 0:
                        Toast.makeText(getActivity(),"최신순",Toast.LENGTH_SHORT).show();
                        break;*/
                    case 1:
                        Toast.makeText(getActivity(),"이름별",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(),"문서별",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(),"최신순",Toast.LENGTH_SHORT).show();
            }
        });

        editSearch = (EditText) view.findViewById(R.id.search);//검색창 설정
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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

                                    dialog.dismiss();
                                }
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

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //리스트의 아이템을 눌렀을 때 텍스트뷰의 해당 위치로 이동한다
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final int pos = position;

                mCursor.moveToPosition(pos);
                String book_title = mCursor.getString(mCursor.getColumnIndex(ExamDbContract.ExamDbEntry.ID));

                ((MainActivity) getActivity()).changeToText(book_title);

            }
        });
    }

}
