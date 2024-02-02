package com.example.powerise.db.morning;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.powerise.db.RoomDatabase;

import java.util.List;

public class MorningRepository {

     MorningDao mMorningDao;
     LiveData<List<Morning>> mAllmornings;

    public MorningRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        mMorningDao = db.morningDao();
        mAllmornings = mMorningDao.getAlphabetizedmornings();
    }

    public LiveData<List<Morning>> getAllmornings() {
        return mAllmornings;
    }

    public void insert(Morning morning) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mMorningDao.insert(morning));
    }
}
