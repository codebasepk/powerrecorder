package com.byteshaft.powerrecorder.services;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.byteshaft.powerrecorder.AppGlobals;
import com.byteshaft.powerrecorder.Helpers;
import com.byteshaft.powerrecorder.utils.SftpHelpers;

import java.util.ArrayList;

public class UploadService extends IntentService {

    private ArrayList<String> arrayList;


    public UploadService() {
        super("powerrecorder");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (Helpers.isMobileDataEnabled() && Helpers.isInternetReallyWorking()) {
            Log.i(AppGlobals.getLogTag(getClass()), "upload service statrted...");
            arrayList = new ArrayList<>();
            arrayList = Helpers.getFilesIfExistAndUpload();
            SftpHelpers.upload(arrayList);
        }
    }
}
