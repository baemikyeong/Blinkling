package com.example.hyemin.blinkling.Bookmark;

import android.provider.BaseColumns;

/**
 * Created by seohyemin on 2017. 8. 29..
 */

public class Database {
    //데이터베이스 호출 시 사용될 생성자

    public static final class CreateDB implements BaseColumns {
        // 테이블
        public final static String TABLE_NAME = "bookmark_table";

        //필드
        //public static final String ID = _ID;
        public static final String TITLE = "title";
        public static final String DOCUMENT = "document";
        public static final String CREATED_AT = "created_at";
        public static final String UPDATED_AT = "updated_at";
        public static final String POSITION = "position";

        //생성자
        public static final String _createSql =
                "CREATE TABLE if not exists " + TABLE_NAME + "("
                + _ID + " integer primary key autoincrement, "
                        + TITLE + " text,"
                        + DOCUMENT + " text,"
                        + CREATED_AT + " text,"
                        + UPDATED_AT + " text,"
                        + POSITION + " text);";
    }
}
