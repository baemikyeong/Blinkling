package com.example.hyemin.blinkling.BookShelf;

import android.graphics.drawable.Drawable;

/**
 * Created by dayeon on 2017. 8. 28..
 */

public class GridViewItem {
    private Drawable iconDrawable ;
    private String titleStr ;


    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }


    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }


}