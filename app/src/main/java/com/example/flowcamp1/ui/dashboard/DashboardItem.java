package com.example.flowcamp1.ui.dashboard;

import android.graphics.Bitmap;

public class DashboardItem {

    private Bitmap bitmap;
    private Bitmap thumbnail;
    private String name;

    public DashboardItem(Bitmap mBitmap, String mName){
        bitmap = mBitmap;
        name = mName;
        thumbnail = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
    }

    public Bitmap getImageId(){
        return this.bitmap;
    }

    public String getImageName() { return this.name; }

    public Bitmap getImageThumbnail() { return this.thumbnail; }
}
