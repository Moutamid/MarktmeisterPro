package com.moutamid.marktmeisterpro.utilis;

import com.fxn.stash.Stash;
import com.moutamid.marktmeisterpro.MainActivity;
import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.models.Stall;
import com.moutamid.marktmeisterpro.models.StallModel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Constants {

    static Dialog dialog;
    public static final String DATEFORMATE = "dd/MM/yyyy";
    public static final String NAME = "NAME";
    public static final String SCAN_RESULT = "SCAN_RESULT";
    public static final String applicationID = "applicationID";
    public static final String IMAGE = "IMAGE";
    public static final String IMAGE_CAPTURE = "IMAGE_CAPTURE";
    public static final String DAY_OR_NIGHT = "DAY_OR_NIGHT";
    public static final String STALL_LIST = "STALL_LIST";
    public static final String SELECTION_CAT = "SELECTION_CAT";
    public static final String SELECTION_CAT_TYPE = "SELECTION_CAT_TYPE";
    public static final String ALL_LIST = "ALL_LIST";
    public static final String Geschaft = "Geschäft";
    public static final String Anschluss = "Anschluss";
    public static final String Auslage = "Auslage";
    public static final String Dokumente = "Dokumente";

    public static String getFormatedDate(long date) {
        return new SimpleDateFormat(DATEFORMATE, Locale.getDefault()).format(date);
    }

    public static void initDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }

    public static void showDialog() {
        dialog.show();
    }

    public static void dismissDialog() {
        dialog.dismiss();
    }

    public static void saveImage(Context context, Bitmap imageBitmap) {

        Activity activity = (Activity) context;

        File mShotDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MShot");
        mShotDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";
        File imageFile = new File(mShotDir, imageFileName);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            Uri contentUri = FileProvider.getUriForFile(context, "com.moutamid.marktmeisterpro.fileprovider", imageFile);
            String image = contentUri.toString();
            String name = Stash.getString(Constants.NAME);
            ArrayList<StallModel> list = Stash.getArrayList(name, StallModel.class);
            list.add(new StallModel(Stash.getString(Constants.applicationID), Stash.getString(Constants.NAME), Stash.getString(Constants.SELECTION_CAT),
                    Stash.getString(Constants.SELECTION_CAT_TYPE), "ongoing", Constants.getFormatedDate(new Date().getTime()), image, false));
            Stash.put(name, list);
            Stall stall = new Stall(name, list);
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

            Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, MainActivity.class));
            activity.finish();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save image.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void checkApp(Activity activity) {
        String appName = "marktmeisterpro";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }
}
