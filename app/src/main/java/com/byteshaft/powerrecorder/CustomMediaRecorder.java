package com.byteshaft.powerrecorder;


import android.media.MediaRecorder;

public class CustomMediaRecorder extends MediaRecorder implements MediaRecorder.OnInfoListener {

    private static boolean mIsUsable = true;

    private static CustomMediaRecorder mCustomMediaRecorder;

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {

    }

    public static CustomMediaRecorder getInstance() {
        if (mCustomMediaRecorder == null) {
            mCustomMediaRecorder = new CustomMediaRecorder();
            return mCustomMediaRecorder;
        } else if (!isUsable()) {
            mCustomMediaRecorder = new CustomMediaRecorder();
            return mCustomMediaRecorder;
        } else {
            return mCustomMediaRecorder;
        }
    }

    static boolean isUsable() {
        return mIsUsable;
    }
}
