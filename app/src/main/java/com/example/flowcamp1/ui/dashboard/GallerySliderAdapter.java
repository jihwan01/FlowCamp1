package com.example.flowcamp1.ui.dashboard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flowcamp1.R;

public class GallerySliderAdapter extends RecyclerView.Adapter<GallerySliderAdapter.ViewHolder> {


    public DashboardAdapter mAdapter;
    public int pos;

    public GallerySliderAdapter(int pos, DashboardAdapter mAdapter){
        Log.d("test",pos+"...");
        this.pos = pos;
        this.mAdapter = mAdapter;
    }

    @NonNull
    @Override
    public GallerySliderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_slider, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GallerySliderAdapter.ViewHolder holder, int i){
        holder.imageView.setImageBitmap(mAdapter.getBitmap(i));
        Log.d("test",i+"....");
    }

    @Override
    public int getItemCount(){
        return mAdapter.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlider);
        }
    }
}