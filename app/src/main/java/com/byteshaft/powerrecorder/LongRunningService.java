package com.byteshaft.powerrecorder;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class LongRunningService extends Service {

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
                videoRecorder.start(2000);
            }
        }
    };
}
