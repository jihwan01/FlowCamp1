package com.example.flowcamp1.ui.dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import com.example.flowcamp1.MainActivity;
import com.example.flowcamp1.R;
import com.example.flowcamp1.databinding.FragmentDashboardBinding;
import com.example.flowcamp1.ui.home.ContactsProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class GalleryListFragment extends Fragment {

    public GridView mGridView;
    public DashboardAdapter mAdapter;

    private List<DashboardItem> itemList;
    private ViewGroup rootView;
    private Uri currentPhotoUri;
    private boolean editMode = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getGalleryPermission(Activity activity, Context context) {
        String temp = "";
        int permissionForGalleryRead = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionForGalleryRead != PackageManager.PERMISSION_GRANTED)
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";

        int permissionForGalleryWrite = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionForGalleryWrite != PackageManager.PERMISSION_GRANTED)
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        if (!temp.isEmpty())
            ActivityCompat.requestPermissions(activity, temp.trim().split(" "), 1);
    }

    private void getCameraPermission(Activity activity, Context context) {
        String temp = "";
        int permissionForCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (permissionForCamera != PackageManager.PERMISSION_GRANTED)
            temp = Manifest.permission.CAMERA;
        if (!temp.isEmpty())
            ActivityCompat.requestPermissions(activity, temp.trim().split(" "), 1);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        Context context = container.getContext();

        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.VISIBLE);

        MenuHost menuhost = requireActivity();
        initMenu(menuhost);

        bindGrid(inflater, container, context);

        mAdapter = new DashboardAdapter(context, itemList, getParentFragmentManager(), activity);
        mGridView.setAdapter(mAdapter);

        bindDelete();

        return rootView;
    }

    ActivityResultLauncher<Intent> gallerylauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Intent data = result.getData();
        if (data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            mAdapter.setBitmap(imageUri);
        }
    });

    private Uri createImageUri() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, imageFileName + ".jpg");
        return FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".fileprovider", imageFile);
    }

    ActivityResultLauncher<Intent> cameralauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Intent data = result.getData();
        mAdapter.setBitmap(currentPhotoUri);
    });


    private void initMenu(MenuHost menuhost){
        menuhost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_gallery, menu);
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem){

                if (menuItem.getItemId() == R.id.gallery_add){
                    Activity activity = getActivity();
                    Context context = getContext();
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("사진 추가하기");

                    builder.setNegativeButton("카메라로 촬영하기", (dialog, which)-> {
                        getCameraPermission(activity, context);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            currentPhotoUri = createImageUri();
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
                            cameralauncher.launch(intent);
                        }
                    });

                    builder.setPositiveButton("갤러리에서 가져오기", (dialog, which) -> {
                        getGalleryPermission(activity, context);
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            gallerylauncher.launch(intent);
                        }
                    });

                    builder.create().show();
                }

                else if (menuItem.getItemId() == R.id.gallery_del) {
                    if (editMode) {
                        List<Integer> checkedPositions = mAdapter.getSelectedPositions();
                        if (checkedPositions.size() > 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("사진 삭제하기");

                            builder.setNegativeButton("취소", null);

                            builder.setPositiveButton("삭제", (dialog, which) -> {
                                for(int i = 0; i < checkedPositions.size(); i++) {
                                    mAdapter.delBitmap(checkedPositions.get(i));
                                }
                                mAdapter.notifyDataSetChanged();
                            });

                            builder.create().show();
                        }
                        editMode = false;
                        mAdapter.changeMode(editMode);
                    }
                    else {
                        editMode = true;
                        mAdapter.changeMode(editMode);
                    }
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void bindGrid(LayoutInflater inflater, ViewGroup container, Context context){
        itemList = new ArrayList<>();

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gallery_list, container, false);
        mGridView = rootView.findViewById(R.id.gallery);

        try {
            File[] files = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).listFiles();
            for(File file:files) {
                FileInputStream fis = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                itemList.add(new DashboardItem(bitmap, file.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bindDelete(){
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> a_parent, View v, int pos, long id){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("사진 삭제하기");

                builder.setNegativeButton("취소", null);

                builder.setPositiveButton("삭제", (dialog, which) -> {
                    //final int pos = mGridView.getCheckedItemPosition();
                    mAdapter.delBitmap(pos);
                    mAdapter.notifyDataSetChanged();
                });

                builder.create().show();

                return true;
            }
        });
    }


}