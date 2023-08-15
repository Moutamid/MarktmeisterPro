package com.moutamid.marktmeisterpro.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.marktmeisterpro.MainActivity;
import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.databinding.ActivityPictureResultBinding;
import com.moutamid.marktmeisterpro.models.Stall;
import com.moutamid.marktmeisterpro.models.StallModel;
import com.moutamid.marktmeisterpro.utilis.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;

public class PictureResultActivity extends AppCompatActivity {
    ActivityPictureResultBinding binding;
    String path;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPictureResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        path = Stash.getString("img");
        Log.d("PATH", "Saved  " + path);
        Glide.with(this).load(path).into(binding.image);

        binding.save.setOnClickListener(v -> {
            String name = Stash.getString(Constants.NAME);
            ArrayList<StallModel> list = Stash.getArrayList(name, StallModel.class);
            list.add(new StallModel(Stash.getString(Constants.applicationID), Stash.getString(Constants.NAME), Stash.getString(Constants.SELECTION_CAT),
                    Stash.getString(Constants.SELECTION_CAT_TYPE), "ongoing", Constants.getFormatedDate(new Date().getTime()), path, false));
            Stash.put(name, list);
            Stall stall = new Stall(name, Stash.getString(Constants.applicationID), list);
            ArrayList<Stall> stallList = Stash.getArrayList(Constants.STALL_LIST, Stall.class);

            if (stallList.size() > 0) {
                boolean notFound = false;
                for (int i = 0; i < stallList.size(); i++) {
                    Stall s = stallList.get(i);
                    if (s.getName().equals(stall.getName())) {
                        stallList.get(i).setStall(list);
                        notFound = false;
                        break;
                    } else {
                        notFound = true;
                    }
                }

                if (notFound) {
                    stallList.add(stall);
                }

            } else {
                stallList.add(stall);
            }

            Stash.put(Constants.STALL_LIST, stallList);

            Toast.makeText(PictureResultActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
            Stash.put(Constants.IMAGE, stall.getName());
            startActivity(new Intent(PictureResultActivity.this, SavedImagesActivity.class));
            finish();

        });

        binding.retake.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        File fileToDelete = new File(path);
        if (fileToDelete.exists()) {
            boolean isDeleted = fileToDelete.delete();
            if (isDeleted) {
                startActivity(new Intent(this, CameraActivity.class));
                finish();
            } else {
                // Failed to delete file
            }
        }
    }
}