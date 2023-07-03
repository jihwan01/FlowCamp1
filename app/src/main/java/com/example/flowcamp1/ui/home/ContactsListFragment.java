package com.example.flowcamp1.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
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
import android.widget.Toast;

import com.example.flowcamp1.R;
import com.example.flowcamp1.databinding.FragmentContactsListBinding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class ContactsListFragment extends Fragment  implements  RecyclerAdapter.OnItemClickListener {

    private FragmentContactsListBinding binding;
    RecyclerView mRecyclerView = null;
    RecyclerAdapter mAdapter = null;
    ArrayList<RecyclerItem> mList = new ArrayList<RecyclerItem>();

    Activity activity;
    Context context;

    boolean initialized = false;

    public ContactsListFragment() {
        // Required empty public constructor
    }
    private ActivityResultLauncher<String> requestContactsReadPermissionLauncher;
    private ActivityResultLauncher<String> requestContactsWritePermissionLauncher;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 퍼미션 요청 결과 처리를 위한 ActivityResultLauncher 초기화
        requestContactsReadPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // 퍼미션이 승인된 경우 List Load
                Log.d("Permission", "Permission Request is Granted!!");
                Toast.makeText(getActivity(), "Permission Request is Granted!!", Toast.LENGTH_SHORT).show();
                readContacts();
            } else {
                // 퍼미션이 거부된 경우에 대한 처리
                // TODO: 퍼미션 거부 시 사용자에게 안내 또는 대체 동작 수행
                Log.d("Contacts Permission", "Read Permission Request is denied!!");
            }
        });

        // 퍼미션 요청 결과 처리를 위한 ActivityResultLauncher 초기화
        requestContactsWritePermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // 퍼미션이 승인된 경우 List Load
                Log.d("Permission", "Permission Request is Granted!!");

            } else {
                // 퍼미션이 거부된 경우에 대한 처리
                // TODO: 퍼미션 거부 시 사용자에게 안내 또는 대체 동작 수행
                Log.d("Contacts Permission", "Write Permission Request is denied!!");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.activity = requireActivity();
        this.context = getContext();
        MenuHost menuhost = requireActivity();
        initMenu(menuhost);

        binding = FragmentContactsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        binding.recycler1.setLayoutManager(new LinearLayoutManager(getContext())) ;
        if(!initialized){
            requestContactsReadPermission();
            requestContactsWritePermission();
        }else {
            // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
            Toast.makeText(getActivity(), "This is the length of mList : " + mList.size(), Toast.LENGTH_SHORT).show();
            if(mAdapter != null){
                binding.recycler1.setAdapter(mAdapter);
            }
//            mAdapter = new RecyclerAdapter(mList) ;
//            mAdapter.setOnItemClickListener(this);
            Toast.makeText(getActivity(), "Already Initialized!!", Toast.LENGTH_SHORT).show();
        }



        return root;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        Log.d("TAG", "Destroy List Fragment!!");
        Log.d("TAG", "This is the size of mlist : " + mList.size());

        binding = null;
    }

    public void addItem(String id, Drawable face, String name, String phoneNum) {
        RecyclerItem item = new RecyclerItem();
        item.setId(id);
        item.setFace(face);
        item.setName(name);
        item.setPhoneNum(phoneNum);

        mList.add(item);
    }

    public void ModifyFace(String id, Drawable newFace){

        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
        Toast.makeText(getActivity(), "Modify face image!", Toast.LENGTH_SHORT).show();
        // ContentResolver를 사용하여 프로필 사진 업데이트
        ContentValues values = new ContentValues();
        values.put(ContactsContract.Contacts.Photo.PHOTO, drawableToByteArray(newFace)); // Drawable을 바이트 배열로 변환하여 저장
        writeContacts(contactUri, values);
    }

    public byte[] drawableToByteArray(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return bitmapToByteArray(((BitmapDrawable) drawable).getBitmap());
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmapToByteArray(bitmap);
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }


    @Override
    public void onItemClick(int position) {
        // TODO : Handle the item click event

        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, new ContactsProfileFragment(this, mList.get(position).getId(), mList.get(position).getFace(), mList.get(position).getName(), mList.get(position).getPhoneNum()));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Log.d("TAG", "Clicked Item : " + mList.get(position).getName());

//        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.puzzle);
//        addItem(drawable, "first");
    }

    private void onAddClick() {
        // TODO : Handle the item click event

//        FragmentManager fm = getParentFragmentManager();
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.user_icon);
//        fragmentTransaction.replace(R.id.fragmentContainer, new ContactsProfileFragment(drawable, "홍길동", "010-0000-0000"));
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

    }


    private void initMenu(MenuHost menuhost){
        menuhost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                androidx.appcompat.app.ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
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

    public void readContacts(){

        Toast.makeText(getActivity(), "Read Contacts", Toast.LENGTH_SHORT).show();
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
                addItem(id, faceDrawable, name, number);
            }
            cursor.close();
        }
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        Toast.makeText(getActivity(), "This is the length of mList : " + mList.size(), Toast.LENGTH_SHORT).show();
        mAdapter = new RecyclerAdapter(mList) ;
        mAdapter.setOnItemClickListener(this);
        binding.recycler1.setAdapter(mAdapter);
        initialized = true;
    }


    public void writeContacts(Uri contactUri, ContentValues values){
        context.getContentResolver().update(contactUri, values, null, null);
    }
    private void requestContactsReadPermission() {
        // 퍼미션을 이미 가지고 있는지 확인
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            // 이미 퍼미션이 승인된 경우 갤러리에서 사진을 선택하도록 호출
            readContacts();
        } else {
            // 퍼미션을 요청
            Log.d("GALLERY", "Please grant the permission!!");
            requestContactsReadPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
        }
    }

    private void requestContactsWritePermission() {
        // 퍼미션을 이미 가지고 있는지 확인
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            // 퍼미션을 이미 가지고 있는 경우 Contacts 쓰는 코드 호출

        } else {
            // 퍼미션을 요청
            Log.d("GALLERY", "Please grant the Write permission!!");
            requestContactsWritePermissionLauncher.launch(Manifest.permission.WRITE_CONTACTS);
        }
    }
}