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

import com.example.powerise.db.Morning;
import com.example.powerise.db.MorningViewModel;

import java.time.Duration;
import java.time.LocalTime;

public class SensorActivity implements SensorEventListener {
    MorningViewModel mMorningViewModel;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Context context;
    private ImageView lightIcon;

    private boolean belowThreshold = true;
    private long belowThresholdTimestamp;

    public SensorActivity(Context context) {
        this.context = context;
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
        lightIcon = ((AppCompatActivity) context).findViewById(R.id.lightIcon);

        if (lux > 10000 && belowThreshold) {
            belowThreshold = false;
            long durationMillis = SystemClock.elapsedRealtime() - belowThresholdTimestamp;
            lightIcon.setImageResource(R.drawable.baseline_access_alarms_24);
            Morning morning = new Morning(durationMillis);
            mMorningViewModel.insert(morning);
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
