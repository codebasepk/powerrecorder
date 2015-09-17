package com.byteshaft.powerrecorder.services;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.byteshaft.powerrecorder.AppGlobals;
import com.byteshaft.powerrecorder.Helpers;
import com.byteshaft.powerrecorder.utils.SftpHelpers;

import java.util.ArrayList;

public class UploadService extends IntentService {

    private ArrayList<String> mArrayList;
    private int mCounter = 0;


    public UploadService() {
        super("powerrecorder");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(AppGlobals.getLogTag(getClass()), "checking internet...");
        while (mCounter < 5) {
            if (Helpers.isMobileDataEnabled() && Helpers.isInternetReallyWorking()) {
                mCounter = 5;
                Log.i(AppGlobals.getLogTag(getClass()), "upload service statrted...");
                mArrayList = new ArrayList<>();
                mArrayList = Helpers.getFilesIfExistAndUpload();
                SftpHelpers.upload(mArrayList);
                break;
            } else {
                mCounter++;
                Log.i("Internet Status: ", "checking 3G again and again..");
            }
        }
    }
}
