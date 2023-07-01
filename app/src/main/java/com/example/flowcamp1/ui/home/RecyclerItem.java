package com.example.flowcamp1.ui.home;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class RecyclerItem {
    private Drawable faceDrawable;
    private String nameStr;

    public void setName(String name){
        nameStr = name;
    }
    public void setFace(Drawable face){
        faceDrawable = face;
    }

    public String getName() {
        return this.nameStr;
    }

    public Drawable getFace() {
        return this.faceDrawable;
    }
}
