package com.moutamid.marktmeisterpro.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.databinding.ActivityPictureResultBinding;
import com.moutamid.marktmeisterpro.utilis.Constants;

public class PictureResultActivity extends AppCompatActivity {
    ActivityPictureResultBinding binding;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPictureResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bitmap imageBitmap = getIntent().getParcelableExtra("imageBitmap");
        binding.image.setImageBitmap(imageBitmap);

        binding.save.setOnClickListener(v -> {
            Constants.saveImage(this, imageBitmap);
        });

        binding.retake.setOnClickListener(v -> {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            } else {
                Toast.makeText(this, "No camera available.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
//            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, );
            Intent intent = new Intent(this, PictureResultActivity.class);
            intent.putExtra("imageBitmap", imageBitmap);
            startActivity(intent);
            finish();
        }
    }

}