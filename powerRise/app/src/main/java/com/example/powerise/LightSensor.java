package com.example.powerise;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LightSensor extends AppCompatActivity implements SensorEventListener {


    private SensorManager sensorManager;
    private Sensor lightSensor;
    private  boolean belowThreshold = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_sensor); // Replace with your layout file
                initializeSensor();
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
        // Sensor accuracy changes handling
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        ImageView lightIcon = (ImageView) findViewById(R.id.lightIcon);


        if (lux > 10000 && belowThreshold) {
            belowThreshold = false;

            lightIcon.setImageResource(R.drawable.baseline_access_alarms_24);


        } else if (lux <= 10000 && !belowThreshold) {
            belowThreshold = true;
            long belowThresholdTimestamp = SystemClock.elapsedRealtime();
            lightIcon.setImageResource(R.drawable.baseline_access_time_24);
        }
        Toast.makeText(this, "Light intensity: " + lux, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}
