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
        int requestCode = 0; // Start with a base request code

        // Weekdays Alarm
        for (int i = Calendar.MONDAY; i <= Calendar.FRIDAY; i++) {
            Intent weekdayIntent = new Intent(this, WeekdayReceiver.class);
            PendingIntent weekdayPendingIntent = PendingIntent.getBroadcast(this, requestCode, weekdayIntent, PendingIntent.FLAG_IMMUTABLE);
            setRepeatingAlarm(alarmManager, weekdayPendingIntent, i, 6);
            requestCode++;
        }

        // Weekend Alarms
        // Saturday
        Intent weekendIntentSaturday = new Intent(this, WeekendReceiver.class);
        PendingIntent weekendPendingIntentSaturday = PendingIntent.getBroadcast(this, requestCode, weekendIntentSaturday, PendingIntent.FLAG_IMMUTABLE);
        setRepeatingAlarm(alarmManager, weekendPendingIntentSaturday, Calendar.SATURDAY, 8);
        requestCode++;

        // Sunday
        Intent weekendIntentSunday = new Intent(this, WeekendReceiver.class);
        PendingIntent weekendPendingIntentSunday = PendingIntent.getBroadcast(this, requestCode, weekendIntentSunday, PendingIntent.FLAG_IMMUTABLE);
        setRepeatingAlarm(alarmManager, weekendPendingIntentSunday, Calendar.SUNDAY, 8);
    }



    private void setRepeatingAlarm(AlarmManager alarmManager, PendingIntent pendingIntent, int dayOfWeek, int hourOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Adjust to the next occurrence of the specified day of the week
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        int daysUntilNextDayOfWeek = (dayOfWeek - today + 7) % 7;
        if (daysUntilNextDayOfWeek == 0) { // If today is the day, check if time has already passed
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                daysUntilNextDayOfWeek = 7;
            }
        }
        calendar.add(Calendar.DAY_OF_YEAR, daysUntilNextDayOfWeek);

        // Set the alarm to repeat weekly
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
