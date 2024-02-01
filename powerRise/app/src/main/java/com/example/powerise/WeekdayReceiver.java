package com.example.powerise;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WeekdayReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Alarm", "Weekday alarm triggered");
        // Create an intent to start the SoundRecorder Activity
        Intent startSoundRecorderIntent = new Intent(context, SoundRecorder.class);

        // Add FLAG_ACTIVITY_NEW_TASK since you're starting the Activity from outside of an Activity context
        startSoundRecorderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Start the SoundRecorder Activity
        context.startActivity(startSoundRecorderIntent);       // Your Weekday Code Here
    }
}