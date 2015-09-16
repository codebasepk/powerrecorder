package com.byteshaft.powerrecorder;


import android.content.Context;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Helpers {

    public static void setOrientation(Camera.Parameters parameters) {
        Display display = ((WindowManager) AppGlobals.getContext().getSystemService
                (Context.WINDOW_SERVICE)).getDefaultDisplay();
        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                Log.i("SPY", "0");
                parameters.setRotation(90);
                break;
            case Surface.ROTATION_90:
                Log.i("SPY", "90");
                break;
            case Surface.ROTATION_180:
                Log.i("SPY", "180");
                break;
            case Surface.ROTATION_270:
                Log.i("SPY", "270");
                parameters.setRotation(180);
        }
    }

    public static int getBitRateForResolution(int width, int height) {
        // Not perfect but gets use there.
        return (width * height) * 6;
    }

    public boolean isMobileDataEnabled() {
        ConnectivityManager manager = (ConnectivityManager)
                AppGlobals.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        boolean isMobile = mobile.getDetailedState() == NetworkInfo.DetailedState.CONNECTED;
        if (isMobile) {
            Toast.makeText(AppGlobals.getContext(), "Mobile data is working",
                    Toast.LENGTH_LONG).show();
        }
        return isMobile;
    }

    public static String getTimeStamp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getDataDirectory() {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dataDirectory = sdcard + "/Android/data/";
        String directoryPath = dataDirectory
                + AppGlobals.getContext().getPackageName()
                + File.separator;
        File file = new File(directoryPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }
}
