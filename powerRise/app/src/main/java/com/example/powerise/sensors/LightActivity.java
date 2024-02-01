package com.example.powerise.sensors;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.powerise.AlarmUtil;
import com.example.powerise.MainActivity;
import com.example.powerise.R;

public class LightActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private boolean belowThreshold = true;
    private AlarmUtil alarmUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_sensor); // Replace with your layout file
        findViewById(R.id.lightIcon);
        alarmUtil = new AlarmUtil(this);
        initializeSensor();
        alarmUtil.playAudio();
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
            alarmUtil.stopAudio();
            Intent backToMain = new Intent(LightActivity.this, MainActivity.class);
            startActivity(backToMain); // Start MainActivity
            finish(); // Optionally, finish this activity if you no longer need it
        } else if (lux <= 10000 && !belowThreshold) {
            belowThreshold = true;
            lightIcon.setImageResource(R.drawable.baseline_access_time_24);
        }

        Toast.makeText(this, "Light intensity: " + lux, Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        alarmUtil.stopAudio(); // Stop audio when the activity is not in the foreground
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
        if (alarmUtil != null) {
            alarmUtil.stopAudio(); // Ensure audio is stopped and resources are released
        }
    }
}