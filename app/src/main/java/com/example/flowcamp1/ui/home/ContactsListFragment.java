package com.example.flowcamp1.ui.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.flowcamp1.R;
import com.example.flowcamp1.databinding.FragmentContactsListBinding;

import java.util.ArrayList;


public class ContactsListFragment extends Fragment  implements  RecyclerAdapter.OnItemClickListener {

    private FragmentContactsListBinding binding;
    RecyclerView mRecyclerView = null;
    RecyclerAdapter mAdapter = null;
    ArrayList<RecyclerItem> mList = new ArrayList<RecyclerItem>();

    public ContactsListFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        MenuHost menuhost = requireActivity();
        initMenu(menuhost);

        binding = FragmentContactsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Context context = requireContext();
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.user_icon);
        addItem(drawable, "1", "010-1111-1111");
        addItem(drawable, "2", "010-2222-2222");
        addItem(drawable, "3", "010-3333-3333");
        addItem(drawable, "4", "010-4444-4444");
        addItem(drawable, "5", "010-5555-5555");
        addItem(drawable, "6", "010-6666-6666");
        addItem(drawable, "7", "010-7777-7777");

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
        Log.d("TAG", "Destroy List Fragment!!");
        Log.d("TAG", "This is the size of mlist : " + mList.size());

        binding = null;
    }

    public void addItem(Drawable face, String name, String phoneNum) {
        RecyclerItem item = new RecyclerItem();
        item.setName(name);
        item.setPhoneNum(phoneNum);
        item.setFace(face);

        mList.add(item);
    }

    @Override
    public void onItemClick(int position) {
        // TODO : Handle the item click event

        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, new ContactsProfileFragment(mList.get(position).getFace(), mList.get(position).getName(), mList.get(position).getPhoneNum()));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Log.d("TAG", "Clicked Item : " + mList.get(position).getName());

//        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.puzzle);
//        addItem(drawable, "first");
    }

    private void onAddClick() {
        // TODO : Handle the item click event

        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Context context = requireContext(); // or getContext() if you're not using requireContext()
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.user_icon);
        fragmentTransaction.replace(R.id.fragmentContainer, new ContactsProfileFragment(drawable, "홍길동", "010-0000-0000"));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

//        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.puzzle);
//        addItem(drawable, "first");
    }


    private void initMenu(MenuHost menuhost){
        menuhost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                androidx.appcompat.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setTitle("Contacts");
                menuInflater.inflate(R.menu.contacts_list_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem){

                if (menuItem.getItemId() == R.id.item_add_button){
                    onAddClick();
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
}