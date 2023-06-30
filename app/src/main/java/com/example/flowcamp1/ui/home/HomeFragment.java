package com.example.flowcamp1.ui.home;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flowcamp1.R;
import com.example.flowcamp1.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    RecyclerView mRecyclerView = null;
    RecyclerAdapter mAdapter = null;
    ArrayList<RecyclerItem> mList = new ArrayList<RecyclerItem>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        binding.recycler1.setLayoutManager(new LinearLayoutManager(getActivity())) ;

        //아이템 추가
        Resources res = getResources();
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "first");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "second");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "third");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "fourth");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "first");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "second");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "third");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "fourth");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "first");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "second");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "third");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "fourth");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "first");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "second");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "third");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "fourth");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "first");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "second");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "third");
        addItem(ResourcesCompat.getDrawable(res, R.drawable.gallery, null), "fourth");

//        adapter.notifyDataSetChanged();
//
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        RecyclerAdapter adapter = new RecyclerAdapter(mList) ;
        binding.recycler1.setAdapter(mAdapter) ;


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addItem(Drawable faceImg, String name) {
        RecyclerItem item = new RecyclerItem();

        item.setFaceImg(faceImg);
        item.setName(name);

        mList.add(item);
    }
}