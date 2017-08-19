package com.example.hyemin.blinkling.Bookmark;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by seohyemin on 2017. 8. 12..
 */

public class Bookmark_DB extends SQLiteOpenHelper {
    private Context context;

    public Bookmark_DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    /**
     * 데이터베이스가 존재하지 않을 때 딱 한번 실행된다 DB를만드는 역할을 함
     *
     * @param
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE IF NOT EXISTS BOOKMARK_TABLE ( ");
        sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" TITLE TEXT, ");
        sb.append(" MEMO TEXT, ");
        sb.append(" DOCUMENT TEXT ) ");

        // SQLite Database로 쿼리 실행
        db.execSQL(sb.toString());

        //확인용 !
        Toast.makeText(context, "Table 생성완료", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void testDB() {

        SQLiteDatabase db = getReadableDatabase();
    }

  /*  public void addPerson(Person person) {
        // 1. 쓸 수 있는 DB 객체를 가져온다.

        SQLiteDatabase db = getWritableDatabase();

        // 2. Person Data를 Insert한다.
        // _id는 자동으로 증가하기 때문에 넣지 않습니다.

        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO TEST_TABLE ( ");
        sb.append(" TITLE, AGE, PHONE ) ");
        sb.append(" VALUES ( ?, ?, ? ) ");

        // sb.append(" VALUES ( #NAME#, #AGE#, #PHONE# ) ");
        //
        //
        // Age는 Integer이기 때문에 홀따옴표(')를 주지 않는다.
        // String query = sb.toString();
        // query.replace("#NAME#", "'" + person.getName() + "'");
        // query.replace("#NAME#", person.getAge());
        // query.replace("#NAME#", "'" + person.getPhone() + "'");
        //
        // db.execSQL(query);

        db.execSQL(sb.toString(),
                new Object[]{
                        person.getName(),
                        Integer.parseInt(person.getAge()),
                        person.getPhone()});
        ;
        Toast.makeText(context, "Insert 완료", Toast.LENGTH_SHORT).show();
    }

    public List getAllPersonData() {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT _ID, TITLE, AGE, PHONE FROM TEST_TABLE");

        //읽기 전용 db 객체를 만든다.
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(sb.toString(), null);

        List people = new ArrayList();
        Person person = null;

        //moveToNext 다음에 데이터가 있으면 true 없으면 false
        while (cursor.moveToNext()) {
            person = new Person();
            person.set_id(cursor.getInt(0));
            person.setName(cursor.getString(1));
            person.setAge(cursor.getString(2));
            person.setPhone(cursor.getString(3));

            people.add(person);

        }
        return people;
    }*/


}
