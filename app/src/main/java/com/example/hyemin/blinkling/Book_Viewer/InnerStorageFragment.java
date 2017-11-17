package com.example.hyemin.blinkling.Book_Viewer;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hyemin.blinkling.MainActivity;
import com.example.hyemin.blinkling.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InnerStorageFragment extends ListFragment {
    File dir = Environment.getExternalStorageDirectory().getAbsoluteFile();
    ListView mFileListView;
    ArrayList<String> mArrayListFile;
    String mPath = "";
    String mRoot = "";
    String mBookName = "";//사용자가 클릭한 북네임
    String InStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public InnerStorageFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String param1) {
        TextViewFragment frag = new TextViewFragment();
        Bundle args = new Bundle();
        args.putString("bookname",param1);
        frag.setArguments(args);
        return frag;
    }


    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.bookmark_btn).setVisible(false);
        menu.findItem(R.id.voice_btn).setVisible(false);
        menu.findItem(R.id.eye_btn).setVisible(false);
        menu.findItem(R.id.light_btn).setVisible(false);
        menu.findItem(R.id.notebook_add).setVisible(false);
        menu.findItem(R.id.webmark_add).setVisible(false);
        menu.findItem(R.id.edit_scroll_range).setVisible(false);

        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_inner_storage, container, false);
        mFileListView = (ListView) rootView.findViewById(android.R.id.list);
        mRoot = InStoragePath;

        // SD카드 접근시임
        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED)) {
            findFolder();

        } else {
            Activity root = getActivity();
            Toast toast = Toast.makeText(root, "There is no SDcard!", Toast.LENGTH_SHORT);
            toast.show();
        }

        return rootView;
    }


    private void findFolder() {

        mArrayListFile = new ArrayList<String>();

        File files = new File(InStoragePath);
        Activity root = getActivity();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(root, android.R.layout.simple_list_item_1, mArrayListFile);

        if (files.listFiles().length > 0) {
            for (File file : files.listFiles()) {
                mArrayListFile.add(file.getName());
            }
        }
        setListAdapter(arrayAdapter);
    }

    public void onListItemClick(ListView Find_ListView, View view, int position, long id) { // 폴더 클릭이벤트
        String strItem = mArrayListFile.get(position); // position은 0부터 시작 position 번째 아이템 이름을 리턴함
        //Toast.makeText(getActivity(), strItem+"번 아이템 클릭", Toast.LENGTH_SHORT).show();// 폴더 이름 토스트 됨
        String strPath = getAbsolutePath(strItem); // 선택된 폴더의 전체 경로를 구한다

        String[] fileList = getFileList(strPath); // 선택된 폴더에 존재하는 파일 목록을 구한다. getFileList에서 블링클링 폴더에 파일 이동시킴
     // Toast.makeText(getActivity(), fileList[0]+"번 파일", Toast.LENGTH_SHORT).show();// 폴더 안의 파일 줄 0번째 파일 이름 토스트 됨
        ShowFileList(fileList); //파일 목록을 ListView 에 표시
    }

    public String[] fileList_check(String[] book_list){
        int i = 0;
        String temp = null;

        while(true){
            if(book_list[i].equals(temp)){

            }
            temp = book_list[i];


        }
    }



    public String getAbsolutePath(String strFolder) {
        String strPathComp;

        // 이전 폴더일때
        if (strFolder == "..") {
            // 전체 경로에서 최하위 폴더를 제거
            int pos = mPath.lastIndexOf("/");//마지막으로 문자가 나타난 인덱스를 리턴
            strPathComp = mPath.substring(0, pos);// 0이 시작, pos가 끝, 0부터 포스까지 섭스트링을 리턴
        } else
            strPathComp = mPath + "/" + strFolder;

        return strPathComp;//       /선택된 폴더 이름 리턴
    }


    public String[] getFileList(String strPath) {
        // 폴더 경로를 지정해서 File 객체 생성
        File fileRoot = new File(InStoragePath + strPath);

        // 해당 경로가 폴더가 아니라면 함수 탈출
        if (fileRoot.isDirectory() == false) {//false즉 마지막 파일이라면 (디렉터리가 아니라)
            Activity root = getActivity();
            //sd카드에는 텍스트 파일만 있다고 가정,   strPath이름의 텍스트 파일을 읽을거야
            int pos = strPath.lastIndexOf("/");
            strPath = strPath.substring(pos+1);//pos=시작인덱스, 포스부터 쭉 서브스트링을 리턴

            int txt = strPath.lastIndexOf("txt");
            int pdf = strPath.lastIndexOf("pdf");

            mBookName = strPath;

            if(txt != -1){
                fileMove(InStoragePath + "/" + mBookName, InStoragePath+"/Blinkling" + "/" + mBookName); //블링클링 폴더에 book 옮

                //mBookName이 이름을 북쉘프에 띄워야해!!
                ((MainActivity)getActivity()).sendBookname(mBookName);

                return null;

            }
            else if(pdf != -1){
                File file = new File(dir, mBookName);

                Toast toast = Toast.makeText(root, mBookName, Toast.LENGTH_SHORT);

                if (file.exists()){
                    Uri path = Uri.fromFile(file);

                    Intent intent =new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    try{
                        startActivity(intent);
                    }catch(ActivityNotFoundException ex){
                        toast.makeText(root, "pdf파일을 보기위한 뷰어 앱이 없습니다.",Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    toast.makeText(root, "pdf파일이 없습니다.",Toast.LENGTH_SHORT).show();
                }
            }


            return null;
        }
        mPath = strPath; //InStoragePath + strPath
        // 파일 목록을 구한다
        String[] fileList = fileRoot.list();
        return fileList;
    }


    public static void fileMove(String inFileName, String outFileName) {
        try {
            FileInputStream fis = new FileInputStream(inFileName);
            FileOutputStream fos = new FileOutputStream(outFileName);

            int data = 0;
            while((data=fis.read())!=-1) {
                fos.write(data);
            }
            fis.close();
            fos.close();

            //복사한뒤 원본파일을 삭제함
           // fileDelete(inFileName);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void fileDelete(String deleteFileName) {
        File I = new File(deleteFileName);
        I.delete();
    }


    public static void fileDelete_in_Blinkling(String deleteFileName) {
        File I = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Blinkling"+"/"+deleteFileName);
       // Toast.makeText(getActivity(),deleteFileName,Toast.LENGTH_SHORT ).show();
        I.delete();
    }


    public void ShowFileList(String[] fileList) {
        if (fileList == null)
            return;
        mArrayListFile.clear();

        // 현재 선택된 폴더가 루트 폴더가 아니라면
        if (mRoot.length() < (InStoragePath+mPath).length())//mPath가 선택한 폴더임 즉 길이가 더 김 mroot보다
            // 이전 폴더로 이동하기 위해서 ListView 에 ".." 항목을 추가
            mArrayListFile.add("..");

        for (int i = 0; i < fileList.length; i++) {
            Log.d("tag", fileList[i]);
            mArrayListFile.add(fileList[i]);
         //   Toast.makeText(getActivity(),fileList[i]+"번 책 추가 됨", Toast.LENGTH_SHORT).show();// 리스트에 파일 add 확인

        }
        ArrayAdapter adapter = (ArrayAdapter) getListView().getAdapter();
        adapter.notifyDataSetChanged();
    }


}