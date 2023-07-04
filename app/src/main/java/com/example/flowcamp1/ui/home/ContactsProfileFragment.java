package com.example.flowcamp1.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flowcamp1.R;
import com.example.flowcamp1.databinding.FragmentContactsProfileBinding;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class ContactsProfileFragment extends Fragment {
    private ContactsListFragment contactsListFragment;
    private String idStr;
    private String nameStr;
    private String phoneNumStr;
    private Drawable faceDrawable;
    private Activity activity;

    private ArrayList<String> grantedPermissions = new ArrayList<>();
    private FragmentContactsProfileBinding binding;
    public ContactsProfileFragment(ContactsListFragment contactsListFragment, String id, Drawable face, String name, String phoneNum) {
        this.contactsListFragment = contactsListFragment;
        this.idStr = id;
        this.faceDrawable = face;
        this.nameStr = name;
        this.phoneNumStr = phoneNum;
    }
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<String> requestCallPermissionLauncher;
    private ActivityResultLauncher<String> requestVideoCallPermissionLauncher;
    private ActivityResultLauncher<String> requestSendSmsPermissionLauncher;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;
    private ActivityResultLauncher<String> requestRecordAudioPermissionLauncher;
    private ActivityResultLauncher<Intent> pickGalleryLauncher;



    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 200;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = requireActivity();
        // 퍼미션 요청 결과 처리를 위한 ActivityResultLauncher 초기화
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // 퍼미션이 승인된 경우 갤러리에서 사진을 선택하도록 호출
                Log.d("Permission", "Permission Request is Granted!!");
                pickGallery();
                grantedPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                // 퍼미션이 거부된 경우에 대한 처리
                // TODO: 퍼미션 거부 시 사용자에게 안내 또는 대체 동작 수행
                deletePermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                Log.d("Permission", "Permission Request is denied!!");
            }
        });

        requestCallPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // 퍼미션이 승인된 경우 전화 걸기 수행
                Log.d("Permission", "권한이 충분하여 전화를 걸 수 있습니다!!");
                grantedPermissions.add(Manifest.permission.CALL_PHONE);
                MakeCall();
            } else {
                // 퍼미션이 거부된 경우에 대한 처리
                // TODO: 퍼미션 거부 시 사용자에게 안내 또는 대체 동작 수행
                deletePermission(Manifest.permission.CALL_PHONE);
                Log.d("Permission", "Permission Request is denied!!");
                Log.d("Permission", "권한이 부족하여 전화를 걸 수 없습니다");
            }
        });

        requestVideoCallPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // 퍼미션이 승인된 경우 전화 걸기 수행
                Log.d("Permission", "권한이 충분하여 전화를 걸 수 있습니다!!");
                grantedPermissions.add(Manifest.permission.CALL_PHONE);
                MakeVideoCall();
            } else {
                // 퍼미션이 거부된 경우에 대한 처리
                // TODO: 퍼미션 거부 시 사용자에게 안내 또는 대체 동작 수행
                deletePermission(Manifest.permission.CALL_PHONE);
                Log.d("Permission", "Permission Request is denied!!");
                Log.d("Permission", "권한이 부족하여 전화를 걸 수 없습니다");
            }
        });

        requestSendSmsPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // 퍼미션이 승인된 경우 전화 걸기 수행
                Log.d("Permission", "권한이 충분하여 문자를 보낼 수 있습니다!!");
                grantedPermissions.add(Manifest.permission.SEND_SMS);
                SendSms();
            } else {
                // 퍼미션이 거부된 경우에 대한 처리
                // TODO: 퍼미션 거부 시 사용자에게 안내 또는 대체 동작 수행
                deletePermission(Manifest.permission.SEND_SMS);
                Log.d("Permission", "Permission Request is denied!!");
                Log.d("Permission", "권한이 부족하여 문자를 보낼 수 없습니다");
            }
        });

        requestCameraPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // 퍼미션이 승인된 경우 전화 걸기 수행
                grantedPermissions.add(Manifest.permission.CAMERA);
                requestRecordAudioPermission();
            } else {
                // 퍼미션이 거부된 경우에 대한 처리
                // TODO: 퍼미션 거부 시 사용자에게 안내 또는 대체 동작 수행
                deletePermission(Manifest.permission.CAMERA);
                Log.d("Permission", "Permission Request is denied!!");
            }
        });

        requestRecordAudioPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // 퍼미션이 승인된 경우 전화 걸기 수행
                grantedPermissions.add(Manifest.permission.RECORD_AUDIO);
                requestVideoCallPermission();
            } else {
                // 퍼미션이 거부된 경우에 대한 처리
                // TODO: 퍼미션 거부 시 사용자에게 안내 또는 대체 동작 수행
                deletePermission(Manifest.permission.RECORD_AUDIO);
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
                        contactsListFragment.ModifyFace(idStr, drawable);
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
        final EditText editNameText = binding.profileNameEdit;
        // ---------------------------------Name---------------------------------
        nameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameText.setVisibility(View.GONE);
                editNameText.setVisibility(View.VISIBLE);
                editNameText.setText(nameText.getText());

                LinearLayout buttons = binding.buttons;
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) buttons.getLayoutParams();
                params.topToBottom = R.id.profile_name_edit; // 원하는 새로운 제약 조건을 지정합니다.
                buttons.setLayoutParams(params); // 변경된 제약 조건을 뷰에 적용합니다.
                
                editNameText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editNameText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        editNameText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||
                        actionId == EditorInfo.IME_ACTION_DONE) {
                    Toast.makeText(getActivity(), "Permission Request is Granted!!", Toast.LENGTH_SHORT).show();
                    String newName = editNameText.getText().toString();
                    nameText.setText(newName);
                    nameText.setVisibility(View.VISIBLE);
                    editNameText.setVisibility(View.GONE);

                    LinearLayout buttons = binding.buttons;
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) buttons.getLayoutParams();
                    params.topToBottom = R.id.profile_name; // 원하는 새로운 제약 조건을 지정합니다.
                    buttons.setLayoutParams(params); // 변경된 제약 조건을 뷰에 적용합니다.
                

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editNameText.getWindowToken(), 0);
                    contactsListFragment.ChangeName(idStr, newName);
                    return true;
                }
                return false;
            }
        });
        // ---------------------------------Phone Number---------------------------------
        TextView phoneNumText = binding.profileNumber;
        final EditText editNumText = binding.profileNumberEdit;
        PhoneNumberFormattingTextWatcher watcher = new PhoneNumberFormattingTextWatcher(editNumText);
        editNumText.addTextChangedListener(watcher);

        phoneNumText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumText.setVisibility(View.GONE);
                editNumText.setVisibility(View.VISIBLE);
                editNumText.setText(phoneNumText.getText());

                Button deleteButton = binding.deleteBtn;
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) deleteButton.getLayoutParams();
                params.topToBottom = R.id.profile_number_edit; // 원하는 새로운 제약 조건을 지정합니다.
                deleteButton.setLayoutParams(params); // 변경된 제약 조건을 뷰에 적용합니다.
                

                editNumText.requestFocus();
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editNumText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        editNumText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||
                        actionId == EditorInfo.IME_ACTION_DONE) {
                    String newNum = editNumText.getText().toString();
                    phoneNumText.setText(newNum);
                    phoneNumText.setVisibility(View.VISIBLE);
                    editNumText.setVisibility(View.GONE);

                    Button deleteButton = binding.deleteBtn;
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) deleteButton.getLayoutParams();
                    params.topToBottom = R.id.profile_number; // 원하는 새로운 제약 조건을 지정합니다.
                    deleteButton.setLayoutParams(params); // 변경된 제약 조건을 뷰에 적용합니다.

                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, 0);

                    contactsListFragment.ChangeNum(idStr, newNum);

                    return true;
                }
                return false;
            }
        });
        // ---------------------------------Face Image---------------------------------
        ImageView faceImage = binding.profilePic;
        faceImage.setImageDrawable(faceDrawable);
        faceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFaceClicked();
                // ImageView가 클릭되었을 때 실행되는 코드를 여기에 추가합니다.
            }
        });

        // ---------------------------------Buttons---------------------------------
        addClickListenerForButtonsGroup();
        nameText.setText(nameStr);
        phoneNumText.setText(phoneNumStr);


        return view;
    }

    private void addClickListenerForButtonsGroup(){
        ImageButton callButton = binding.callBtn;
        ImageButton textButton = binding.textBtn;
        ImageButton videoCallButton = binding.videoCallBtn;
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallPressed();
            }
        });
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSmsPressed();
            }
        });
        videoCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVideoCallPressed();
            }
        });

        Button deleteButton = binding.deleteBtn;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "delete 버튼이 클릭되었습니다.", Toast.LENGTH_SHORT).show();
                onDeletePressed();
            }
        });
    }
    private void onBackBtnPressed() {
        getActivity().onBackPressed();
        Log.d("TAG", "Back Button Clicked!");
    }
    private void onFaceClicked(){
        Toast.makeText(getActivity(), "ImageView가 클릭되었습니다.", Toast.LENGTH_SHORT).show();
        requestPermission();
    }
    private void onCallPressed(){
        Toast.makeText(activity, "call 버튼이 클릭되었습니다.", Toast.LENGTH_SHORT).show();
        requestCallPermission();
    }
    private void onSmsPressed(){
        Toast.makeText(activity, "text 버튼이 클릭되었습니다.", Toast.LENGTH_SHORT).show();
        requestSendSmsPermission();
    }
    private void onVideoCallPressed(){
        Toast.makeText(activity, "video call 버튼이 클릭되었습니다.", Toast.LENGTH_SHORT).show();
        // 야매로 permission 요청코드끼리 이어서 만듦
        requestCameraPermission();

    }

    private void onDeletePressed(){
        Toast.makeText(getActivity(), "delete button이 클릭되었습니다!", Toast.LENGTH_SHORT).show();
        Log.d("Button Click", "delete button이 클릭되었습니다!");

        ContentResolver contentResolver = activity.getContentResolver();
        String selection = ContactsContract.RawContacts._ID + " = ?";
        String[] selectionArgs = new String[] { idStr };

        try {
            int deletedContacts = contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI, selection, selectionArgs);

            if (deletedContacts > 0) {
                Toast.makeText(getActivity(), "연락처 " + deletedContacts + "개" + " 삭제 성공", Toast.LENGTH_SHORT).show();
                contactsListFragment.initialized = false;
                goToListFragment();
            } else {
                Toast.makeText(getActivity(), "해당 연락처를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "연락처 삭제 실패", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void goToListFragment(){
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

                if (menuItem.getItemId() == android.R.id.home){
                    onBackBtnPressed();
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
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
    private void requestCallPermission() {
        // 퍼미션을 이미 가지고 있는지 확인
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // 이미 퍼미션이 승인된 경우 전화를 걸도록 호출

            MakeCall();

        } else {
            // 퍼미션을 요청
            Log.d("GALLERY", "Please grant the permission!!");
            requestCallPermissionLauncher.launch(Manifest.permission.CALL_PHONE);
        }
    }
    private void requestVideoCallPermission() {
        // 퍼미션을 이미 가지고 있는지 확인
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // 이미 퍼미션이 승인된 경우 전화를 걸도록 호출
            MakeVideoCall();
        } else {
            // 퍼미션을 요청
            Log.d("GALLERY", "Please grant the permission!!");
            requestVideoCallPermissionLauncher.launch(Manifest.permission.CALL_PHONE);
        }
    }
    private void requestSendSmsPermission() {
        // 퍼미션을 이미 가지고 있는지 확인
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            // 이미 퍼미션이 승인된 경우 갤러리에서 사진을 선택하도록 호출
            SendSms();
        } else {
            // 퍼미션을 요청
            Log.d("GALLERY", "Please grant the permission!!");
            requestSendSmsPermissionLauncher.launch(Manifest.permission.SEND_SMS);
        }
    }

    private void requestCameraPermission() {
        // 퍼미션을 이미 가지고 있는지 확인
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // 이미 퍼미션이 승인된 경우 갤러리에서 사진을 선택하도록 호출
            requestRecordAudioPermission();
        } else {
            // 퍼미션을 요청
            Log.d("GALLERY", "Please grant the permission!!");
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void requestRecordAudioPermission() {
        // 퍼미션을 이미 가지고 있는지 확인
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            // 이미 퍼미션이 승인된 경우 갤러리에서 사진을 선택하도록 호출
            requestVideoCallPermission();
        } else {
            // 퍼미션을 요청
            Log.d("GALLERY", "Please grant the permission!!");
            requestRecordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    private void deletePermission(String permission){
        int index = grantedPermissions.indexOf(permission);
        if (index != -1) {
            grantedPermissions.remove(index);
        }
    }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        pickGalleryLauncher.launch(intent);
        Log.d("GALLERY", "Please Pick a pic from gallery!!");

    }

    private void MakeCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumStr));
        startActivity(intent);
//        Log.d("Call", "전화를 걸게요!!");
    }

    private void SendSms() {
        Uri uri = Uri.parse("smsto:" + phoneNumStr);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", " ");
        startActivity(intent);
    }

    private void MakeVideoCall() {
        Uri uri = Uri.parse("tel:" + phoneNumStr);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        intent.putExtra("android.telecom.extra.START_CALL_WITH_VIDEO_STATE", 3); // 3: 영상 통화
        startActivity(intent);
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