package com.example.flowcamp1.ui.dashboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.flowcamp1.R;
import com.example.flowcamp1.databinding.FragmentContactsProfileBinding;
import com.example.flowcamp1.databinding.FragmentDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GalleryImageFragment extends Fragment {
    ViewPager2 viewPager;
    ViewGroup rootView;
    public DashboardAdapter mAdapter;
    GallerySliderAdapter sliderAdapter;
    boolean orientationsState=true;
    int pos;

    public GalleryImageFragment(int pos, DashboardAdapter mAdapter){
        this.mAdapter = mAdapter;
        this.pos = pos;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                                ViewGroup container, Bundle savedInstanceState) {
        //Context context = container.getContext();

        super.onCreate(savedInstanceState);
        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gallery_image, container, false);
        sliderAdapter = new GallerySliderAdapter(pos, mAdapter);
        viewPager = rootView.findViewById(R.id.view_page);
        viewPager.setAdapter(sliderAdapter);
        viewPager.setCurrentItem(pos, false);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.GONE);

        return rootView;
    }

}