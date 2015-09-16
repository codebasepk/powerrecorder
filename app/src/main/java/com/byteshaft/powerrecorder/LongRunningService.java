package com.byteshaft.powerrecorder;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
        String path = Environment.getExternalStorageDirectory() + File.separator + "power_recorder.txt";
        String line;
        File file = new File(path);
        fileText = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                fileText.append(line);
            }
            bufferedReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        int t = Integer.valueOf(fileText.toString());
        System.out.println(t);
        return t*1000;
    }
}
