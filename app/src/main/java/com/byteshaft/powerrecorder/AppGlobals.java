package com.byteshaft.powerrecorder;


import android.app.Application;
import android.content.Context;

public class AppGlobals extends Application {

    private static String LOG_TAG = "Power Recorder";
    private static Context sContext;
    private static boolean sIsVideoRecording;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }

    public static void videoRecordingInProgress(boolean value) {
        sIsVideoRecording = value;
    }

    public static String getLogTag(Class aClass) {
        return LOG_TAG + aClass.getName();
    }
}
