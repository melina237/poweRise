package com.example.powerise.db.morning;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MorningDao {

    // allowing the insert of the same morning multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Morning morning);

    @Query("DELETE FROM morning_table")
    void deleteAll();

    @Query("SELECT * FROM morning_table ORDER BY ms_to_get_up ASC")
    LiveData<List<Morning>> getAlphabetizedmornings();
}
