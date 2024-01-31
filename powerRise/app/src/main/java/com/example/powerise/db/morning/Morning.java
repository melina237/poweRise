package com.example.powerise.db.morning;

import android.content.Context;
import android.content.res.Configuration;

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

    public String getMorning(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Gerät ist im Querformat (horizontal)
            return String.format(Locale.getDefault(), "%s, %s: %s - %s (%s)", dayOfWeek, date, startAlarm, endAlarm, mMorning);
        } else {
            // Gerät ist im Hochformat (vertikal)
            return String.format(Locale.getDefault(), "%s, %s: %s", dayOfWeek, date, mMorning);
        }
    }
}
