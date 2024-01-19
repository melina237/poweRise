package com.example.powerise;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class NoiseAlert extends Activity {

    private boolean mRunning = false;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int POLL_INTERVAL = 300;
    private Handler mHandler = new Handler();
    MediaPlayer mp = new MediaPlayer();

    private SoundMeter mSensor;
    private PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_noise_alert);
        Log.i("NoiseAlert", "Check if we have permission to record audio");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("NoiseAlert", "Permission is not granted, request it");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            Log.i("NoiseAlert", "Permission is already granted, start recording");
            initializeRecording();
        }
    }

    private void initializeRecording() {
        mSensor = new SoundMeter();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "myapp:NoiseAlert");
        start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("NoiseAlert", "Permission was granted, start recording");
                } else {
                    // Permission denied, disable functionality that depends on this permission.
                    Log.e("NoiseAlert", "Permission denied to record audio");
                }
                return;
            }
        }
    }
    private Runnable mSleepTask = new Runnable() {
        public void run() {
            double amp = mSensor.getAmplitude();

            Log.i("NoiseAlert", "runnable mSleepTask");

            start();
        }
    };

    private void start() {
        Log.i("Noise", "==== start ===");

        mSensor.start(); //starts recording
        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }

        //Noise monitoring start
        // Runnable(mPollTask) will execute after POLL_INTERVAL
        mHandler.postDelayed(mPollTask, POLL_INTERVAL)

        ;
    }

    private Runnable mPollTask = new Runnable() {
        public void run() {
            // Get the current amplitude
            double amplitude = mSensor.getAmplitude();
            Log.i("NoiseAlert", "Current Amplitude: " + amplitude);

            // Continue running this task periodically
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };


    @Override
    public void onStop() {
        super.onStop();
         Log.i("Noise", "==== onStop ===");

        //Stop noise monitoring
        stop();

    }

    private void stop() {
        Log.i("Noise", "==== Stop Noise Monitoring===");
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
        mRunning = false;

    }


}

