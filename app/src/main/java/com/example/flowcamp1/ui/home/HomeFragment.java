package com.example.flowcamp1.ui.home;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

        // 리사이클러뷰에 표시할 데이터 리스트 생성.
//        ArrayList<String> list = new ArrayList<>();
//        for (int i=0; i<100; i++) {
//            RecyclerItem item = new RecyclerItem();
//            item.setText1(String.format("TEXT %d", i));
//            mList.add(item) ;
//        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gallery);
        addItem(bitmap, "first");
        addItem(bitmap, "first");
        addItem(bitmap, "first");
        addItem(bitmap, "first");
        addItem(bitmap, "first");
        addItem(bitmap, "first");
        addItem(bitmap, "first");


        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        binding.recycler1.setLayoutManager(new LinearLayoutManager(getContext())) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        RecyclerAdapter adapter = new RecyclerAdapter(mList) ;
        binding.recycler1.setAdapter(adapter) ;
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addItem(Bitmap face, String name) {
        RecyclerItem item = new RecyclerItem();
        item.setName(name);
        item.setFace(face);
//        item.setAge(age);
//        item.setName(name);

        mList.add(item);
    }
}