package com.example.flowcamp1.ui.dashboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.flowcamp1.MainActivity;
import com.example.flowcamp1.R;
import com.example.flowcamp1.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    public GridView mGridView;
    public DashboardAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        Context context = container.getContext();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);
        mGridView = rootView.findViewById(R.id.gallery);

        List<DashboardItem> itemList = new ArrayList<>();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gallery);
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));
        itemList.add(new DashboardItem(bitmap));

        mAdapter = new DashboardAdapter(context, itemList);
        mGridView.setAdapter(mAdapter);

        return rootView;
    }
}