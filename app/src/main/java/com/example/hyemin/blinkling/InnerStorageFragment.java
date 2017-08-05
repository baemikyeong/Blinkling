package com.example.hyemin.blinkling;



import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


/**
 *
 * A simple {@link Fragment} subclass.
 *
 */
public class InnerStorageFragment extends ListFragment {
    ListView mFileListView;
    ArrayList<String> mArrayListFile;
    String mPath = "";
    String mRoot = "";
    String mBookName = "";
    String InStoragePath = Environment.getRootDirectory().getAbsolutePath();
    public InnerStorageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_inner_storage, container, false);
        mFileListView = (ListView) rootView.findViewById(android.R.id.list);
        mRoot = InStoragePath;
        //  ( (MainActivity)getActivity()).changeToBookshelf();
         findFolder();

   /* SD카드 접근시임
        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED)) {
            findFolder();
           // Activity root = getActivity();
           // Toast toast = Toast.makeText(root, "There is  SDcard!", Toast.LENGTH_SHORT);
          //  toast.show();

        } else {
            Activity root = getActivity();
            Toast toast = Toast.makeText(root, "There is no SDcard!", Toast.LENGTH_SHORT);
            toast.show();
        }
        */


        return rootView;
    }


    private void findFolder() {
        // Environment.getRootDirectory().getAbsolutePath()


        mArrayListFile = new ArrayList<String>();

        //String InStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File files = new File( InStoragePath );
        Activity root = getActivity();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(root, android.R.layout.simple_list_item_1, mArrayListFile);

        if (files.listFiles().length > 0) {
            for (File file : files.listFiles()) {
                mArrayListFile.add(file.getName());
            }
        }
        else {
            files = null;
        }
        setListAdapter(arrayAdapter);
    }


    public void onListItemClick(ListView Find_ListView, View view, int position, long id){
        String strItem = mArrayListFile.get(position);//returns the element at the specified position in this list
        String strPath = getAbsolutePath(strItem);// 선택된 폴더의 전체 경로를 구한다
        String[] fileList = getFileList(strPath);//선택된 폴더에 존재하는 파일 목록을 구한다
        ShowFileList(fileList); //파일 목록을 ListView 에 표시
    }

    public String getAbsolutePath(String strFolder) {
        String strPath;

        // 이전 폴더일때
        if( strFolder == ".." ) {
            // 전체 경로에서 최하위 폴더를 제거
            int pos = mPath.lastIndexOf("/");//마지막으로 문자가 나타난 인덱스를 리턴
            strPath = mPath.substring(0, pos);// 0이 시작, pos가 끝, 0부터 포스까지 섭스트링을 리턴
        }
        else
            strPath = mPath + "/" + strFolder;

        Activity root = getActivity();
        Toast toast = Toast.makeText(root, strPath, Toast.LENGTH_SHORT);
        toast.show();
        return strPath;//       /선택된 폴더 이름 리턴
    }



    public String[] getFileList(String strPath) {
        // 폴더 경로를 지정해서 File 객체 생성
        File fileRoot = new File(InStoragePath + strPath);

        // 해당 경로가 폴더가 아니라면 함수 탈출
        if( fileRoot.isDirectory() == false ) {//false즉 마지막 파일이라면 (디렉터리가 아니라)
            Activity root = getActivity();
            Toast toast = Toast.makeText(root, "IF문", Toast.LENGTH_SHORT);
            toast.show();
            //sd카드에는 텍스트 파일만 있다고 가정,   strPath이름의 텍스트 파일을 읽을거야
            int pos = strPath.lastIndexOf("/");
            strPath = strPath.substring(pos+1);//pos=시작인덱스, 포스부터 쭉 서브스트링을 리턴
            mBookName = strPath; //


            toast = Toast.makeText(root, mBookName, Toast.LENGTH_SHORT);
            toast.show();

//그러니까 경로의 마지막부분인 파일이름으로 읽는걸 들어가야 한다.
           Fragment txtFrag = new TextViewFragment();
            Bundle bundle = new Bundle();
            bundle.putString("name",mBookName);
            txtFrag.setArguments(bundle);

            ( (MainActivity)getActivity()).changeToText();

            return null;
        }
        mPath = strPath;
        //    mTextMsg.setText(mPath);
        // 파일 목록을 구한다
        String[] fileList = fileRoot.list();
        return fileList;
    }

    public void ShowFileList(String[] fileList) {
        if( fileList == null )
            return;
        mArrayListFile.clear();
        // 현재 선택된 폴더가 루트 폴더가 아니라면
        if( mRoot.length() < mPath.length() )//mPath가 선택한 폴더임 즉 길이가 더 김 mroot보다
            // 이전 폴더로 이동하기 위해서 ListView 에 ".." 항목을 추가
            mArrayListFile.add("..");

        for(int i=0; i < fileList.length; i++) {
            Log.d("tag", fileList[i]);
            mArrayListFile.add(fileList[i]);
        }
        ArrayAdapter adapter = (ArrayAdapter)getListView().getAdapter();
        adapter.notifyDataSetChanged();
    }




}
