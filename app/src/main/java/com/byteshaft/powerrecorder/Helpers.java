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

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class Helpers {

    public static int getBitRateForResolution(int width, int height) {
        // Not perfect but gets use there.
        return (width * height) * 6;
    }

    public static boolean isMobileDataEnabled() {
        ConnectivityManager manager = (ConnectivityManager)
                AppGlobals.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        boolean isMobile = mobile.getDetailedState() == NetworkInfo.DetailedState.CONNECTED;
        if (isMobile) {
            Log.i("Mobile data state", "Mobile Data Working");
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

    public static ArrayList<String> getFilesIfExistAndUpload() {
        ArrayList<String> arrayList = new ArrayList<>();
        String storageDirectory = getDataDirectory();
        System.out.println("Storage dir : " + storageDirectory);
        File filePath = new File(storageDirectory);
        File[] files = filePath.listFiles();
        for (File currentFile : files) {
            if (!AppGlobals.getCurrentFileState(currentFile.getAbsolutePath()) &&
                    currentFile.getAbsolutePath().contains("mp4")) {
                arrayList.add(currentFile.getAbsolutePath());
            }
        }
        return arrayList;
    }

    public static boolean isInternetReallyWorking() {
        boolean success = false;
        try {
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

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
}
