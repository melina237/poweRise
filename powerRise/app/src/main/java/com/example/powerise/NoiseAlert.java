package com.example.powerise;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class NoiseAlert extends Activity {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private SoundMeter mSensor;

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            //Log.i("Noise", "runnable mSleepTask");

            start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noise_alert);

        // Check if we have permission to record audio
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            // Permission is already granted, start recording
            initializeRecorder();
        }
    }
        public void run() {
            Log.i("Noise", "runnable mSleepTask");
            start();
        }

    private void start() {
        Log.i("Noise", "==== start ===");
//          mSensor.start(); //starts recording
//        if (!mWakeLock.isHeld()) {
//            mWakeLock.acquire();
//        }

        Log.i("Noise","monitoring start");
        // Runnable(mPollTask) will execute after POLL_INTERVAL
//        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }
    private void initializeRecorder() {
        mSensor = new SoundMeter();
        run();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, start recording
                    initializeRecorder();
                } else {
                    // Permission denied, disable functionality that depends on this permission.
                    Log.e("NoiseAlert", "Permission denied to record audio");
                }
                return;
            }
        }
}}

