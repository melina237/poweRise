package com.example.powerise.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.powerise.sensors.LightSensorActivity;

public class WeekendReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Alarm", "Weekend alarm triggered");

        // Create an intent to start the SoundSensorActivity when the alarm triggers
        Intent startLightSensorIntent = new Intent(context, LightSensorActivity.class);

        // Added FLAG_ACTIVITY_NEW_TASK since we start the activity from a BroadcastReceiver and not from an Activity
        startLightSensorIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Start SoundSensorActivity
        context.startActivity(startLightSensorIntent);
    }
}

