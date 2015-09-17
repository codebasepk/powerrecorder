package com.byteshaft.powerrecorder.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.byteshaft.powerrecorder.AppGlobals;
import com.byteshaft.powerrecorder.Helpers;
import com.byteshaft.powerrecorder.VideoRecorder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LongRunningService extends Service {

    private StringBuilder fileText;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Service Started...");
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        registerReceiver(receiver, intentFilter);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                VideoRecorder videoRecorder = new VideoRecorder();
                videoRecorder.start(readTextFile());
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

        int t = Integer.valueOf(fileText.toString());
        return t*1000;
    }
}
