package com.moutamid.marktmeisterpro.fragments;

import android.Manifest;
import android.app.AlertDialog;
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
import com.moutamid.marktmeisterpro.MainActivity;
import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.activities.SelectItemActivity;
import com.moutamid.marktmeisterpro.databinding.FragmentScanBinding;
import com.moutamid.marktmeisterpro.models.EventModel;
import com.moutamid.marktmeisterpro.utilis.Constants;

import java.util.ArrayList;
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
            String res = result.getText();
            String name;
            String ID;
            String eventId;
            boolean go, eventCheck = false;
            if (res.startsWith(" ")){
                res = res.substring(1);
            }

            MainActivity activity = (MainActivity) requireActivity();

            String[] parts = res.split("; ");
            eventId = parts[0].split(": ")[1];
            ID = parts[1].split(": ")[1];
            name = parts[2].split(": ")[1];

            EventModel eventModel = (EventModel) Stash.getObject(Constants.EventIdLIST, EventModel.class);
            if (eventModel == null) {
                go = false;
                eventCheck = true;
            } else {
                eventCheck = false;
                go = eventId.equals(eventModel.getID());
            }

            if (eventCheck) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Keine Event-ID gefunden")
                        .setMessage("Bitte fügen Sie eine Event-ID in den Einstellungen hinzu")
                        .setPositiveButton("OK", ((dialog, which) -> {
                            activity.bottomNavigationView.setSelectedItemId(R.id.nav_export);
                        })).show();
            } else {
                if (go) {
                    Stash.put(Constants.NAME, name);
                    Stash.put(Constants.applicationID, ID);
                    Stash.put(Constants.SCAN_RESULT, res);
                    startActivity(new Intent(requireContext(), SelectItemActivity.class));
                    requireActivity().finish();
                } else {
                    new AlertDialog.Builder(requireContext())
                            .setMessage("Der QR-Code gehört zu einer anderen Veranstaltung")
                            .setPositiveButton("Ok", ((dialog, which) -> {
                                mCodeScanner.startPreview();
                                dialog.dismiss();
                            }))
                            .show();
                }
            }
        }));

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES);
                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }

        } else {
            mCodeScanner.startPreview();
        }

        return binding.getRoot();

    }

    private String extractValue(String res, String key) {
        int startIndex = res.indexOf(key + ":");
        if (startIndex == -1) {
            return "";
        }
        startIndex += key.length() + 2;
        int endIndex = res.indexOf(";", startIndex);
        if (endIndex == -1) {
            endIndex = res.length();
        }
        return res.substring(startIndex, endIndex).trim();
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

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCodeScanner.stopPreview();
    }
}