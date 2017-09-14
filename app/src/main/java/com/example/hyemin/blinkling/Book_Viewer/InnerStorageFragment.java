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
    String strPathComp = "";
    // String InStoragePath = Environment.getRootDirectory().getAbsolutePath();
    String InStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //    Fragment frag = new TextViewFragment();
//    Bundle bundle = new Bundle();
//            bundle.putString("bookname",mBookName);
//            frag.setArguments(bundle);
//
//             ( (MainActivity)getActivity()).changeToText();
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
        menu.findItem(R.id.notebook_delete).setVisible(false);
        menu.findItem(R.id.bookmark_delete).setVisible(false);
        menu.findItem(R.id.webmark_add).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_inner_storage, container, false);
        mFileListView = (ListView) rootView.findViewById(android.R.id.list);
        mRoot = InStoragePath;
        //  ( (MainActivity)getActivity()).changeToBookshelf();
        //    findFolder();

        // SD카드 접근시임
        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED)) {
            findFolder();
            Activity root = getActivity();


        } else {
            Activity root = getActivity();
            Toast toast = Toast.makeText(root, "There is no SDcard!", Toast.LENGTH_SHORT);
            toast.show();
        }



        return rootView;
    }


    private void findFolder() {
        // Environment.getRootDirectory().getAbsolutePath()


        mArrayListFile = new ArrayList<String>();

        //String InStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File files = new File(InStoragePath);
        Activity root = getActivity();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(root, android.R.layout.simple_list_item_1, mArrayListFile);

        if (files.listFiles().length > 0) {
            for (File file : files.listFiles()) {
                mArrayListFile.add(file.getName());
            }
        } else {
            files = null;
        }
        setListAdapter(arrayAdapter);
    }

    public void onListItemClick(ListView Find_ListView, View view, int position, long id) {
        String strItem = mArrayListFile.get(position);//position은 0부터 시작 position 번째 아이템 이름을 리턴함
        String strPath = getAbsolutePath(strItem);// 선택된 폴더의 전체 경로를 구한다
        String[] fileList = getFileList(strPath);//선택된 폴더에 존재하는 파일 목록을 구한다
        ShowFileList(fileList); //파일 목록을 ListView 에 표시
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

        Activity root = getActivity();

        return strPathComp;//       /선택된 폴더 이름 리턴
    }


    public String[] getFileList(String strPath) {
        // 폴더 경로를 지정해서 File 객체 생성
        File fileRoot = new File(InStoragePath + strPath);

        // 해당 경로가 폴더가 아니라면 함수 탈출
        if (fileRoot.isDirectory() == false) {//false즉 마지막 파일이라면 (디렉터리가 아니라)
            Activity root = getActivity();
//            Toast toast = Toast.makeText(root, "IF문", Toast.LENGTH_SHORT);
//            toast.show();
            //sd카드에는 텍스트 파일만 있다고 가정,   strPath이름의 텍스트 파일을 읽을거야
            int pos = strPath.lastIndexOf("/");
            strPath = strPath.substring(pos+1);//pos=시작인덱스, 포스부터 쭉 서브스트링을 리턴

            int txt = strPath.lastIndexOf("txt");
            int pdf = strPath.lastIndexOf("pdf");

//            pos = strPath.lastIndexOf(".");
//            strPath = strPath.substring(0,pos);
            mBookName = strPath; //



//그러니까 경로의 마지막부분인 파일이름으로 읽는걸 들어가야 한다.
            //  newInstance(mBookName);
//            Fragment frag = new TextViewFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("bookname",mBookName);
//            frag.setArguments(bundle);

            if(txt != -1){
//                Fragment fragment = new BookshelfFragment(); // Fragment 생성
//                Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
//                bundle.putString("keyBook", mBookName); // key , value
//                fragment.setArguments(bundle);


                fileMove(InStoragePath + "/" + mBookName, InStoragePath+"/Blinkling" + "/" + mBookName);
//mBookName이 이름을 북쉘프에 띄워야해!!

                ((MainActivity)getActivity()).sendBookname(mBookName);





//                FileInputStream fis = new FileInputStream(InStoragePath+ "/" + mBookName);
//                FileOutputStream fos = new FileOutputStream("/sdcard/이동할폴더명/파일명");
//
//                int data = 0;
//
//                while((data=fis.read())!=-1)
//                {
//                    fos.write(data);
//                }
//
//                fis.close();
//                fos.close();
                return null;

                //   ( (MainActivity)getActivity()).changeToText(mBookName);//진짜 북네임임 이 값을 북쉘프로 넘겨야되ㅁ
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
        //    mTextMsg.setText(mPath);
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
            fileDelete(inFileName);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void fileDelete(String deleteFileName) {
        File I = new File(deleteFileName);
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
        }
        ArrayAdapter adapter = (ArrayAdapter) getListView().getAdapter();
        adapter.notifyDataSetChanged();
    }


}