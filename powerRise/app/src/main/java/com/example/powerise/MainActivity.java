// MainActivity.java
package com.example.powerise;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private SensorActivity lightSensorActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lightSensorActivity = new SensorActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Call the onResume method of SensorActivity
        lightSensorActivity.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Call the onPause method of SensorActivity
        lightSensorActivity.onPause();
    }
}

