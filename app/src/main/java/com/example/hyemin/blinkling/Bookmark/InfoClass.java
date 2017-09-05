package com.example.hyemin.blinkling.Bookmark;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by seohyemin on 2017. 8. 29..
 */

public class InfoClass {
    public int _id;
    public String title;
    public String document;
    public String created_at;
    public String updated_at;
    public String position;
    public Drawable image;

    //생성자
    public InfoClass(){}

    /**
     * 실질적으로 값을 입력할 때 사용되는 생성자(getter and setter)
     * @param _id            데이터베이스 아이디
     * @param title         북마크 이름
     * @param document      북마크가 포함된 책이름
     * @param created_at     북마크 생성 날짜
     * @param updated_at    북마크 수정 날짜
     * @param position      북마크 저장할 위치
     */
    public InfoClass(int _id, String title, String document, String created_at, String updated_at, String position) {
        this._id = _id;
        this.title = title;
        this.document = document;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.position = position;
    }
    public Drawable getImage(){
        return image;
    }

    public void setImage(Drawable image){
        this.image = image;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String Title) {
        this.title = title;
    }

    public String getDocument() {
        return document;
    }

    public void setContact(String document) {
        this.document = document;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPosition(){
        return position;
    }

    public void setPosition(String position){
        this.position = position;
    }

}
