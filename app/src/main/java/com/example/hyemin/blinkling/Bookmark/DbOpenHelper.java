package com.example.hyemin.blinkling.Bookmark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by seohyemin on 2017. 8. 29..
 */

public class DbOpenHelper {

    private static final String DATABASE_NAME = "Bookmark_DB";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DataBaseHelper mDBHelper;
    private Context mCtx;

    public class DataBaseHelper extends SQLiteOpenHelper {

        /**
         * 데이터베이스 헬퍼 생성자
         * @param context   context
         * @param name      Db Name
         * @param factory   CursorFactory
         * @param version   Db Version
         */
        public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //최초 DB를 만들 때 한번만 호출
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Database.CreateDB._createSql);
        }

        //버전이 업데이트 되었을 경우 DB를 다시 만들어주는 메소드
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //업데이트를 했는데 DB가 존재할 경우 onCreate를 다시 불러온다
            db.execSQL("DROP TABLE IF EXISTS " + Database.CreateDB.TABLE_NAME);
            onCreate(db);
        }
    }

    //DbOpenHelper 생성자
    public DbOpenHelper(Context context) {
        this.mCtx = context;
    }

    //Db를 여는 메소드
    public DbOpenHelper open() throws SQLException {
        //데이터베이스 이름은 boookmark_db로 한다
        mDBHelper = new DataBaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    //Db를 다 사용한 후 닫는 메소드
    public void close() {
        mDB.close();
    }

    /**
     *  데이터베이스에 사용자가 입력한 값을 insert하는 메소드
     * @param title         북마크 이름
     * @param document      북마크가 포함된 책이름
     * @param created_at     북마크 생성 날짜
     * @param updated_at    북마크 수정 날짜
     * @return              SQLiteDataBase에 입력한 값을 insert
     */
    public long insertColumn(String title, String document, String created_at, String updated_at, String position) {
        ContentValues values = new ContentValues();
        values.put(Database.CreateDB.TITLE, title);
        values.put(Database.CreateDB.DOCUMENT, document);
        values.put(Database.CreateDB.CREATED_AT, created_at);
        values.put(Database.CreateDB.UPDATED_AT, updated_at);
        values.put(Database.CreateDB.POSITION, position);
        return mDB.insert(Database.CreateDB.TABLE_NAME, null, values);
    }

    /**
     * 기존 데이터베이스에 사용자가 변경할 값을 입력하면 값이 변경됨(업데이트)
     * @param id            데이터베이스 아이디
     * @param title         북마크 이름
     * @param document      북마크가 포함된 책이름
     * @param created_at     북마크 생성 날짜
     * @param updated_at    북마크 수정 날짜
     * @return              SQLiteDataBase에 입력한 값을 update
     */
    public boolean updateColumn(long id, String title, String document, String created_at, String updated_at, String position) {
        ContentValues values = new ContentValues();
        values.put(Database.CreateDB.TITLE, title);
        values.put(Database.CreateDB.DOCUMENT, document);
        values.put(Database.CreateDB.CREATED_AT, created_at);
        values.put(Database.CreateDB.UPDATED_AT, updated_at);
        values.put(Database.CreateDB.POSITION, position);
        return mDB.update(Database.CreateDB.TABLE_NAME, values, "_id="+id, null) > 0;
    }

    //입력한 id값을 가진 DB를 지우는 메소드
    public boolean deleteColumn(long id) {
        return mDB.delete(Database.CreateDB.TABLE_NAME, "_id=" + id, null) > 0;
    }

    //입력한 전화번호 값을 가진 DB를 지우는 메소드
    public boolean deleteColumn(String title) {
        return mDB.delete(Database.CreateDB.TABLE_NAME, "TITLE="+title, null) > 0;
    }

    //커서 전체를 선택하는 메소드
    public Cursor getAllColumns() {
        return mDB.query(Database.CreateDB.TABLE_NAME, null, null, null, null, null, null);
    }

    //ID 컬럼 얻어오기
    public Cursor getColumn(long id) {
        Cursor c = mDB.query(Database.CreateDB.TABLE_NAME, null,
                "_id="+id, null, null, null, null);
        //받아온 컬럼이 null이 아니고 0번째가 아닐경우 제일 처음으로 보냄
        if (c != null && c.getCount() != 0)
            c.moveToFirst();
        return c;
    }

    //이름으로 검색하기 (rawQuery)
    public Cursor getMatchName(String name) {
        Cursor c = mDB.rawQuery( "Select * from address where name" + "'" + name + "'", null);
        return c;
    }

}
