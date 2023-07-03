package com.example.flowcamp1.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import android.provider.ContactsContract;
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

    Activity activity;
    Context context;


    public ContactsListFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.activity = requireActivity();
        this.context = getContext();
        MenuHost menuhost = requireActivity();
        initMenu(menuhost);

        binding = FragmentContactsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.user_icon);
//        addItem(drawable, "1", "010-1111-1111");
//        addItem(drawable, "2", "010-2222-2222");
//        addItem(drawable, "3", "010-3333-3333");
//        addItem(drawable, "4", "010-4444-4444");
//        addItem(drawable, "5", "010-5555-5555");
//        addItem(drawable, "6", "010-6666-6666");
//        addItem(drawable, "7", "010-7777-7777");

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
                androidx.appcompat.app.ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setTitle("Contacts");
                requestContactsPermission();
                readContacts();
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

    public void readContacts(){
        ContentResolver contentResolver = activity.getContentResolver();
        String[] projection = {ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                ,  ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                ,  ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Contacts.PHOTO_URI};


        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(projection[0]);
                int nameIndex = cursor.getColumnIndex(projection[1]);
                int numberIndex = cursor.getColumnIndex(projection[2]);
                int photoIndex = cursor.getColumnIndex(projection[3]);
                // 4.2 해당 index 를 사용해서 실제 값을 가져온다.
                String id = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                String number = cursor.getString(numberIndex);
                String photoUriStr = cursor.getString(photoIndex);

                Bitmap photoBitmap = null;
                if (photoUriStr != null) {
                    try {
                        photoBitmap = BitmapFactory.decodeStream(
                                activity.getContentResolver().openInputStream(Uri.parse(photoUriStr))
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Log.d("Contact", "Name: " + name + ", Phone Number: " + number);
                Drawable faceDrawable;
                if (photoBitmap != null) {
                    // 사진 사용
                    // ...
                    faceDrawable = new BitmapDrawable(getResources(), photoBitmap);
                } else {
                    // 기본 이미지 사용 또는 처리
                    // ...
                    faceDrawable = ContextCompat.getDrawable(context, R.drawable.user_icon);
                }
                if(number.length() == 11){
                    String newNumberStr = number.substring(0,3) + "-" + number.substring(3,7) + "-" + number.substring(7);
                    number = newNumberStr;
                }

                // 이름과 전화번호를 사용하여 원하는 작업 수행
                Log.d("Contact", "Name: " + name + ", Phone Number: " + number);
                addItem(faceDrawable, name, number);
            }
            cursor.close();
        }
    }

    private static final int PERMISSION_REQUEST_CODE = 100;
    private void requestContactsPermission() {
        String[] permissions = {Manifest.permission.READ_CONTACTS};

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        } else {
            // 권한이 이미 허용된 상태이므로 주소록 접근 작업 수행

        }
    }
}