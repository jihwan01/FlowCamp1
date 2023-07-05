package com.example.flowcamp1.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
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
import androidx.recyclerview.widget.DividerItemDecoration;
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
                // Log.d("Permission", "Permission Request is Granted!!");
                // Toast.makeText(getActivity(), "Permission Request is Granted!!", Toast.LENGTH_SHORT).show();
                requestContactsWritePermission();
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
        }else {
            // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
            if(mAdapter != null){
                binding.recycler1.setAdapter(mAdapter);
            }
        }
//        binding.recycler1.addItemDecoration(new VerticalSpaceItemDecoration(48));
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(requireContext(),new LinearLayoutManager(requireActivity()).getOrientation());
        binding.recycler1.addItemDecoration(dividerItemDecoration);


        return root;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        Log.d("TAG", "Destroy List Fragment!!");
        Log.d("TAG", "This is the size of mlist : " + mList.size());

        binding = null;
    }
    public void addToDataBase(Drawable face, String name, String phoneNum){
        if(face == null){
            face = ContextCompat.getDrawable(context, R.drawable.user_icon);
        }else{
            // TODO : 사진 넣기
        }
        ContentResolver contentResolver = activity.getContentResolver();

        ContentValues rawContactValues = new ContentValues();
        rawContactValues.putNull(ContactsContract.RawContacts.ACCOUNT_TYPE);
        rawContactValues.putNull(ContactsContract.RawContacts.ACCOUNT_NAME);

        Uri rawContactUri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, rawContactValues);
        Long rawContactId = rawContactUri != null ? Long.parseLong(rawContactUri.getLastPathSegment()) : null;

        ContentValues contactValues = new ContentValues();
        contactValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        contactValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        contactValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);

        ContentValues phoneValues = new ContentValues();
        phoneValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        phoneValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        phoneValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNum);
        phoneValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);


        try {
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, contactValues);
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, phoneValues);
            Toast.makeText(getActivity(), "연락처 추가 성공", Toast.LENGTH_SHORT).show();
            initialized = false;
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, this);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "연락처 추가 실패", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void addItem(String id, Drawable face, String name, String phoneNum) {
        RecyclerItem item = new RecyclerItem();
        item.setId(id);
        item.setFace(face);
        item.setName(name);
        item.setPhoneNum(phoneNum);

        mList.add(item);
    }

    public void ChangeName(String id, String name){
        ContentResolver contentResolver = activity.getContentResolver();
        // 연락처 RawContact URI 생성
        String selection = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] selectionArgs = new String[]{id, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
        Toast.makeText(activity, id + " 이거 바꾼다", Toast.LENGTH_SHORT).show();
        // 변경할 데이터 설정
        ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(selection, selectionArgs)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE); // MIME 타입 설정

        // 변경 작업을 수행할 ArrayList 생성
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        operations.add(builder.build());

        // 연락처 일괄 업데이트
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, operations);
        } catch (Exception e) {
            e.printStackTrace();
            // 업데이트 실패 처리
            Toast.makeText(getActivity(), "이름 수정 실패", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        initialized = false;
    }

    public void ChangeNum(String id, String num){
        String newNum = num.replaceAll("\\D", "");
        ContentResolver contentResolver = activity.getContentResolver();
        // 연락처 RawContact URI 생성
        String selection = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] selectionArgs = new String[]{id, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};

        // 변경할 데이터 설정
        ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(selection, selectionArgs)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, newNum)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE); // MIME 타입 설정

        // 변경 작업을 수행할 ArrayList 생성
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        operations.add(builder.build());

        // 연락처 일괄 업데이트
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, operations);
        } catch (Exception e) {
            e.printStackTrace();
            // 업데이트 실패 처리
            Toast.makeText(getActivity(), "전화번호 수정 실패", Toast.LENGTH_SHORT).show();
        }
        initialized = false;
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
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, new ContactsProfileFragment(this, mList.get(position).getId(), mList.get(position).getFace(), mList.get(position).getName(), mList.get(position).getPhoneNum()));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Log.d("TAG", "Clicked Item : " + mList.get(position).getName());

    }
    private void onAddClick() {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, new ContactsNewProfileFragment(this));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Log.d("TAG", "Add Button is clicked!");
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
                    if(requestContactsWritePermission()){
                        onAddClick();
                    }

                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    public void readContacts(){
        mList.clear();
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
            requestContactsWritePermission();
            readContacts();
        } else {
            // 퍼미션을 요청
            Log.d("GALLERY", "Please grant the permission!!");
            requestContactsReadPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
        }
    }

    private boolean requestContactsWritePermission() {
        // 퍼미션을 이미 가지고 있는지 확인
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            // 퍼미션을 이미 가지고 있는 경우 Contacts 쓰는 코드 호출
            return true;
        } else {
            // 퍼미션을 요청
            Log.d("GALLERY", "Please grant the Write permission!!");
            requestContactsWritePermissionLauncher.launch(Manifest.permission.WRITE_CONTACTS);
            return false;
        }
    }
}