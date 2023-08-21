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
    public static final String DATEFORMATE = "yyMMdd";
    public static final String isBACK = "isBACK";
    public static final String isPICTURE = "isPICTURE";
    public static final String NAME = "NAME";
    public static final String SCAN_RESULT = "SCAN_RESULT";
    public static final String applicationID = "applicationID";
    public static final String ID = "ID";
    public static final String USER = "USER";
    public static final String From_Splash = "From_Splash";
    public static final String Resolution = "Resolution";
    public static final String SMALL = "SMALL";
    public static final String MEDIUM = "MEDIUM";
    public static final String LARGE = "LARGE";
    public static final String EventID = "EventID";
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
    public static final String EventIdLIST = "EventIdLIST";

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
