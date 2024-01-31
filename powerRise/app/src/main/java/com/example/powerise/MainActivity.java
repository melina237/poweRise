package com.example.powerise;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Schedule the alarms when the app starts
        scheduleAlarm();
    }

    private void scheduleAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Weekdays Alarm
        Intent weekdayIntent = new Intent(this, WeekdayReceiver.class);
        PendingIntent weekdayPendingIntent = PendingIntent.getBroadcast(this, 0, weekdayIntent, PendingIntent.FLAG_IMMUTABLE);

        // Weekends Alarm
        Intent weekendIntent = new Intent(this, WeekendReceiver.class);
        PendingIntent weekendPendingIntent = PendingIntent.getBroadcast(this, 1, weekendIntent, PendingIntent.FLAG_IMMUTABLE);

        // Set the alarms
        setRepeatingAlarm(alarmManager, weekdayPendingIntent, Calendar.MONDAY, 6);
        setRepeatingAlarm(alarmManager, weekdayPendingIntent, Calendar.TUESDAY, 6);
        setRepeatingAlarm(alarmManager, weekdayPendingIntent, Calendar.WEDNESDAY, 6);
        setRepeatingAlarm(alarmManager, weekdayPendingIntent, Calendar.THURSDAY, 6);
        setRepeatingAlarm(alarmManager, weekdayPendingIntent, Calendar.FRIDAY, 6);

        setRepeatingAlarm(alarmManager, weekendPendingIntent, Calendar.SATURDAY, 8);
        setRepeatingAlarm(alarmManager, weekendPendingIntent, Calendar.SUNDAY, 8);

    }

    private void setRepeatingAlarm(AlarmManager alarmManager, PendingIntent pendingIntent, int dayOfWeek, int hourOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Repeat weekly
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    protected void onPause() {
        super.onPause();
    }
}
