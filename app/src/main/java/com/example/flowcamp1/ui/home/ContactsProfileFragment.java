package com.example.flowcamp1.ui.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.flowcamp1.R;
import com.example.flowcamp1.databinding.FragmentContactsProfileBinding;

import org.w3c.dom.Text;


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

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        Log.d("TAG", "Destroy Profile Fragment!!");
        binding = null;
    }

}