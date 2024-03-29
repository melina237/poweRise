package com.example.powerise.sensors;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.powerise.AlarmSound;
import com.example.powerise.MainActivity;
import com.example.powerise.R;
import com.example.powerise.db.morning.Morning;
import com.example.powerise.db.morning.MorningViewModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class LightSensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private boolean belowThreshold = true;
    private AlarmSound alarmSound;

    private long belowThresholdTimestamp;

    public MorningViewModel mMorningViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_sensor);
        alarmSound = new AlarmSound(this);
        belowThresholdTimestamp = SystemClock.elapsedRealtime();
        initializeSensor();
        alarmSound.playAudio();
    }

    private void initializeSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            Toast.makeText(this, "Light sensor not available", Toast.LENGTH_SHORT).show();
        } else {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        ImageView lightIcon = findViewById(R.id.lightIcon);

        if (lux > 1000 && belowThreshold) {
            belowThreshold = false;
            lightIcon.setImageResource(R.drawable.baseline_access_alarms_24);

            insertMorning();


            alarmSound.stopAudio();
            Intent backToMain = new Intent(LightSensorActivity.this, MainActivity.class);
            startActivity(backToMain);
            finish();
        } else if (lux <= 1000 && !belowThreshold) {
            belowThreshold = true;
            belowThresholdTimestamp = SystemClock.elapsedRealtime();
            lightIcon.setImageResource(R.drawable.baseline_access_time_24);
        }

        Toast.makeText(this, "Light intensity: " + lux, Toast.LENGTH_SHORT).show();
    }




    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        alarmSound.stopAudio();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alarmSound != null) {
            alarmSound.stopAudio();
        }
    }

    public void insertMorning() {
        Log.i("Alarm", "Inserting morning");
        long durationSeconds = (SystemClock.elapsedRealtime() - belowThresholdTimestamp) / 1000;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE"); // "EEE" for short day of week format
        String dayOfWeek = LocalDate.now().format(formatter);

        LocalTime currentTime = LocalTime.now();

        String startTime = LocalTime.of(10, 0).toString();

        String endTime = currentTime.toString().substring(0,8);

        mMorningViewModel = new ViewModelProvider(this).get(MorningViewModel.class);
        Morning morning = new Morning(durationSeconds, LocalDate.now().toString(),dayOfWeek, startTime, endTime);
        mMorningViewModel.insert(morning);
        Log.i("Alarm", "Morning inserted");
    }
}
