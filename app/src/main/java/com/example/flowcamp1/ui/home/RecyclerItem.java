package com.example.flowcamp1.ui.home;

import android.graphics.drawable.Drawable;

public class RecyclerItem {
    private String text1Str;
    private String text2Str;

    public void setText1(String text1){
        text1Str = text1;
    }
    public void setText2(String text2){
        text2Str = text2;
    }

    public String getText1() {
        return this.text1Str;
    }

    public String getText2() {
        return this.text2Str;
    }
}
