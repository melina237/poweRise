package com.example.powerise;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SoundRecorder extends Activity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final double AMPLITUDE_THRESHOLD = 20000; // This is an approximation
    private static final int POLL_INTERVAL = 200; // milliseconds

    private MediaRecorder mRecorder = null;
    private Handler mHandler = new Handler();
    private boolean isRecording = false;

    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_recorder); // Set your layout here

        // Set file path for recording
        filePath = getExternalFilesDir(null).getAbsolutePath() + "/audio_record.3gp";

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            startRecording();
        }
    }

    private void startRecording() {
        if (isRecording) {
            return;
        }

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(filePath); // Use the app-specific directory

        try {
            mRecorder.prepare();
            mRecorder.start();
            isRecording = true;
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        } catch (Exception e) {
            Log.e("SoundRecorder", "Error starting recording: " + e.getMessage());
            isRecording = false;
        }
    }

    private Runnable mPollTask = new Runnable() {
        public void run() {
            if (isRecording) {
                double amplitude = getAmplitude();
                Log.i("SoundRecorder", "Amplitude: " + amplitude);
                if (amplitude > AMPLITUDE_THRESHOLD) {
                    stopRecording();
                } else {
                    mHandler.postDelayed(this, POLL_INTERVAL);
                }
            }
        }
    };

    private void stopRecording() {
        if (!isRecording) {
            return;
        }

        if (mRecorder != null) {
            try {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                isRecording = false;
            } catch (RuntimeException stopException) {
                Log.e("SoundRecorder", "Error stopping recording: " + stopException.getMessage());
            }
        }
    }

    public double getAmplitude() {
        if (mRecorder != null) {
            Log.i("SoundRecorder", "Amplitude");
            return (mRecorder.getMaxAmplitude() / 2700.0);
        } else {
            return 0;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            } else {
                Log.e("SoundRecorder", "Permission denied to record audio");
            }
        }
    }
}
