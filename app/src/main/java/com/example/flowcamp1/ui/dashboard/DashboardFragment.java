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
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import com.example.flowcamp1.MainActivity;
import com.example.flowcamp1.R;
import com.example.flowcamp1.databinding.FragmentDashboardBinding;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    public GridView mGridView;
    public DashboardAdapter mAdapter;

    List<DashboardItem> itemList = new ArrayList<>();

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

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);
        mGridView = rootView.findViewById(R.id.gallery);

        for(int i = 1; i <= 5; i++) {
            Class<R.drawable> drawable = R.drawable.class;
            String path = "pic" + Integer.toString(i);
            int id = getResources().getIdentifier(path, "drawable", context.getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
            itemList.add(new DashboardItem(bitmap));
        }

        mAdapter = new DashboardAdapter(context, itemList);
        mGridView.setAdapter(mAdapter);

        MenuHost menuhost = requireActivity();
        initMenu(menuhost);
        return rootView;
    }

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

                        cameralauncher.launch(intent);
                    });

                    builder.setPositiveButton("갤러리에서 가져오기", (dialog, which) -> {
                        getGalleryPermission(activity, context);
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);

                        gallerylauncher.launch(intent);
                    });

                    builder.create().show();
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    ActivityResultLauncher<Intent> gallerylauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Intent data = result.getData();
        try {
            InputStream stream = getContext().getContentResolver().openInputStream(data.getData());
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
            mAdapter.setBitmap(bitmap);
            mAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    });

    ActivityResultLauncher<Intent> cameralauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Intent data = result.getData();
        try {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            mAdapter.setBitmap(bitmap);
            mAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    });
}