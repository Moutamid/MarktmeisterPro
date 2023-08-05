package com.moutamid.marktmeisterpro.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.fxn.stash.Stash;
import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.activities.SelectItemActivity;
import com.moutamid.marktmeisterpro.databinding.FragmentScanBinding;
import com.moutamid.marktmeisterpro.utilis.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanFragment extends Fragment {
    FragmentScanBinding binding;
    private CodeScanner mCodeScanner;
    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentScanBinding.inflate(getLayoutInflater(), container, false);

        mCodeScanner = new CodeScanner(requireContext(), binding.scannerView);
        mCodeScanner.setDecodeCallback(result -> requireActivity().runOnUiThread(() -> {
            Log.d("CHECKIN123", result.getText());
            String res = result.getText();
            String name = "";
            String ID = "";
            if (res.startsWith("ID")){
                Pattern idPattern = Pattern.compile("ID: (\\d{2}-\\d{2}-\\d{5})");
                Matcher idMatcher = idPattern.matcher(res);
                if (idMatcher.find()) {
                    ID = idMatcher.group(1);
                }
                Pattern namePattern = Pattern.compile("name: ([^;]+)");
                Matcher nameMatcher = namePattern.matcher(res);
                if (nameMatcher.find()) {
                    name = nameMatcher.group(1);
                }
            } else {
                String[] parts = res.split("_");

                if (parts.length >= 2) {
                    name = parts[1];
                    ID = parts[0];
                }
            }
            Log.d("CHECKIN123", name);
            Log.d("CHECKIN123", ID);
            Stash.put(Constants.NAME, name);
            Stash.put(Constants.applicationID, ID);
            Stash.put(Constants.SCAN_RESULT, res);
            startActivity(new Intent(requireContext(), SelectItemActivity.class));
            requireActivity().finish();
        }));

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES);
                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

        } else {
            mCodeScanner.startPreview();
        }

        return binding.getRoot();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                mCodeScanner.startPreview();
            } else {
                Toast.makeText(binding.getRoot().getContext(), "Permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }
}