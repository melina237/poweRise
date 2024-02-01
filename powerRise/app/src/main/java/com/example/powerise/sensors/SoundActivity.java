package com.example.powerise.sensors;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.powerise.AlarmUtil;
import com.example.powerise.MainActivity;
import com.example.powerise.R;

import com.example.powerise.db.morning.Morning;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SoundActivity extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final double AMPLITUDE_THRESHOLD = 20000; // This is an approximation
    private static final int POLL_INTERVAL = 200; // milliseconds
    private MediaRecorder mRecorder = null;
    private final Handler mHandler = new Handler();
    private boolean isRecording = false;
    private String filePath;
    private AlarmUtil alarmUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_sensor); // Set your layout here

        filePath = Objects.requireNonNull(getExternalFilesDir(null)).getAbsolutePath() + "/audio_record.3gp";
        alarmUtil = new AlarmUtil(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        }
        alarmUtil.playAudio(); // Play sound when the amplitude threshold is exceeded

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startRecording();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRecording();
    }

    private void startRecording() {
        if (isRecording) {
            return;
        }

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(filePath);

        try {
            mRecorder.prepare();
            mRecorder.start();
            isRecording = true;
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        } catch (Exception e) {
            Log.e("SoundRecorder", "Error starting recording: " + e.getMessage());
            isRecording = false;
            Toast.makeText(this, "Error starting recording", Toast.LENGTH_SHORT).show();
        }
    }

    private final Runnable mPollTask = new Runnable() {
        public void run() {
            if (isRecording) {
                double amplitude = getAmplitude();
                Log.i("SoundRecorder", "Amplitude: " + amplitude);
                if (amplitude > AMPLITUDE_THRESHOLD) {
                    stopRecording();
                    alarmUtil.playAudio(); // Play sound when the amplitude threshold is exceeded
                    // Morning inserten


                    long durationSeconds = (SystemClock.elapsedRealtime() - belowThresholdTimestamp) / 1000;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE"); // "EEE" for short day of week format
                    String dayOfWeek = LocalDate.now().format(formatter);

                    lightIcon.setImageResource(R.drawable.baseline_access_alarms_24);
                    LocalTime currentTime = LocalTime.now();

                    String startTime = LocalTime.of(8, 0).toString();

                    String endTime = currentTime.toString().substring(0,8);
                    Morning morning = new Morning(durationSeconds, LocalDate.now().toString(),dayOfWeek, startTime, endTime);
                    mMorningViewModel.insert(morning);
                    Intent backToMain = new Intent(SoundActivity.this, MainActivity.class);
                    startActivity(backToMain); // Start MainActivity
                    finish(); // Optionally, finish this activity if you no longer need it
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
                alarmUtil.stopAudio(); // Stop any playing audio
                Toast.makeText(SoundActivity.this, "Recording stopped", Toast.LENGTH_SHORT).show();
            } catch (RuntimeException stopException) {
                Log.e("SoundRecorder", "Error stopping recording: " + stopException.getMessage());
            }
        }
    }

    public double getAmplitude() {
        if (mRecorder != null) {
            return (mRecorder.getMaxAmplitude() / 2700.0);
        } else {
            return 0;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            } else {
                Log.e("SoundRecorder", "Permission denied to record audio");
                Toast.makeText(this, "Permission denied to record audio", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
