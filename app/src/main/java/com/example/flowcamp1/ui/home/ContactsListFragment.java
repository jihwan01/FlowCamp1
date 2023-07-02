package com.example.flowcamp1.ui.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.flowcamp1.R;
import com.example.flowcamp1.databinding.FragmentContactsListBinding;
import com.example.flowcamp1.databinding.FragmentHomeBinding;
import com.example.flowcamp1.ui.dashboard.DashboardFragment;

import java.util.ArrayList;


public class ContactsListFragment extends Fragment  implements  RecyclerAdapter.OnItemClickListener {

    private FragmentContactsListBinding binding;
    RecyclerView mRecyclerView = null;
    RecyclerAdapter mAdapter = null;
    ArrayList<RecyclerItem> mList = new ArrayList<RecyclerItem>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentContactsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Context context = requireContext(); // or getContext() if you're not using requireContext()
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.telephone);
        addItem(drawable, "1");
        addItem(drawable, "2");
        addItem(drawable, "3");
        addItem(drawable, "4");
        addItem(drawable, "5");
        addItem(drawable, "6");
        addItem(drawable, "7");


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
        Log.d("TAG", "Destroy!!!!");
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

        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, new DashboardFragment());
        fragmentTransaction.commit();
        Log.d("TAG", "Clicked Item : " + mList.get(position).getName());

//        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.puzzle);
//        addItem(drawable, "first");
    }
}