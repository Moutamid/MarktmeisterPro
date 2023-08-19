package com.moutamid.marktmeisterpro.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.textfield.TextInputLayout;
import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.databinding.FragmentSettingBinding;
import com.moutamid.marktmeisterpro.models.EventModel;
import com.moutamid.marktmeisterpro.models.UserModel;
import com.moutamid.marktmeisterpro.utilis.Constants;

import java.util.ArrayList;

public class SettingFragment extends Fragment {
    FragmentSettingBinding binding;
    private static final int REQUEST_IMAGE_PICK = 1;
    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingBinding.inflate(getLayoutInflater(), container, false);

        UserModel userModel = (UserModel) Stash.getObject(Constants.USER, UserModel.class);
        EventModel event = (EventModel) Stash.getObject(Constants.EventIdLIST, EventModel.class);

        if (userModel != null) {
            Glide.with(requireContext()).load(userModel.getProfileLink()).placeholder(R.drawable.profile_icon).into(binding.profile);
            binding.name.getEditText().setText(userModel.getName());
            binding.surname.getEditText().setText(userModel.getSurName());
            binding.position.getEditText().setText(userModel.getPosition());
        }

        if (event != null){
            binding.eventID.getEditText().setText(event.getID());
            binding.ort.getEditText().setText(event.getCity());
        }

        binding.event.setOnClickListener(v -> {
            binding.eventBtn.setVisibility(View.VISIBLE);
            binding.profilBtn.setVisibility(View.GONE);
            binding.cameraResBtn.setVisibility(View.GONE);
        });
        binding.profil.setOnClickListener(v -> {
            binding.eventBtn.setVisibility(View.GONE);
            binding.profilBtn.setVisibility(View.VISIBLE);
            binding.cameraResBtn.setVisibility(View.GONE);
        });
        binding.cameraRes.setOnClickListener(v -> {
            binding.eventBtn.setVisibility(View.GONE);
            binding.profilBtn.setVisibility(View.GONE);
            binding.cameraResBtn.setVisibility(View.VISIBLE);
        });

        if (Stash.getString(Constants.Resolution, Constants.LARGE).equals(Constants.SMALL)) {
            binding.small.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNavIndicator)));
            binding.medium.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNav)));
            binding.large.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNav)));
        } else if (Stash.getString(Constants.Resolution, Constants.LARGE).equals(Constants.MEDIUM)) {
            binding.small.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNav)));
            binding.medium.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNavIndicator)));
            binding.large.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNav)));
        } else if (Stash.getString(Constants.Resolution, Constants.LARGE).equals(Constants.LARGE)) {
            binding.small.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNav)));
            binding.medium.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNav)));
            binding.large.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNavIndicator)));
        }

        binding.small.setOnClickListener(v -> {
            Stash.put(Constants.Resolution, Constants.SMALL);
            binding.small.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNavIndicator)));
            binding.medium.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNav)));
            binding.large.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNav)));
        });

        binding.medium.setOnClickListener(v -> {
            Stash.put(Constants.Resolution, Constants.MEDIUM);
            binding.small.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNav)));
            binding.medium.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNavIndicator)));
            binding.large.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNav)));
        });

        binding.large.setOnClickListener(v -> {
            Stash.put(Constants.Resolution, Constants.LARGE);
            binding.small.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNav)));
            binding.medium.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNav)));
            binding.large.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomNavIndicator)));
        });

        binding.save.setOnClickListener(v -> {
            UserModel user = (UserModel) Stash.getObject(Constants.USER, UserModel.class);
            if (user == null){
                user = new UserModel();
            }
            user.setName(binding.name.getEditText().getText().toString());
            user.setPosition(binding.position.getEditText().getText().toString());
            user.setSurName(binding.surname.getEditText().getText().toString());
            Stash.put(Constants.USER, user);
            Toast.makeText(requireContext(), "Profil gespeichert", Toast.LENGTH_SHORT).show();
        });

        binding.profile.setOnClickListener(v -> addImage());
        binding.upload.setOnClickListener(v -> addImage());

        binding.add.setOnClickListener(v -> {
            String ID = binding.eventID.getEditText().getText().toString();
            String ort = binding.ort.getEditText().getText().toString();
            if (ID.isEmpty()){
                Toast.makeText(requireContext(), "Empty Field", Toast.LENGTH_SHORT).show();
            } else {
                EventModel eventModel = new EventModel(ID, ort);
                Stash.put(Constants.EventIdLIST, eventModel);
                Toast.makeText(requireContext(), "Event ID gespeichert", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private void addImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, ""), REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            UserModel user = (UserModel) Stash.getObject(Constants.USER, UserModel.class);
            if (user == null) {
                user = new UserModel("","","","");
            }
            String link = data.getData().toString();
            Glide.with(requireContext()).load(link).placeholder(R.drawable.profile_icon).into(binding.profile);
            user.setProfileLink(link);
            Stash.put(Constants.USER, user);
            Toast.makeText(requireContext(), "Profil gespeichert", Toast.LENGTH_SHORT).show();
        }
    }

}