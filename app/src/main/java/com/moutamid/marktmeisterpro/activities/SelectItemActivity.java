package com.moutamid.marktmeisterpro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.marktmeisterpro.MainActivity;
import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.databinding.ActivitySelectItemBinding;
import com.moutamid.marktmeisterpro.models.Stall;
import com.moutamid.marktmeisterpro.models.StallModel;
import com.moutamid.marktmeisterpro.utilis.Constants;
import com.shrikanthravi.library.NightModeButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class SelectItemActivity extends AppCompatActivity {
    ActivitySelectItemBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(v -> {
            Stash.put(Constants.From_Splash, false);
            onBackPressed();
        });

        String name = Stash.getString(Constants.NAME);
        binding.name.setText(name);

        binding.geschaft.setOnClickListener(v -> {
            binding.geschaftBtn.setVisibility(View.VISIBLE);
            binding.anschlusseBtn.setVisibility(View.GONE);
            binding.auslageBtn.setVisibility(View.GONE);
            binding.dokumenteBtn.setVisibility(View.GONE);
        });

        binding.anschlusse.setOnClickListener(v -> {
            binding.geschaftBtn.setVisibility(View.GONE);
            binding.anschlusseBtn.setVisibility(View.VISIBLE);
            binding.auslageBtn.setVisibility(View.GONE);
            binding.dokumenteBtn.setVisibility(View.GONE);
        });
        binding.auslage.setOnClickListener(v -> {
            binding.geschaftBtn.setVisibility(View.GONE);
            binding.anschlusseBtn.setVisibility(View.GONE);
            binding.auslageBtn.setVisibility(View.VISIBLE);
            binding.dokumenteBtn.setVisibility(View.GONE);
        });

        binding.dokumente.setOnClickListener(v -> {
            binding.geschaftBtn.setVisibility(View.GONE);
            binding.anschlusseBtn.setVisibility(View.GONE);
            binding.auslageBtn.setVisibility(View.GONE);
            binding.dokumenteBtn.setVisibility(View.VISIBLE);
        });

        Stash.put(Constants.DAY_OR_NIGHT, false);

        binding.switchImage.setOnSwitchListener(isNight -> Stash.put(Constants.DAY_OR_NIGHT, isNight));

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SelectItemActivity.this, MainActivity.class));
        finish();
    }

    public void geschaftClick(View view) {
        Button btn = (Button) view;
        Stash.put(Constants.SELECTION_CAT, "Geschäft");
        Stash.put(Constants.SELECTION_CAT_TYPE, btn.getText().toString());
        startCamera();
    }

    public void anschlusseClick(View view) {
        Button btn = (Button) view;
        Stash.put(Constants.SELECTION_CAT, "Anschlüsse");
        Stash.put(Constants.SELECTION_CAT_TYPE, btn.getText().toString());
        startCamera();
    }

    public void auslageClick(View view) {
        Button btn = (Button) view;
        Stash.put(Constants.SELECTION_CAT, "Auslage");
        Stash.put(Constants.SELECTION_CAT_TYPE, btn.getText().toString());
        startCamera();
    }
    public void dokumenteClick(View view) {
        Button btn = (Button) view;
        Stash.put(Constants.SELECTION_CAT, "Dokumente");
        Stash.put(Constants.SELECTION_CAT_TYPE, btn.getText().toString());
        startCamera();
    }

    private void startCamera() {
        startActivity(new Intent(this, CameraActivity.class));
        finish();
    }

}