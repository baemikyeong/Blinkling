/*
package com.example.hyemin.blinkling.Bookmark;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by seohyemin on 2017. 8. 12..
 *//*


public class Bookmark_DB extends SQLiteOpenHelper {
    private Context context;
    public static final String TABLE_NAME = "BOOKMARK_TABLE";
    List<Bookmark_Info> bm;
    SQLiteDatabase db;
    Cursor cursor;
    String sql;

    //생성
    public Bookmark_DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //최초 DB를 만들때 딱 한번만 호출된다
    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE IF NOT EXISTS BOOKMARK_TABLE ( ");
        sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" TITLE TEXT, ");
        sb.append(" TYPE TEXT, ");
        sb.append(" DOCUMENT TEXT ) ");

        // SQLite Database로 쿼리 실행
        db.execSQL(sb.toString());

        //확인용 !
        Toast.makeText(context, "Table 생성완료", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //업데이트 되면 이전 테이블을 제거한다
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        //테이블의 새 인스턴스를 생성함
        onCreate(db);

    }


    public void addBookmark(Bookmark_Info bookmarkInfo){

        // 1. 쓸 수 있는 DB 객체를 가져온다.
        SQLiteDatabase db = getWritableDatabase();

        // 2. Person Data를 Insert한다.
        // _id는 자동으로 증가하기 때문에 넣지 않습니다.

        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO BOOKMARK_TABLE ( ");
        sb.append(" TITLE, TYPE, DOCUMENT ) ");
        sb.append(" VALUES ( ?, ?, ? ) ");

        db.execSQL(sb.toString(),
                new Object[]{
                        bookmarkInfo.getTitle(),
                        Integer.parseInt(bookmarkInfo.get_pos()),
                        bookmarkInfo.getDoc()});;

        Toast.makeText(context,bookmarkInfo.getTitle() +"/"+
                                        Integer.parseInt(bookmarkInfo.get_pos())+"/"+
                                      bookmarkInfo.getDoc()+"ADD 완료", Toast.LENGTH_SHORT).show();
    }

    //입력된 모든 북마크를 db에서 가져오는 메소드
    public List<Bookmark_Info> getAllBookmarkData() {

        //사용자들에게 보여지는 속성은 ID, 북마크의 제목, 북마크가 속하는 문서이다.
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT _ID, TITLE, TYPE, DOCUMENT FROM BOOKMARK_TABLE ");

        //읽기 전용 db 객체를 만든다.
        SQLiteDatabase db = getReadableDatabase();

        //커서를 통해서 아이템들을 조정
        Cursor cursor = db.rawQuery(sb.toString(), null);

        bm = new ArrayList<Bookmark_Info>();

        Bookmark_Info bi = null;

        //moveToNext 다음에 데이터가 있으면 true 없으면 false
        while (cursor.moveToNext()) {
            bi = new Bookmark_Info();
            bi.set_id(cursor.getInt(0));
            bi.setTitle(cursor.getString(1));
            bi.setPos(cursor.getString(2));
            bi.setDoc(cursor.getString(3));

            bm.add(bi);

        }
        return bm;
    }


}
*/
