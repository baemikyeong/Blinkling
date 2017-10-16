package com.example.hyemin.blinkling.BookShelf;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.hyemin.blinkling.MainActivity;
import com.example.hyemin.blinkling.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class BookshelfFragment extends Fragment {
    File dir = Environment.getExternalStorageDirectory().getAbsoluteFile();
    GridView mFileGridView;
    ArrayList<String> mArrayListFile;//파일
    final static GridViewAdapter gridadapter = new GridViewAdapter();
    String mBookName = "";
    String InStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Blinkling";
    boolean init = true;
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;

    List mFileList;

    public BookshelfFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bookshelf, container, false);
        mFileGridView = (GridView) rootView.findViewById(list);
        mFileGridView.setAdapter(gridadapter);

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_STORAGE);
        } else {
            if (init == true) {
                String[] Blinklist = getBlinklingList();
                showToBookShelf(Blinklist);
                init = false;
            }
            this.mFileList = new ArrayList();

        }



        mFileGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String strItem = gridadapter.getGridViewItemList().get(position).getTitle(); //position은 0부터 시작 position 번째 아이템 이름을 리턴함
                ( (MainActivity)getActivity()).changeToText(strItem, 0);
            }
        });



        mFileGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View v, final int position, long arg3) {
                final int pos = position;
                //
                //
//                Activity root = getActivity();
//                Toast toast = Toast.makeText(root, "ㄱ릭레길게 없음", Toast.LENGTH_SHORT);
//                toast.show();

                String message = "해당 데이터를 삭제하시겠습니까?<br />";

                DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // 선택된 아이템을 리스트에서 삭제한다.
                        gridadapter.getGridViewItemList().remove(position);
                        // Adapter에 데이터가 바뀐걸 알리고 리스트뷰에 다시 그린다.
                        gridadapter.notifyDataSetChanged();

                    }

                };


                new AlertDialog.Builder(getActivity())
                        .setTitle("문서 삭제")
                        .setMessage(Html.fromHtml(message))
                        .setPositiveButton("삭제", deleteListener)
                        .show();




                return true;

            }
        });



        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (init == true) {
                        String[] Blinklist = getBlinklingList();
                        showToBookShelf(Blinklist);
                        init = false;
                    }
                }
                break;

            default:
                break;
        }
    }
    public void setBookshelf(String mBookName_main){
//        gridadapter = new GridViewAdapter();
//        mFileGridView.setAdapter(gridadapter);
        gridadapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.book1)
                ,  mBookName_main) ;

        gridadapter.notifyDataSetChanged();
        mBookName = mBookName_main;

    }

//    String path="C:\";
//    File dirFile=new File(path);
//    File []fileList=dirFile.listFiles();
//for(File tempFile : fileList) {
//        if(tempFile.isFile()) {
//            String tempPath=tempFile.getParent();
//            String tempFileName=tempFile.getName();
//            System.out.println("Path="+tempPath);
//            System.out.println("FileName="+tempFileName);
//            /*** Do something withd tempPath and temp FileName ^^; ***/
//        }
//    }

//    public File[] getBlinklingList(){
//       Activity root = getActivity();
//        File fileRoot = new File(InStoragePath);
//        if(fileRoot.isDirectory() == false){
//            //파일이라면!!즉 처음 블링클링 안에 아무것도 없는 초기상태
/////////////////////////////////////////////////////////////여기토스트수정///////////////////////////////////////////////////////////////////////////////////////
//            Toast toast = Toast.makeText(root, "블링클링 폴더에 아무것도 없음", Toast.LENGTH_SHORT);
//            toast.show();
//            return null;
//        }
//
//
//        File[] stringFileList = fileRoot.listFiles();
//
//
////        for( File ffile : stringFileList) {
////            if(ffile.isFile()){
////                mFileList.add( ffile.getName() );
////            }
////            else {
////                Toast toast = Toast.makeText(root, "파일이 없음", Toast.LENGTH_SHORT);
////                toast.show();
////            }
////        }
//
////            return mFileList;
//        return stringFileList;
//        }


    public String[] getBlinklingList(){
        Activity root = getActivity();
        File fileRoot = new File(InStoragePath);
//        if(fileRoot.isDirectory() == false){
//            //파일이라면!!즉 처음 블링클링 안에 아무것도 없는 초기상태
/////////////////////////////////////////////////////////////여기토스트수정///////////////////////////////////////////////////////////////////////////////////////
//            Toast toast = Toast.makeText(root, "블링클링 폴더에 아무것도 없음", Toast.LENGTH_SHORT);
//            toast.show();
//            return null;
//        }

        String[] stringFileList = fileRoot.list();
        return stringFileList;

    }



//    public void showToBookShelf(File[] stringFileList){
//
//        if(gridadapter.isEmpty() == true) {
//            for (int i = 0; i < stringFileList.length; i++) {
//                gridadapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.book1)
//                        , stringFileList[i].getName());
//            }
//            gridadapter.notifyDataSetChanged();
//        }
//
//
//    }



    public void showToBookShelf(String []stringFileList){

        if(gridadapter.isEmpty() == true) {
            for (int i = 0; i < stringFileList.length; i++) {
                gridadapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.book1)
                        , stringFileList[i]);
            }
            gridadapter.notifyDataSetChanged();
        }


    }



    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.bottomNavigation.getSelectedItemId() != R.id.navigation_home)
            MainActivity.bottomNavigation.getMenu().findItem(R.id.navigation_home).setChecked(true);

    }


    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.bookmark_btn).setVisible(false);
        menu.findItem(R.id.voice_btn).setVisible(false);
        menu.findItem(R.id.eye_btn).setVisible(false);
        menu.findItem(R.id.light_btn).setVisible(false);
        menu.findItem(R.id.webmark_add).setVisible(false);
        menu.findItem(R.id.edit_scroll_range).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}