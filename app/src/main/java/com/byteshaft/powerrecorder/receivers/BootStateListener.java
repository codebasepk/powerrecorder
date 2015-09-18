package com.byteshaft.powerrecorder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.byteshaft.powerrecorder.services.LongRunningService;


public class BootStateListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, LongRunningService.class));
    }
}
