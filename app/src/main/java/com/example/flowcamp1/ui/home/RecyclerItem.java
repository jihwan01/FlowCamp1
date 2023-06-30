package com.example.flowcamp1.ui.home;

import android.graphics.drawable.Drawable;

public class RecyclerItem {
    private Drawable faceImgDrawable;
    private String nameStr;

    public void setFaceImg(Drawable faceImg){
        faceImgDrawable = faceImg;
    }

    public void setName(String name){
        nameStr = name;
    }

    public Drawable getFaceImg() {
        return this.faceImgDrawable;
    }

    public String getName() {
        return this.nameStr;
    }
}
