package com.moutamid.marktmeisterpro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.marktmeisterpro.MainActivity;
import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.databinding.ActivityCameraBinding;
import com.moutamid.marktmeisterpro.models.Stall;
import com.moutamid.marktmeisterpro.models.StallModel;
import com.moutamid.marktmeisterpro.utilis.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {
    ActivityCameraBinding binding;
    TextureView previewTextureView;
    int width, height;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private CameraCharacteristics cameraCharacteristics;
    private CaptureRequest.Builder captureRequestBuilder;
    private ImageReader imageReader;
    private Handler backgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        previewTextureView = binding.textureView;

        binding.back.setOnClickListener(v -> onBackPressed());

        binding.flash.setOnClickListener(v -> {

        });

        if (Stash.getString(Constants.Resolution, Constants.LARGE).equals(Constants.SMALL)) {
            width = 240;
            height = 240;
        } else if (Stash.getString(Constants.Resolution, Constants.LARGE).equals(Constants.MEDIUM)) {
            width = 640;
            height = 480;
        } else if (Stash.getString(Constants.Resolution, Constants.LARGE).equals(Constants.LARGE)) {
            width = 1280;
            height = 720;
        }

        // Step 4: Camera Initialization
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = null;
        try {
            cameraId = cameraManager.getCameraIdList()[0]; // Use the first camera (rear camera)
            cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId); // Initialize cameraCharacteristics
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    cameraDevice = camera;
                    imageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
                    imageReader.setOnImageAvailableListener(reader -> {
                        // Handle available images
                    }, null);
                  //  configureTextureViewSize(width, height);
                    createCameraPreview(); // Create the camera preview
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    // Handle camera disconnect
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    // Handle camera error
                }
            }, null);
        } catch (
                CameraAccessException e) {
            e.printStackTrace();
        }


        binding.camera.setOnClickListener(v -> captureImage());

    }


    private void saveCapturedImage(Image img) {
        if (img == null) {
            return;
        }

        // Convert Image to byte array
        ByteBuffer buffer = img.getPlanes()[0].getBuffer();
        byte[] imageBytes = new byte[buffer.remaining()];
        buffer.get(imageBytes);

        // Save the image to storage
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MShot");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        String tag = Stash.getBoolean(Constants.DAY_OR_NIGHT, false) ? "Nacht" : "Tag";
        String name = Stash.getString(Constants.applicationID) + "_" + Stash.getString(Constants.NAME) + "_" +
                Stash.getString(Constants.SELECTION_CAT) + "_" + Stash.getString(Constants.SELECTION_CAT_TYPE)+ "_" ;

        if (Stash.getString(Constants.SELECTION_CAT).equals("Geschäft")) {
           name = name + tag + "_";
        }
        String finalName = name;
        File[] matchingFiles = storageDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String n) {
                return n.startsWith(finalName);
            }
        });

        int highestNumber = 0;
        for (File file : matchingFiles) {
            String fileName = file.getName();
            int endIndex = fileName.lastIndexOf('_');
            int number = Integer.parseInt(fileName.substring(endIndex + 1, fileName.lastIndexOf('.')));
            if (number > highestNumber) {
                highestNumber = number;
            }
        }

        int nextNumber = highestNumber + 1;
        String newFileName = name + nextNumber + "_" + Constants.getFormatedDate(new Date().getTime()) + ".jpg";
        Log.d("NAMEEEE", name);
        Log.d("NAMEEEE", newFileName);
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        String imageFileName = "IMG_" + timeStamp + ".jpg";
        File imageFile = new File(storageDir, newFileName);

//      File imageFile = new File(storageDir, name);

        try (OutputStream os = new FileOutputStream(imageFile)) {
            os.write(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the image
        img.close();
        String image = imageFile.getAbsolutePath();
        Stash.put("img", image);
        startActivity(new Intent(CameraActivity.this, PictureResultActivity.class));
        finish();
    }

    private void createCameraPreview() {
        try {
            SurfaceTexture texture = previewTextureView.getSurfaceTexture(); // Replace with your preview view
//            assert texture != null;
            texture.setDefaultBufferSize(width, height); // Set the dimensions of the preview

            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureRequestBuilder.addTarget(surface);

            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            Log.d("ROTATION3", "rotation " + rotation);
            int rr = getOrientation(rotation) + getOrientation(rotation);
            Log.d("ROTATION3", "rr " + rr);
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, rr);

            cameraDevice.createCaptureSession(Collections.singletonList(surface),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            if (cameraDevice == null) {
                                return;
                            }

                            cameraCaptureSession = session;
                           // configureAutoFocus();
                            try {
                                captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                                session.setRepeatingRequest(captureRequestBuilder.build(), null, null);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            // Handle configuration failure
                        }
                    }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void configureTextureViewSize(int width, int height) {

        ViewGroup.LayoutParams params = previewTextureView.getLayoutParams();
        params.width = width;
        params.height = height;
        previewTextureView.setLayoutParams(params);
    }

    private int getOrientation(int rotation) {
        Log.d("ROTATION3", "OR  " + ORIENTATIONS.get(rotation));
        int sensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        return (sensorOrientation + ORIENTATIONS.get(rotation) + 360) % 360;
    }

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    private void captureImage() {
        try {
            // Create an image capture request
            CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(imageReader.getSurface());

            // Set the appropriate image quality parameters
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            captureBuilder.set(CaptureRequest.JPEG_QUALITY, (byte) 100);
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
            captureBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);

            // Create a CaptureCallback to handle the capture result
            CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {

                    Image capturedImage =  imageReader.acquireLatestImage();
                    saveCapturedImage(capturedImage);
                }
            };

            // Create a CaptureSession and capture the image
            cameraDevice.createCaptureSession(Arrays.asList(imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureCallback, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    // Handle configuration failure
                }
            }, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void configureAutoFocus() {
        if (cameraDevice == null) {
            return;
        }

        try {
            CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            // Set the focus mode to auto or continuous focus
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);

            // Trigger autofocus
            captureBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);

            // Build the capture request
            CaptureRequest previewRequest = captureBuilder.build();

            // Implement CaptureCallback for autofocus result
            cameraCaptureSession.setRepeatingRequest(previewRequest, captureCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
            if (afState != null && afState == CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED) {
                // Focus has been achieved
            }
        }
    };


}