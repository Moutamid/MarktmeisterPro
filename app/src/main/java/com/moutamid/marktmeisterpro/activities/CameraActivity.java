package com.moutamid.marktmeisterpro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.fxn.stash.Stash;
import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.databinding.ActivityCameraBinding;
import com.moutamid.marktmeisterpro.utilis.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {
    ActivityCameraBinding binding;
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

        binding.back.setOnClickListener(v -> onBackPressed());

        binding.flash.setOnClickListener(v -> {

        });

        if (Stash.getString(Constants.Resolution, Constants.LARGE).equals(Constants.SMALL)) {
            width = 340;
            height = 340;
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
                    createCameraPreview(); // Create the camera preview
//                    configureTextureViewSize(height);
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
            int number = Integer.parseInt(fileName.substring(endIndex+1, fileName.lastIndexOf('.')));
            if (number > highestNumber) {
                highestNumber = number;
            }
        }

        int nextNumber = highestNumber + 1;
        String newFileName = name + Constants.getFormatedDate(new Date().getTime()) + "_" + nextNumber + ".jpg";
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
            SurfaceTexture texture;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                texture = binding.textureView.getSurfaceTexture();
                texture.setDefaultBufferSize(width, height);
                camera(texture);
            } else {
                binding.textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                    @Override
                    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                        surface.setDefaultBufferSize(CameraActivity.this.width, CameraActivity.this.height);
                        camera(surface);
                    }

                    @Override
                    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

                    }

                    @Override
                    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                        return false;
                    }

                    @Override
                    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

                    }
                });
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void camera(SurfaceTexture texture) {
        try {
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureRequestBuilder.addTarget(surface);

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

    private void configureTextureViewSize(int height) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int heightInPixels = height;
        float density = displayMetrics.density;
        int heightInDp = (int) (heightInPixels / density);
        ViewGroup.LayoutParams params = binding.textureView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = heightInDp;
        binding.textureView.setLayoutParams(params);
    }

    private int getOrientation(int rotation) {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        int sensorOrientation = 0;

        try {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraDevice.getId());
            sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        // Calculate the final image orientation
        int imageOrientation = (sensorOrientation - degrees + 360) % 360;
        return imageOrientation;
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
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            int rr = getOrientation(rotation);
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, rr);
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


}