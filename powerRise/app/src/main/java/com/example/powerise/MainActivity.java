package com.example.powerise;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.powerise.receiver.WeekdayReceiver;
import com.example.powerise.receiver.WeekendReceiver;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Schedule the alarms when the app starts
        scheduleAlarm();


        // Erstelle ein Morning-Objekt und füge es der Datenbank hinzu
        Log.i("morning", "eintrag hinzugefügt");




    }

    private void scheduleAlarm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!getSystemService(AlarmManager.class).canScheduleExactAlarms()) {
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
                return;
            }
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1); // Move to the next day

        int hourOfDay;
        PendingIntent pendingIntent;
        Log.i("Alarm", "Scheduling alarm");
        if (isWeekend(calendar)) {
            hourOfDay = 10; // Weekend alarm time
            Intent weekendIntent = new Intent(this, WeekendReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 1, weekendIntent, PendingIntent.FLAG_IMMUTABLE);
            Log.i("Alarm", "Weekend alarm scheduled");
        } else {
            hourOfDay = 6; // Weekday alarm time
            Intent weekdayIntent = new Intent(this, WeekdayReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, weekdayIntent, PendingIntent.FLAG_IMMUTABLE);
            Log.i("Alarm", "Weekday alarm scheduled");
        }

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private boolean isWeekend(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    protected void onPause() {
        super.onPause();
    }

    public void goToStatistics (View view){
        Intent intent = new Intent (this, StatisticsActivity.class);
        startActivity(intent);

    }






}

