package com.example.powerise;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SensorActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Context context;

    private ImageView lightIcon;

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

    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        lightIcon = ((AppCompatActivity)context).findViewById(R.id.lightIcon);

        if (lux > 10000) {
            lightIcon.setImageResource(R.drawable.baseline_access_alarms_24);
            System.out.println("gross");
        } else {
            lightIcon.setImageResource(R.drawable.baseline_access_time_24);
            System.out.println("klein");
        }

        System.out.println("wert");
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
