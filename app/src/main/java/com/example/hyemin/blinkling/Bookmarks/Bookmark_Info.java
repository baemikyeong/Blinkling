package com.example.hyemin.blinkling.Bookmarks;

import android.graphics.drawable.Drawable;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by seohyemin on 2017. 8. 21..
 */

public class Bookmark_Info {
    public Drawable mIcon;
    public int _id;
    public String title;
    public String doc;
    public String type;

    public Bookmark_Info( String title,String type, String doc){
      //  this._id = _id;
        this.title = title;
        this.type = type;
        this.doc = doc;

    }

    public static final Comparator<Bookmark_Info> ALPHA_COMPARATOR = new Comparator<Bookmark_Info>() {
        @Override
        public int compare(Bookmark_Info bookmarkInfo, Bookmark_Info t1) {
            return sCollator.compare(bookmarkInfo.title, t1.title);
        }

        private final Collator sCollator = Collator.getInstance();

    };


    public int get_id(){
        return _id;
    }

    public String getType(){
        return type;
    }

    public String getTitle(){
        return title;
    }

    public String getDoc(){
        return doc;
    }

    public void set_id(int _id){ this._id = _id; }

    public void setType(String type){ this.type = type; }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDoc(String doc){
        this.doc = doc;
    }


}

