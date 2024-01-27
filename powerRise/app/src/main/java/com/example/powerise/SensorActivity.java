package com.example.powerise;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.powerise.db.morning.Morning;
import com.example.powerise.db.morning.MorningViewModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SensorActivity implements SensorEventListener {
    private final Context context;
    private final MorningViewModel mMorningViewModel;

    private boolean belowThreshold = true;
    private long belowThresholdTimestamp;

    public SensorActivity(Context context, MorningViewModel mMorningViewModel) {
        this.context = context;
        this.mMorningViewModel = mMorningViewModel;
        initializeSensor();
    }

    private void initializeSensor() {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            Toast.makeText(context, "Light sensor not available", Toast.LENGTH_SHORT).show();
        }
    }

    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        ImageView lightIcon = ((AppCompatActivity) context).findViewById(R.id.lightIcon);

        if (lux > 10000 && belowThreshold) {
            BrightEnough(lightIcon);

        } else if (lux <= 10000 && !belowThreshold) {
            TooDim(lightIcon);
        }
//        Toast.makeText(context, "Light intensity: " + lux, Toast.LENGTH_SHORT).show();
    }

    private void TooDim(ImageView lightIcon) {
        belowThreshold = true;
        belowThresholdTimestamp = SystemClock.elapsedRealtime();
        lightIcon.setImageResource(R.drawable.baseline_access_time_24);
    }

    private void BrightEnough(ImageView lightIcon) {
        belowThreshold = false;

        long durationSeconds = (SystemClock.elapsedRealtime() - belowThresholdTimestamp) / 1000;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE"); // "EEE" for short day of week format
        String dayOfWeek = LocalDate.now().format(formatter);

        lightIcon.setImageResource(R.drawable.baseline_access_alarms_24);
        LocalTime currentTime = LocalTime.now();

        // Set the start time to 8:00 AM
        String startTime = LocalTime.of(8, 0).toString();

        // Set the end time to the current time
        String endTime = currentTime.toString().substring(0,8);


        Morning morning = new Morning(durationSeconds, LocalDate.now().toString(),dayOfWeek, startTime, endTime);
        mMorningViewModel.insert(morning);
        Log.d("New entry", morning.getMorning());
    }


}
