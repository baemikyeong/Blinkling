package com.example.hyemin.blinkling.Bookmarks;

import android.provider.BaseColumns;

/**
 * Created by seohyemin on 2017. 8. 28..
 */


//데이터베이스 테이블
public final class DataBases {

    public static final class CreateDB implements BaseColumns {
        public static final String TITLE = "title";
        public static final String TYPE = "type";
        public static final String DOCUMENT = "document";
        public static final String _CREATE =
                "create table " + "BOOKMARK_DB" + "("
                        + _ID + " integer primary key autoincrement, "
                        + TITLE + " text not null , "
                        + TYPE + " text not null , "
                        + DOCUMENT + " text not null );";
    }
}
