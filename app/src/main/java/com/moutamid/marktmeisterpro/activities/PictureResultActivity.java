package com.moutamid.marktmeisterpro.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fxn.stash.Stash;
import com.moutamid.marktmeisterpro.databinding.ActivityPictureResultBinding;
import com.moutamid.marktmeisterpro.fragments.SavedImagesActivity;
import com.moutamid.marktmeisterpro.models.Stall;
import com.moutamid.marktmeisterpro.models.StallModel;
import com.moutamid.marktmeisterpro.utilis.Constants;

import java.io.File;
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
        Log.d("PATH123", "Saved  " + path);
        Glide.with(this).load(path)
                .skipMemoryCache(true) // Skip memory cache
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip disk cache
                .into(binding.image);

        int capturedImageOrientation = Constants.rotateImage(path);
        Log.d("PATH123", "capturedImageOrientation  " + capturedImageOrientation);

//        if (capturedImageOrientation == 90 || capturedImageOrientation == 270) {
//            binding.image.setRotation(-90); // Rotate the ImageView for horizontal images
//        } else {
//            binding.image.setRotation(0);  // Reset rotation for portrait images
//        }

        binding.save.setOnClickListener(v -> {
            String name = Stash.getString(Constants.NAME);
            String ID = Stash.getString(Constants.applicationID);
            ArrayList<StallModel> list = Stash.getArrayList(ID, StallModel.class);
            String tag = Stash.getBoolean(Constants.DAY_OR_NIGHT, false) ? "Nacht" : "Tag";
            if (Stash.getString(Constants.SELECTION_CAT).equals("Geschäft")) {
                list.add(new StallModel(Stash.getString(Constants.applicationID), Stash.getString(Constants.NAME), Stash.getString(Constants.SELECTION_CAT),
                        Stash.getString(Constants.SELECTION_CAT_TYPE), "ongoing", Constants.getFormatedDate(new Date().getTime()), path, tag, false));
            } else {
                list.add(new StallModel(Stash.getString(Constants.applicationID), Stash.getString(Constants.NAME), Stash.getString(Constants.SELECTION_CAT),
                        Stash.getString(Constants.SELECTION_CAT_TYPE), "ongoing", Constants.getFormatedDate(new Date().getTime()), path, "", false));
            }
            Stash.put(ID, list);
            Stall stall = new Stall(name, Stash.getString(Constants.applicationID), list);
            ArrayList<Stall> stallList = Stash.getArrayList(Constants.STALL_LIST, Stall.class);

            if (stallList.size() > 0) {
                boolean notFound = false;
                for (int i = 0; i < stallList.size(); i++) {
                    Stall s = stallList.get(i);
                    if (s.getApplicationID().equals(stall.getApplicationID())) {
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

            Toast.makeText(PictureResultActivity.this, "Bild gespeichert", Toast.LENGTH_SHORT).show();
            Stash.put(Constants.IMAGE, stall.getName());
            Stash.put(Constants.ID, stall.getApplicationID());
            Stash.clear("img");
            Stash.put(Constants.isPICTURE, true);
            finish();

        });

        binding.retake.setOnClickListener(v -> {
            Stash.clear("img");
            binding.image.setImageResource(0);
            Glide.get(PictureResultActivity.this).clearMemory();
            new Thread(() -> Glide.get(PictureResultActivity.this).clearDiskCache()).start();
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