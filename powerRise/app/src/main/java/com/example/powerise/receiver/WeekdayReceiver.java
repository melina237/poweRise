package com.example.powerise.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.powerise.sensors.SoundSensorActivity;

public class WeekdayReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Alarm", "Weekday alarm triggered");
        // Create an intent to start the SoundSensorActivity when the alarm triggers
        Intent startSoundRecorderIntent = new Intent(context, SoundSensorActivity.class);

        // Added FLAG_ACTIVITY_NEW_TASK since we start the activity from a BroadcastReceiver and not from an Activity
        startSoundRecorderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Start SoundSensorActivity
        context.startActivity(startSoundRecorderIntent);
    }
}