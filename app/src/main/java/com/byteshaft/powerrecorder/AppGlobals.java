package com.byteshaft.powerrecorder;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppGlobals extends Application {

    private static String LOG_TAG = "Power Recorder";
    private static Context sContext;
    private static boolean sIsVideoRecording;
    private static SharedPreferences sPreferences;
    public static final String TEXT_FILE = "power_recorder.txt";

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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

    public static SharedPreferences getPreferenceManager() {
        return sPreferences;
    }

    public static void saveCurrentVideoName(String key, boolean value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static boolean getCurrentFileState(String key) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean(key, false);
    }
}
