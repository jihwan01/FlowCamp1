package com.example.flowcamp1.ui.dashboard;

import android.graphics.Bitmap;

public class DashboardItem {

    private Bitmap id;
    private String name;

    public DashboardItem(Bitmap mId, String mName){
        id = mId;
        name = mName;
    }

    public Bitmap getImageId(){
        return this.id;
    }

    public String getImageName() { return this.name; }
}
