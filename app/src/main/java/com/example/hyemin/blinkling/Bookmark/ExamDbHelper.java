package com.example.hyemin.blinkling.Bookmark;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

/**
 * Created by seohyemin on 2017. 9. 2..
 */

public class ExamDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "ExamDb"; //DB이름
    private static final int DATABASE_VERSION = 1;//db 업데이트 할 때 사용함

    // 정렬
    public static final String ORDER_BY_DEFAULT = _ID + " asc";
    public static final String ORDER_BY_DEFAULT_DESC = _ID + " desc";
    public static final String ORDER_BY_NAME = ExamDbContract.ExamDbEntry.TITLE + " asc, " + _ID + " asc";
    //public static final String ORDER_BY_DATE_DESC = CREATED_AT + " desc, " + _ID + " desc";
    public static final String ORDER_BY_NAME_DESC = ExamDbContract.ExamDbEntry.TITLE + " desc, " + _ID + " desc";

    //생성자
    public static final String _createSql =
            "CREATE TABLE if not exists " + ExamDbContract.ExamDbEntry.TABLE_NAME + "("
                    + ExamDbContract.ExamDbEntry._ID + " integer primary key autoincrement, "
                    + ExamDbContract.ExamDbEntry.TITLE + " text,"
                    + ExamDbContract.ExamDbEntry.DOCUMENT + " text not null,"
                    + ExamDbContract.ExamDbEntry.CREATED_AT + " text not null,"
                    + ExamDbContract.ExamDbEntry.UPDATED_AT + " text not null,"
                    + ExamDbContract.ExamDbEntry.POS + " text);";

    private static ExamDbHelper sSingleton = null;

    public ExamDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized ExamDbHelper getInstance(Context context) {
        // 객체가 없을 경우에만 객체를 생성합니다.
        if(sSingleton == null) {
            sSingleton = new ExamDbHelper(context);
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
        db.execSQL(_createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int newVersion) {

    }
}
