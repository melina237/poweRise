package com.example.powerise.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.powerise.sensors.LightSensor;


public class WeekendReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Alarm", "Weekend alarm triggered");
        Intent startLightSensorIntent = new Intent(context, LightSensor.class);
        startLightSensorIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startLightSensorIntent);
    }
}

