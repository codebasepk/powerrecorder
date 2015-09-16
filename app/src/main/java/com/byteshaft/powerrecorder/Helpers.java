package com.byteshaft.powerrecorder;


import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class Helpers {

    public static void setOrientation(Camera.Parameters parameters) {
        Display display = ((WindowManager) AppGlobals.getContext().getSystemService
                (Context.WINDOW_SERVICE)).getDefaultDisplay();
        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                Log.i("SPY", "0");
                parameters.setRotation(90);
                break;
            case Surface.ROTATION_90:
                Log.i("SPY", "90");
                break;
            case Surface.ROTATION_180:
                Log.i("SPY", "180");
                break;
            case Surface.ROTATION_270:
                Log.i("SPY", "270");
                parameters.setRotation(180);
        }
    }

    public static int getBitRateForResolution(int width, int height) {
        // Not perfect but gets use there.
        return (width * height) * 6;
    }
}
