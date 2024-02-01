package com.example.powerise.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.powerise.sensors.LightActivity;


public class WeekendReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Alarm", "Weekend  alarm triggered");
        // Create an intent to start the SoundRecorder Activity
        Intent startLightSensorIntent = new Intent(context, LightActivity.class);

        // Add FLAG_ACTIVITY_NEW_TASK since you're starting the Activity from outside of an Activity context
        startLightSensorIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Start the SoundRecorder Activity
        context.startActivity(startLightSensorIntent);
    }
}
