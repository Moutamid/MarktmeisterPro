package com.moutamid.marktmeisterpro.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SelectItemActivity extends AppCompatActivity {
    ActivitySelectItemBinding binding;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(v -> onBackPressed());

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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void startCamera() {
//        dispatchTakePictureIntent();
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
//  TODO              intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 0);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            Toast.makeText(this, "No camera available.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//
//            // Save the image to a file
//            String imagePath = saveImageToFile(imageBitmap);
//
//            // Move the image to the desired folder
//            if (imagePath != null) {
//                moveFileToFolder(imagePath);
//            }


            Bitmap capturedBitmap  = (Bitmap) data.getExtras().get("data");
//            String imagePath = mCurrentPhotoPath;
//            if (imagePath != null) {
//                moveFileToFolder(imagePath);
//            }
            int width = 0;
            int height = 0;
            if (Stash.getString(Constants.Resolution, Constants.MEDIUM).equals(Constants.SMALL)) {
                width = 240;
                height = 240;
            } else if (Stash.getString(Constants.Resolution, Constants.MEDIUM).equals(Constants.MEDIUM)) {
                width = 640;
                height = 480;
            } else if (Stash.getString(Constants.Resolution, Constants.MEDIUM).equals(Constants.LARGE)) {
                width = 1280;
                height = 720;
            }

//            Bitmap resizedBitmap = resizeBitmap(capturedBitmap, width, height);

            Constants.saveImage(SelectItemActivity.this, capturedBitmap);
        }
    }

    private String saveImageToFile(Bitmap imageBitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = new File(storageDir, imageFileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void moveFileToFolder(String imagePath) {
        File sourceFile = new File(imagePath);
        File destFolder = new File(Environment.getExternalStorageDirectory(), "MShot");

        if (!destFolder.exists()) {
            if (destFolder.mkdir() || destFolder.isDirectory()) {
                // Folder created successfully or already exists
            }
        }

        File destFile = new File(destFolder, sourceFile.getName());

        try {
            FileInputStream inStream = new FileInputStream(sourceFile);
            FileOutputStream outStream = new FileOutputStream(destFile);
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();

            inChannel.transferTo(0, inChannel.size(), outChannel);

            inStream.close();
            outStream.close();

            // Delete the original file if needed
            // sourceFile.delete();

            // Notify MediaScanner about the new file so it shows up in gallery apps
            MediaScannerConnection.scanFile(this,
                    new String[]{destFile.toString()}, null,
                    (path, uri) -> {
                        // File is scanned and available in the media content
                    });

            String image = destFile.toString();
            String name = Stash.getString(Constants.NAME);
            ArrayList<StallModel> list = Stash.getArrayList(name, StallModel.class);
            list.add(new StallModel(Stash.getString(Constants.applicationID), Stash.getString(Constants.NAME), Stash.getString(Constants.SELECTION_CAT),
                    Stash.getString(Constants.SELECTION_CAT_TYPE), "ongoing", Constants.getFormatedDate(new Date().getTime()), image, false));
            Stash.put(name, list);
            Stall stall = new Stall(name, Stash.getString(Constants.applicationID), list);
            ArrayList<Stall> stallList = Stash.getArrayList(Constants.STALL_LIST, Stall.class);

            if (stallList.size() > 0){
                boolean notFound = false;
                for (int i = 0; i < stallList.size(); i++) {
                    Stall s = stallList.get(i);
                    if (s.getName().equals(stall.getName())){
                        stallList.get(i).setStall(list);
                        notFound = false;
                        break;
                    } else {
                        notFound = true;
                    }
                }

                if (notFound){
                    stallList.add(stall);
                }

            } else {
                stallList.add(stall);
            }

            Stash.put(Constants.STALL_LIST, stallList);

            Toast.makeText(SelectItemActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SelectItemActivity.this, MainActivity.class));
            finish();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }


    private Bitmap resizeBitmap(Bitmap originalBitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
    }

}