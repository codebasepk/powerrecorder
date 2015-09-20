package com.byteshaft.powerrecorder.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.byteshaft.powerrecorder.AppGlobals;
import com.byteshaft.powerrecorder.Helpers;
import com.byteshaft.powerrecorder.R;
import com.byteshaft.powerrecorder.VideoRecorder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LongRunningService extends Service {

    private StringBuilder fileText;
    public static boolean serviceRunning = false;
    private VideoRecorder videoRecorder;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceRunning = true;
        Log.i( AppGlobals.getLogTag(getClass()),"Service Started...");
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, intentFilter);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Power Recorder")
                        .setContentText("Running");
        startForeground(AppGlobals.NOTIFICATION_ID, mBuilder.build());
        videoRecorder = new VideoRecorder();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoRecorder.release();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)
                    || intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

                if (!VideoRecorder.isRecording()) {
                    videoRecorder.start(readTextFile());
                }
            }
        }
    };

    public int readTextFile() {
        String path = Helpers.getDataDirectory();
        String line;
        File file = new File(path + File.separator + AppGlobals.TEXT_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileWriter writer;
            try {
                writer = new FileWriter(new File(path, AppGlobals.TEXT_FILE));
                writer.append("10");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fileText = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Helpers.
                    getDataDirectory()+File.separator + AppGlobals.TEXT_FILE));
            while ((line = bufferedReader.readLine()) != null) {
                fileText.append(line);
            }
            bufferedReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String valueFromFile  = fileText.substring(0, Math.min(fileText.length(), 3));
        System.out.println(valueFromFile);
        int t;
        if (checkIfStringIsNumber(valueFromFile)) {
            t = Integer.valueOf(valueFromFile);
        } else {
            t = 10;
        }
        return t*1000;
    }

    private boolean checkIfStringIsNumber(String videoDuration) {
        String regexStr = "^[0-9]*$";

        return videoDuration.trim().matches(regexStr);
    }
}
