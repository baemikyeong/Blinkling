package com.example.hyemin.blinkling.Bookmark;

import android.provider.BaseColumns;

/**
 * Created by seohyemin on 2017. 9. 8..
 */

public class ExamDbContract_web {
    /**
     * 아래의 엔트리 클래스가 구현하는 BaseColumns 는 모든 테이블이 기본적으로 구현해야 하는
     * 식별자인 id 값과 추후 데이터의 개수를 카운트하는데 사용하는 count 가 담겨있습니다.
     */
    public static class ExamDbEntry implements BaseColumns {
        public static final String TABLE_NAME = "ExamDb_web";
        public static final String ID = _ID;
        public static final String TITLE = "title";
        public static final String CREATED_AT = "created_at";
        public static final String UPDATED_AT = "updated_at";
        public static final String POS = "pos";

        public static final String[] columns = {ID,TITLE,UPDATED_AT,CREATED_AT,POS};
    }
}
