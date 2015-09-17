package com.byteshaft.powerrecorder;


import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceHolder;

import com.byteshaft.ezflashlight.CameraStateChangeListener;
import com.byteshaft.ezflashlight.Flashlight;
import com.byteshaft.powerrecorder.services.UploadService;

import java.io.File;
import java.io.IOException;


public class VideoRecorder extends MediaRecorder implements CameraStateChangeListener {

    private Flashlight flashlight;
    private int mRecordTime;
    private Helpers mHelpers;
    private String mPath;
    private static boolean sIsRecording;
    public static boolean isRecording() {
        return sIsRecording;
    }

    void start(android.hardware.Camera camera, SurfaceHolder holder, int time) {
        mHelpers = new Helpers();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setRotation(270);
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        camera.unlock();
        setCamera(camera);
        setAudioSource(MediaRecorder.AudioSource.MIC);
        setVideoSource(MediaRecorder.VideoSource.CAMERA);
        setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        setVideoEncodingBitRate(Helpers.getBitRateForResolution(
                AppConstants.VIDEO_WIDTH, AppConstants.VIDEO_HEIGHT));
//        setOrientation();
        setVideoSize(AppConstants.VIDEO_WIDTH, AppConstants.VIDEO_HEIGHT);
        setPreviewDisplay(holder.getSurface());
        mPath = Helpers.getDataDirectory() + File.separator + Helpers.getTimeStamp() + ".mp4";
        System.out.println(mPath);
        setOutputFile(mPath);
        try {
            prepare();
            start();
            AppGlobals.videoRecordingInProgress(true);
        } catch (IOException e) {
            e.printStackTrace();
            AppGlobals.videoRecordingInProgress(false);
            return;
        }

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRecording()) {
                    stopRecording();
                }
            }
        }, time);
    }

    public void start(int time) {
        mRecordTime = time;
        flashlight = new Flashlight(AppGlobals.getContext());
        flashlight.setCameraStateChangedListener(this);
        flashlight.setUpCameraPreview(1);
        sIsRecording = true;
    }

    public void stopRecording() {
        System.out.println("Recording Stopped...");
        stop();
        reset();
        release();
        flashlight.releaseAllResources();
        sIsRecording = false;
        AppGlobals.getContext().startService(new Intent(AppGlobals.getContext(),
                UploadService.class));
    }

    @Override
    public void onCameraInitialized() {

    }

    @Override
    public void onCameraViewSetup(Camera camera, SurfaceHolder surfaceHolder) {
        System.out.print("Recording Started...");
        start(camera, surfaceHolder, mRecordTime);
    }

    @Override
    public void onCameraBusy() {
       Log.w(AppGlobals.getLogTag(getClass()) , "Camera Busy..");
    }
}
