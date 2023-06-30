package com.example.flowcamp1.ui.home;

import android.graphics.drawable.Drawable;

public class RecyclerItem {
    private String nameStr;
    private String ageStr;

    public void setAge(String age){
        ageStr = age;
    }

    public void setName(String name){
        nameStr = name;
    }

    public String getAge() {
        return this.ageStr;
    }

    public String getName() {
        return this.nameStr;
    }
}
