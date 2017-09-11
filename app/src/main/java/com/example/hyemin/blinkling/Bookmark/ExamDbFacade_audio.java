package com.example.hyemin.blinkling.Bookmark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by seohyemin on 2017. 9. 8..
 */

public class ExamDbFacade_audio {

    private ExamDbHelper_audio mHelper;
    private Context mContext;
    private InfoClass_audio mInfoClass_audio;

    public ExamDbFacade_audio(Context context) {
        mHelper = ExamDbHelper_audio.getInstance(context);
        mContext = context;

    }

    public ArrayList<InfoClass_audio> insert(String title, String created_at, String updated_at, String position) {

        // db는 읽기만 가능한 것과 읽고 쓸 수 있는 것이 있습니다.
        // 삽입은 쓰는 것이므로 getWritableDatabase() 메소드를 이용해야 합니다.
        SQLiteDatabase db = mHelper.getWritableDatabase();

        // 삽입할 데이터는 ContentValues 객체에 담깁니다.
        // 맵과 동일하게 key 와 value 로 데이터를 저장합니다.
        ContentValues values = new ContentValues();

        // 삽입할 문자열을 파라메터로 받아서 저장합니다.
        values.put(ExamDbContract_audio.ExamDbEntry.TITLE, title);
        values.put(ExamDbContract_audio.ExamDbEntry.CREATED_AT, created_at);
        values.put(ExamDbContract_audio.ExamDbEntry.UPDATED_AT, updated_at);
        values.put(ExamDbContract_audio.ExamDbEntry.POS, position);

        /**
         *
         * 안드로이드에서는 기본적으로 삽입, 갱신, 삭제, 조회의 기능을 하는 메소드를 구현해놓았습니다.
         * insert 메소드의 파라메터는 다음과 같습니다.
         *
         * public long insert (String table, String nullColumnHack, ContentValues values)
         *
         * table : 삽입할 테이블, nullColumnHack : null , values : 삽입할 데이터들
         */
        db.insert(ExamDbContract_audio.ExamDbEntry.TABLE_NAME, null, values);

        return select();
    }

    /**
     * 테이블에 존재하는 모든 데이터들을 리턴합니다.
     *
     * @return
     */
    public ArrayList<InfoClass_audio> select() {
        ArrayList<InfoClass_audio> result = new ArrayList<>();

        SQLiteDatabase db = mHelper.getReadableDatabase();

        /**
         * public Cursor query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
         * table : 접근할 테이블명, columns : 가져올 데이터들의 컬럼명,
         * selection : where 절의 key 들, selectionsArgs : where 절의 value 들
         *
         * Cursor 객체는 해당 쿼리의 결과가 담기는 객체입니다.
         */
        Cursor cursor = db.query(ExamDbContract_audio.ExamDbEntry.TABLE_NAME,
                ExamDbContract_audio.ExamDbEntry.columns,
                null, null, null, null, null);

        /**
         * 커서 객체는 최초 생성시 포지션이 -1 로 지정되어 있습니다.
         * cursor.moveToFirst() 메소드를 이용하면 포지션이 0으로 이동하는데,
         * moveToNext() 메소드를 이용하면 일단 0으로 이동한 뒤, 포지션을 1씩 이동합니다.
         *
         * 아래의 반복문은 커서를 0 부터 끝까지 탐색하여 각각의 위치에서 사용자가 입력한 문자열을
         *
         * 얻어오고, 그것을 리스트에 하나씩 저장합니다.
         */
        while (cursor.moveToNext()) {
            //infoclass에 입력된 값을 입력
            mInfoClass_audio = new InfoClass_audio(
                    cursor.getInt(cursor.getColumnIndex(ExamDbContract_audio.ExamDbEntry.ID)),
                    cursor.getString(cursor.getColumnIndex(ExamDbContract_audio.ExamDbEntry.TITLE)),
                    cursor.getString(cursor.getColumnIndex(ExamDbContract_audio.ExamDbEntry.POS)),
                    cursor.getString(cursor.getColumnIndex(ExamDbContract_audio.ExamDbEntry.CREATED_AT)),
                    cursor.getString(cursor.getColumnIndex(ExamDbContract_audio.ExamDbEntry.UPDATED_AT))
            );
            //입력된 값을 가지고 있는 infoclass를 infoarray에 add
            result.add(mInfoClass_audio);
        }
        return result;
    }

