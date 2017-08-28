package com.example.hyemin.blinkling.Bookmarks;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by seohyemin on 2017. 8. 28..
 */

public class DbOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "BOOKMARK_DB";
    private static final String TABLE_NAME = "BOOKMARK_TABLE";
    private static final int DB_VERSION = 1;
    private Bookmark_DB B_DB;
    public static SQLiteDatabase db;

    private class Bookmark_DB extends SQLiteOpenHelper{

        //생성자
        public Bookmark_DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //최초 DB를 만들때 한번만 호출된다
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBases.CreateDB._CREATE);
        }

        //버전이 업데이트 되었을 경우 DB를 다시 만들어 준다
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //업데이트 되면 이전 테이블을 제거한다
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
            //테이블의 새 인스턴스를 생성함
            onCreate(db);
        }



    }

    public DbOpenHelper(Context context){
        this.context = context;
    }

    public DbOpenHelper open() throws SQLException{
        B_DB = new Bookmark_DB(context,"BOOKMARK_DB",null, DB_VERSION);
        db = B_DB.getWritableDatabase();
        return this;
    }

    public void close(){
        db.close();
    }

    public Cursor getAllColumns(){
        return db.query("BOOKMARK_DB",null,null,null,null,null,null);
    }

}
