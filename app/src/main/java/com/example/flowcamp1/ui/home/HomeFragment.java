package com.example.flowcamp1.ui.home;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class HomeFragment extends Fragment implements  RecyclerAdapter.OnItemClickListener {

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
        Context context = requireContext(); // or getContext() if you're not using requireContext()
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.telephone);
        addItem(drawable, "1");
        addItem(drawable, "2");
        addItem(drawable, "3");
        addItem(drawable, "4");
        addItem(drawable, "5");
        addItem(drawable, "6");
        addItem(drawable, "7");
        addItem(drawable, "8");
        addItem(drawable, "9");
        addItem(drawable, "10");
        addItem(drawable, "11");
        addItem(drawable, "12");
        addItem(drawable, "13");
        addItem(drawable, "14");


        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        binding.recycler1.setLayoutManager(new LinearLayoutManager(getContext())) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        RecyclerAdapter adapter = new RecyclerAdapter(mList) ;
        adapter.setOnItemClickListener(this);
        binding.recycler1.setAdapter(adapter) ;
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addItem(Drawable face, String name) {
        RecyclerItem item = new RecyclerItem();
        item.setName(name);
        item.setFace(face);

        mList.add(item);
    }

    @Override
    public void onItemClick(int position) {
        // TODO : Handle the item click event

        Log.d("TAG", "Run OnItemClick " + mList.get(position).getName());

//        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.puzzle);
//        addItem(drawable, "first");
    }
}