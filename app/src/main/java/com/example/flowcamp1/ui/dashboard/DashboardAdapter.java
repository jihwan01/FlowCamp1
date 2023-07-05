package com.example.flowcamp1.ui.dashboard;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.flowcamp1.MainActivity;
import com.example.flowcamp1.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardAdapter extends BaseAdapter {

    private List<DashboardItem> mItem;
    private Context mContext;
    private MainActivity mActivity;
    private SparseBooleanArray selectedPositions;
    private boolean editMode;
    private FragmentManager fm;

    public DashboardAdapter(Context context, List<DashboardItem> aItem, FragmentManager fm, MainActivity activity) {
        this.mContext = context;
        this.mItem = aItem;
        this.fm = fm;
        this.mActivity = activity;
        editMode = false;
        selectedPositions = new SparseBooleanArray();
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
        View checkmark = convertView.findViewById(R.id.checkmark);
        checkmark.setVisibility(View.GONE);
        Bitmap thumbnail = getThumbnail(pos);
        image.setImageBitmap(thumbnail);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMode) {
                    boolean currentPosition = !selectedPositions.get(pos, false);
                    selectedPositions.put(pos, currentPosition);
                    notifyDataSetChanged(); // 이미지 클릭시 화면 갱신
                }
                else{
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.gallery_fragment_container, new GalleryImageFragment(pos, mItem));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        if(editMode) {
            boolean isSelectedPosition = selectedPositions.get(pos, false);
            checkmark = convertView.findViewById(R.id.checkmark);
            checkmark.setVisibility(isSelectedPosition ? View.VISIBLE : View.GONE);
        }

        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setMessage("사진 삭제하기");

                builder.setNegativeButton("취소", null);

                builder.setPositiveButton("삭제", (dialog, which) -> {
                    delBitmap(pos);
                    notifyDataSetChanged();
                });

                builder.create().show();

                return true;
            }
        });
        
        return convertView;
    }

    private void saveBitmapToFile(Bitmap bitmap, String filename) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + filename + ".png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Log.v("test", Environment.DIRECTORY_PICTURES);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void setBitmap(Uri imageUri){
        Glide.with(mContext)
                .asBitmap()
                .load(imageUri)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        String imageName = "pic" + resource.hashCode();
                        saveBitmapToFile(resource, imageName);
                        mItem.add(new DashboardItem(resource, imageName));
                        notifyDataSetChanged();
                    }
                });
    }

    public Bitmap getBitmap(int pos){
        return this.mItem.get(pos).getImageId();
    }

    public Bitmap getThumbnail(int pos){
        return this.mItem.get(pos).getImageThumbnail();
    }

    public String getBitmapName(int pos) { return this.mItem.get(pos).getImageName(); }

    public void delBitmap(int pos) {
        File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), getBitmapName(pos));
        file.delete();
        this.mItem.remove(pos);
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedPositions() {
        List<Integer> selectedPositionsList = new ArrayList<>();
        for (int i = 0; i < selectedPositions.size(); i++) {
            if (selectedPositions.valueAt(i)) {
                selectedPositionsList.add(selectedPositions.keyAt(i));
                Log.v("test", selectedPositions.keyAt(i)+".");
            }
        }
        Collections.sort(selectedPositionsList, Collections.reverseOrder());
        return selectedPositionsList;
    }

    public void changeMode(boolean mode){
        editMode = mode;
        selectedPositions.clear();
    }
}
