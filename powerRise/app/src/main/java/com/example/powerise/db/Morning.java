package com.example.powerise.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "morning_table")
public class Morning {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ms_to_get_up")
    public float mMorning;

    public Morning(@NonNull float morning) {this.mMorning = morning;}

    public float getmorning(){return this.mMorning;}
}