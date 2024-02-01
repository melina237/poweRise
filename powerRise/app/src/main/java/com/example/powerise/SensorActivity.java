package com.example.powerise;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.powerise.db.morning.Morning;
import com.example.powerise.db.morning.MorningViewModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SensorActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor lightSensor;
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
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            Toast.makeText(context, "Light sensor not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        ImageView lightIcon = ((AppCompatActivity) context).findViewById(R.id.lightIcon);

        if (lux > 10000 && belowThreshold) {
            belowThreshold = false;

            long durationSeconds = (SystemClock.elapsedRealtime() - belowThresholdTimestamp) / 1000;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE"); // "EEE" for short day of week format
            String dayOfWeek = LocalDate.now().format(formatter);

            LocalTime currentTime = LocalTime.now();

            String startTime = LocalTime.of(8, 0).toString();

            String endTime = currentTime.toString().substring(0,8);


            Morning morning = new Morning(durationSeconds, LocalDate.now().toString(),dayOfWeek, startTime, endTime);
            mMorningViewModel.insert(morning);
            //Log.d("neuer eintrag", morning.getMorning());

            lightIcon.setImageResource(R.drawable.baseline_access_alarms_24);


        } else if (lux <= 10000 && !belowThreshold) {
            belowThreshold = true;
            belowThresholdTimestamp = SystemClock.elapsedRealtime();
            lightIcon.setImageResource(R.drawable.baseline_access_time_24);
        }
        Toast.makeText(context, "Light intensity: " + lux, Toast.LENGTH_SHORT).show();
    }

    public void onResume() {
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void onPause() {
        sensorManager.unregisterListener(this);
    }
}
