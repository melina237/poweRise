package com.example.powerise.db.morning;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.powerise.db.RoomDatabase;

import java.util.List;

public class MorningRepository {

     MorningDao mMorningDao;
     LiveData<List<Morning>> mAllmornings;

    // Note that in order to unit test the morningRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public MorningRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        mMorningDao = db.morningDao();
        mAllmornings = mMorningDao.getAlphabetizedmornings();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Morning>> getAllmornings() {
        return mAllmornings;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Morning morning) {
        RoomDatabase.databaseWriteExecutor.execute(() -> mMorningDao.insert(morning));
    }
}
