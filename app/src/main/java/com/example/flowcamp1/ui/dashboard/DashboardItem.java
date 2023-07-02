package com.example.flowcamp1.ui.dashboard;

import android.graphics.Bitmap;

public class DashboardItem {

    private Bitmap id;

    public DashboardItem(Bitmap mId){
        id = mId;
    }

    public Bitmap getImageId(){
        return this.id;
    }
}
