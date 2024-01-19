package com.example.powerise.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;

@Entity(tableName = "morning_table")
public class Morning {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ms_to_get_up")
    public float mMorning;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "dayOfWeek")
    public String dayOfWeek;

    @ColumnInfo(name = "startAlarm")
    public String startAlarm;

    @ColumnInfo(name = "endAlarm")
    public String endAlarm;

    public Morning(@NonNull float morning, @NonNull String date, @NonNull String dayOfWeek, @NonNull String startAlarm, @NonNull String endAlarm) {
        this.mMorning = morning;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.startAlarm = startAlarm;
        this.endAlarm = endAlarm;
    }

    public String getMorning() {
        //int orientation = SensorActivity.context.getResources().getConfiguration().orientation;

        //if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Device is in landscape orientation (horizontal)
            return String.format(Locale.getDefault(), "%s, %s: %s - %s (%s)", dayOfWeek, date, startAlarm, endAlarm, mMorning);
        /*} else {
            // Device is in portrait orientation (vertical)
            return String.format(Locale.getDefault(), "%s, %s: %s", dayOfWeek, date, mMorning);
        }*/
    }
}
