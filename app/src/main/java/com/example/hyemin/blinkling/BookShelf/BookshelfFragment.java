package com.example.hyemin.blinkling.BookShelf;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hyemin.blinkling.MainActivity;
import com.example.hyemin.blinkling.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class BookshelfFragment extends Fragment {
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;
    File dir = Environment.getExternalStorageDirectory().getAbsoluteFile();
    GridView mFileGridView;
    ArrayList<String> mArrayListFile;//파일
    final static GridViewAdapter gridadapter = new GridViewAdapter();
    String mBookName = "";
    String InStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Blinkling";
    String mystoragepath = Environment.getExternalStorageDirectory().getAbsolutePath();

    boolean init = true;
    List mFileList;

    public BookshelfFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bookshelf, container, false);
        mFileGridView = (GridView) rootView.findViewById(list);
        mFileGridView.setAdapter(gridadapter);
        this.mFileList = new ArrayList();




        int writePermissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (readPermissionCheck == PackageManager.PERMISSION_DENIED || writePermissionCheck == PackageManager.PERMISSION_DENIED) {
            //권한없음

            //     Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                //Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
                //                    true를 리턴하는경우 우리는 왜 해당 권한이 필요한지를 설명해주고나서 권한 허가를 요청해야 합니다.
//                    만약 권한허가요청 다이어로그에서 사용자가 [다시 묻지 않기]를 체크했다면 이 함수는 항상 false를 리턴합니다.
//                            처음 권한을 요청하는경우에 이 함수는 항상 false를 요청합니다.
//                            즉 사용자가 [다시 묻지 않기]를 체크하지 않고, 1번이상 권한요청에 대해 거부한 경우에만 true를 리턴해주고 있습니다.
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_STORAGE);
            }
            // MY_PERMISSION_REQUEST_STORAGE is an
            // app-defined int constant

        } else {//권한있음
            if (init == true) {

                String[] Blinklist = getBlinklingList();
                showToBookShelf(Blinklist);
                init = false;
            }

        }


        mFileGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String strItem = gridadapter.getGridViewItemList().get(position).getTitle(); //position은 0부터 시작 position 번째 아이템 이름을 리턴함
                ( (MainActivity)getActivity()).changeToText(strItem);
            }
        });




        mFileGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View v, int position, long arg3) {
                final int pos = position;


                //fileMove(InStoragePath + "/" + mBookName, InStoragePath+"/Blinkling" + "/" + mBookName);

//                mCursor.moveToPosition(pos);
//                mFacade.delete(mCursor.getInt(mCursor.getColumnIndex(ExamDbContract.ExamDbEntry.ID)));
//                mCursor = mFacade.getAll();
//
//                mAdapter.swapCursor(mCursor);
//                mListView.setAdapter(mAdapter);


                // selectResult.remove(pos);
                // mAdapter.notifyDataSetChanged();

                return false;
            }
        });



        return rootView;
    }
//
//    public void bookshelfInit() {
//            if (init == true) {
//                String[] Blinklist = getBlinklingList();
//                showToBookShelf(Blinklist);
//                init = false;
//            }
//    }



    //
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {










        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
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





//        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//            switch (requestCode) {
//                case MY_PERMISSION_REQUEST_STORAGE:
//                    if (grantResults.length > 0
//                            && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//
//                        //InnerStorageFragment_start();
//
//
//                        // permission was granted, yay! do the
//                        // calendar task you need to do.
//
//                    } else {
//
//                        Log.d(TAG, "Permission always deny");
//
//                        // permission denied, boo! Disable the
//                        // functionality that depends on this permission.
//                      //  Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//            }
//        }








    public void onListItemClick(ListView Find_ListView, View view, int position, long id) {
        String strItemm = gridadapter.getGridViewItemList().get(position).getTitle(); //position은 0부터 시작 position 번째 아이템 이름을 리턴함
        ( (MainActivity)getActivity()).changeToText(strItemm);


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
        if(fileRoot.isDirectory() == false){
            //파일이라면!!즉 처음 블링클링 안에 아무것도 없는 초기상태
///////////////////////////////////////////////////////////여기토스트수정///////////////////////////////////////////////////////////////////////////////////////
            Toast toast = Toast.makeText(root, "블링클링 폴더에 아무것도 없음", Toast.LENGTH_SHORT);
            toast.show();
            return null;
        }

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
        super.onPrepareOptionsMenu(menu);
    }
}