    /**
     * 테이블에 존재하는 data 컬럼의 모든 값들을 사용자가 입력한 값으로 업데이트 합니다.
     *
     * @return
     */
    public ArrayList<InfoClass_audio> update(String title, String created_at, String updated_at, String position) {
        // 업데이트는 쓰기 기능이 필요합니다.
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ExamDbContract_audio.ExamDbEntry.TITLE, title);
        values.put(ExamDbContract_audio.ExamDbEntry.CREATED_AT, created_at);
        values.put(ExamDbContract_audio.ExamDbEntry.UPDATED_AT, updated_at);
        values.put(ExamDbContract_audio.ExamDbEntry.POS, position);

        /**
         * public int update (String table, ContentValues values, String whereClause, String[] whereArgs)
         * table : 갱신할 데이터가 존재하는 테이블, values : 변경할 값들,
         * whereClause : 조건절 (key), whereArgs : 값들
         */
        db.update(ExamDbContract_audio.ExamDbEntry.TABLE_NAME, values, null, null);

        return select();
    }

    public ArrayList<InfoClass_audio> delete(String delete) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        /**
         * public int delete (String table, String whereClause, String[] whereArgs)
         * table : 지울 데이터가 위치하는 테이블, whereClause : 조건절, whereArgs : 조건절
         */
        db.delete(ExamDbContract_audio.ExamDbEntry.TABLE_NAME, ExamDbContract_audio.ExamDbEntry.ID + " = ?", new String[]{delete});


        return select();
    }

/*    //입력한 id값을 가진 DB를 지우는 메소드
    public boolean deleteColumn(final long id) {
        String[] whereArgs = {id + ""};
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db.delete(ExamDbContract.ExamDbEntry.TABLE_NAME, "_id=" + id, null) > 0;
        //return mDB.delete(Database.CreateDB.TABLE_NAME, Database.CreateDB.WHERE_BY_ID, whereArgs );
    }*/


    //해당하는 id의 리스트를 삭제하는 메소드
    public void delete(int id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        db.delete(ExamDbContract_audio.ExamDbEntry.TABLE_NAME, ExamDbContract_audio.ExamDbEntry.ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void editTitle(int id, String newTitle) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // 삽입할 문자열을 파라메터로 받아서 저장합니다.
        values.put(ExamDbContract_audio.ExamDbEntry.TITLE, newTitle);

        /**
         * public int update (String table, ContentValues values, String whereClause, String[] whereArgs)
         * table : 갱신할 데이터가 존재하는 테이블, values : 변경할 값들,
         * whereClause : 조건절 (key), whereArgs : 값들
         */
        db.update(ExamDbContract_audio.ExamDbEntry.TABLE_NAME, values, ExamDbContract_audio.ExamDbEntry.ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Cursor getTitle(long id){
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String booktitle;
        /**
         * public Cursor query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
         * table : 접근할 테이블명, columns : 가져올 데이터들의 컬럼명,
         * selection : where 절의 key 들, selectionsArgs : where 절의 value 들
         *
         * Cursor 객체는 해당 쿼리의 결과가 담기는 객체입니다.
         */
        Cursor cursor = db.query(ExamDbContract_audio.ExamDbEntry.TABLE_NAME,
                new String[]{ExamDbContract_audio.ExamDbEntry.TITLE},
                ExamDbContract_audio.ExamDbEntry.ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        booktitle = cursor.getString(cursor.getColumnIndex(ExamDbContract_audio.ExamDbEntry.TITLE));
        return cursor;
    }

    public Cursor getAll() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT _ID, title, updated_at FROM " + ExamDbContract_audio.ExamDbEntry.TABLE_NAME+" order by _ID asc", null);
        return c;
    }


    public Cursor order_desc() {//최신순
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT _ID, title, updated_at FROM " + ExamDbContract_audio.ExamDbEntry.TABLE_NAME +" order by _ID desc", null);
        return c;

    }
    public Cursor order_alp_asc() {//알파벳순
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT _ID, title, updated_at FROM " + ExamDbContract_audio.ExamDbEntry.TABLE_NAME +" order by title asc", null);
        return c;

    }

    /**
     * CursorAdapter 에 데이터를 제공하기 위한 메소드 입니다.
     * db 에 존재하는 모든 데이터를 리턴합니다.
     * <p>
     * CursorAdapter 에 들어가는 cursor 객체는 반드시 _ID 컬럼을 포함해야 합니다.
     *
     * @return
     */
    public Cursor getCursor() {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        return db.query(ExamDbContract_audio.ExamDbEntry.TABLE_NAME,
                ExamDbContract_audio.ExamDbEntry.columns,
                null, null, null, null, null);
    }

}
