package com.example.hyemin.blinkling.Bookmark;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

/**
 * Created by seohyemin on 2017. 9. 2..
 */

public class ExamDbHelper_web extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "ExamDb_web"; //DB이름
    private static final int DATABASE_VERSION = 1;//db 업데이트 할 때 사용함

    // 정렬
    public static final String ORDER_BY_DEFAULT = _ID + " asc";
    public static final String ORDER_BY_DEFAULT_DESC = _ID + " desc";
    public static final String ORDER_BY_NAME = ExamDbContract.ExamDbEntry.TITLE + " asc, " + _ID + " asc";
    //public static final String ORDER_BY_DATE_DESC = CREATED_AT + " desc, " + _ID + " desc";
    public static final String ORDER_BY_NAME_DESC = ExamDbContract.ExamDbEntry.TITLE + " desc, " + _ID + " desc";

    //생성자
    public static final String _createSql_web =
            "CREATE TABLE if not exists " + ExamDbContract_web.ExamDbEntry.TABLE_NAME + "("
                    + ExamDbContract_web.ExamDbEntry._ID + " integer primary key autoincrement, "
                    + ExamDbContract_web.ExamDbEntry.TITLE + " text,"
                    + ExamDbContract_web.ExamDbEntry.CREATED_AT + " text not null,"
                    + ExamDbContract_web.ExamDbEntry.UPDATED_AT + " text not null,"
                    + ExamDbContract_web.ExamDbEntry.POS + " text not null);";

    private static ExamDbHelper_web sSingleton = null;

    public ExamDbHelper_web(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized ExamDbHelper_web getInstance(Context context) {
        // 객체가 없을 경우에만 객체를 생성합니다.
        if(sSingleton == null) {
            sSingleton = new ExamDbHelper_web(context);
        }

        // 객체가 이미 존재할 경우, 기존 객체를 리턴합니다.
        return sSingleton;
    }

    /**
     * DbHelper 클래스가 생성된 다음에 바로 실행되는 부분입니다.
     * db가 존재하지 않을 경우 db를 생성합니다.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //웹 북마크 생성
        db.execSQL(_createSql_web);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int newVersion) {

    }
}
