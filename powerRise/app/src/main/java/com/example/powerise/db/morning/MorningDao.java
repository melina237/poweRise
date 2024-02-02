package com.example.powerise.db.morning;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MorningDao {

    // allowing the insert of the same morning multiple times by passing a
    // conflict resolution strategy
    // should not be necessary because of the autoGenerate = true
    // in the Morning class primary key
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Morning morning);

    @Query("DELETE FROM morning_table")
    void deleteAll();

    @Delete
    void deleteMorning(Morning morning);

    @Query("SELECT * FROM morning_table ORDER BY id ASC")
    LiveData<List<Morning>> getAlphabetizedmornings();
}
