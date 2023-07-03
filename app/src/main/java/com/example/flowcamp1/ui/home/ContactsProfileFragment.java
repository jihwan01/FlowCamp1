package com.example.flowcamp1.ui.home;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flowcamp1.R;
import com.example.flowcamp1.databinding.FragmentContactsProfileBinding;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;


public class ContactsProfileFragment extends Fragment {
    private String nameStr;
    private String phoneNumStr;
    private Drawable faceDrawable;
    private FragmentContactsProfileBinding binding;
    public ContactsProfileFragment(Drawable face, String name, String phoneNum) {
        this.faceDrawable = face;
        this.nameStr = name;
        this.phoneNumStr = phoneNum;
    }
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> pickGalleryLauncher;

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 200;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 퍼미션 요청 결과 처리를 위한 ActivityResultLauncher 초기화
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // 퍼미션이 승인된 경우 갤러리에서 사진을 선택하도록 호출
                Log.d("Permission", "Permission Request is Granted!!");
                pickGallery();
            } else {
                // 퍼미션이 거부된 경우에 대한 처리
                // TODO: 퍼미션 거부 시 사용자에게 안내 또는 대체 동작 수행
                Log.d("Permission", "Permission Request is denied!!");
            }
        });

        // 갤러리 결과 처리를 위한 ActivityResultLauncher 초기화
        pickGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    // 갤러리에서 선택한 사진에 대한 처리
                    Uri selectedImageUri = data.getData();
                    try {
                        Drawable drawable = getDrawableFromUri(selectedImageUri);
                        binding.profilePic.setImageDrawable(drawable);
                        // TODO: 가져온 이미지에 대해서 실제 연락처 상의 데이터를 수정해야함.
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(), "갤러리에서 사진이 선택됐습니다!", Toast.LENGTH_SHORT).show();
                    // TODO: 선택한 사진에 대한 작업 수행
                }
            } else if (result.getResultCode() == AppCompatActivity.RESULT_CANCELED) {
                // 갤러리 선택 취소에 대한 처리
                // TODO: 선택 취소 시 사용자에게 안내 또는 대체 동작 수행
                Toast.makeText(getActivity(), "갤러리에서 사진이 선택되지않았습니다!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MenuHost menuhost = requireActivity();
        initMenu(menuhost, nameStr);
        binding = FragmentContactsProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        TextView nameText = binding.profileName;
        TextView phoneNumText = binding.profileNumber;
        ImageView faceImage = binding.profilePic;
        faceImage.setImageDrawable(faceDrawable);
        faceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFaceClicked();
                // ImageView가 클릭되었을 때 실행되는 코드를 여기에 추가합니다.
            }
        });
        nameText.setText(nameStr);
        phoneNumText.setText(phoneNumStr);


        return view;
    }

    private void onBackBtnPressed() {
        getActivity().onBackPressed();
        Log.d("TAG", "Back Button Clicked!");
    }
    private void onFinPressed() {
        Log.d("TAG", "Finish Button Clicked!");
    }

    private void goToListFragment(ContactsListFragment contactsListFragment){
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, contactsListFragment);
        fragmentTransaction.commit();
    }

    private void initMenu(MenuHost menuhost, String name){
        menuhost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                androidx.appcompat.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("");
                menuInflater.inflate(R.menu.contacts_profile_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem){

                if (menuItem.getItemId() == R.id.item_fin_button){
                    onFinPressed();
                }else if (menuItem.getItemId() == android.R.id.home){
                    onBackBtnPressed();
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
    private void onFaceClicked(){
        Toast.makeText(getActivity(), "ImageView가 클릭되었습니다.", Toast.LENGTH_SHORT).show();
        requestPermission();
    }
    private void requestPermission() {
        // 퍼미션을 이미 가지고 있는지 확인
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // 이미 퍼미션이 승인된 경우 갤러리에서 사진을 선택하도록 호출
            pickGallery();
        } else {
            // 퍼미션을 요청
            Log.d("GALLERY", "Please grant the permission!!");
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        pickGalleryLauncher.launch(intent);
        Log.d("GALLERY", "Please Pick a pic from gallery!!");

    }

    private Drawable getDrawableFromUri(Uri uri) throws IOException {
        ContentResolver resolver = requireActivity().getContentResolver();
        InputStream inputStream = resolver.openInputStream(uri);
        return Drawable.createFromStream(inputStream, null);
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        Log.d("TAG", "Destroy Profile Fragment!!");
        binding = null;
    }

}