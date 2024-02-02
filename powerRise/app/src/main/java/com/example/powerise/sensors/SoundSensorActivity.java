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
import androidx.lifecycle.ViewModelProvider;

import com.example.powerise.AlarmSound;
import com.example.powerise.MainActivity;
import com.example.powerise.R;
import com.example.powerise.db.morning.Morning;
import com.example.powerise.db.morning.MorningViewModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SoundSensorActivity extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int POLL_INTERVAL = 100;
    private MediaRecorder mRecorder = null;
    private final Handler mHandler = new Handler();
    private boolean isRecording = false;
    private String filePath;
    private AlarmSound alarmSound;

    private MorningViewModel mMorningViewModel;
    private long belowThresholdTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_sensor);

        filePath = Objects.requireNonNull(getExternalFilesDir(null)).getAbsolutePath() + "/audio_record.3gp";
        alarmSound = new AlarmSound(this);
        mMorningViewModel = new ViewModelProvider(this).get(MorningViewModel.class);
        belowThresholdTimestamp = SystemClock.elapsedRealtime();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            alarmSound.playAudio();
            startRecording();
        }
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
                Toast.makeText(SoundSensorActivity.this, "Amplitude: " + amplitude, Toast.LENGTH_SHORT).show();
                Log.i("SoundRecorder", "Amplitude: " + amplitude);

                if (amplitude > 26214) { // 80% of max amplitude
                    Log.i("Alarm", "Amplitude exceeded threshold, switching to MainActivity");
                    stopRecording();

                    insertMorning();
                    Log.i("Alarm", "Switching to MainActivity");
                    Intent backToMain = new Intent(SoundSensorActivity.this, MainActivity.class);
                    startActivity(backToMain);
                    finish();
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
                alarmSound.stopAudio();
                Toast.makeText(SoundSensorActivity.this, "Recording stopped", Toast.LENGTH_SHORT).show();
            } catch (RuntimeException stopException) {
                Log.e("SoundRecorder", "Error stopping recording: " + stopException.getMessage());
            }
        }
    }

    public double getAmplitude() {
        if (mRecorder != null) {
            int amplitude = mRecorder.getMaxAmplitude();
            Log.i("Alarm", "getAmplitude: " + amplitude);
            return amplitude;
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


    public void insertMorning() {
        Log.i("Alarm", "Inserting morning");
        long durationSeconds = (SystemClock.elapsedRealtime() - belowThresholdTimestamp) / 1000;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE"); // "EEE" for short day of week format
        String dayOfWeek = LocalDate.now().format(formatter);

        LocalTime currentTime = LocalTime.now();

        String startTime = LocalTime.of(6, 0).toString();

        String endTime = currentTime.toString().substring(0, 8);

        Morning morning = new Morning(durationSeconds, LocalDate.now().toString(), dayOfWeek, startTime, endTime);
        mMorningViewModel.insert(morning);
        Log.i("Alarm", "Morning inserted");

    }
}
