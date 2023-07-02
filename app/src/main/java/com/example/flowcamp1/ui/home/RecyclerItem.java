package com.example.flowcamp1.ui.home;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class RecyclerItem {
    private Drawable faceDrawable;
    private String nameStr;
    private String phoneNumStr;

    public void setName(String name){
        this.nameStr = name;
    }
    public void setFace(Drawable face){
        this.faceDrawable = face;
    }

    public void setPhoneNum(String phoneNum){
        this.phoneNumStr = phoneNum;
    }

    public String getName() {
        return this.nameStr;
    }
    public String getPhoneNum() {
        return this.phoneNumStr;
    }

    public Drawable getFace() {
        return this.faceDrawable;
    }
}
