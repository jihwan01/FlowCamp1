package com.example.flowcamp1.ui.dashboard;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.flowcamp1.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DashboardAdapter extends BaseAdapter {

    private List<DashboardItem> mItem;
    private Context mContext;

    public DashboardAdapter(Context context, List<DashboardItem> aItem) {
        this.mContext = context;
        this.mItem = aItem;
    }

    @Override
    public int getCount() {
        return this.mItem.size();
    }

    @Override
    public Object getItem(int pos) {
        return this.mItem.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dashboard_item, parent, false);
        }
        ImageView image = convertView.findViewById(R.id.gallery_item);

        Bitmap bitmap = getBitmap(pos);
        image.setImageBitmap(bitmap);
        
        return convertView;
    }

    public void setBitmap(Bitmap image, String name){
        this.mItem.add(new DashboardItem(image, name));
    }

    public Bitmap getBitmap(int pos){
        return this.mItem.get(pos).getImageId();
    }

    public String getBitmapName(int pos) { return this.mItem.get(pos).getImageName(); }
    public void delBitmap(int pos) {
        File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), getBitmapName(pos));
        file.delete();
        this.mItem.remove(pos); }
}
