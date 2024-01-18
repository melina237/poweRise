package com.example.powerise.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class MorningRepository {

    private com.example.powerise.db.MorningDao mMorningDao;
    private LiveData<List<com.example.powerise.db.Morning>> mAllmornings;

    // Note that in order to unit test the morningRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    MorningRepository(Application application) {
        com.example.powerise.db.MorningRoomDatabase db = com.example.powerise.db.MorningRoomDatabase.getDatabase(application);
        mMorningDao = db.morningDao();
        mAllmornings = mMorningDao.getAlphabetizedmornings();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<com.example.powerise.db.Morning>> getAllmornings() {
        return mAllmornings;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(com.example.powerise.db.Morning morning) {
        com.example.powerise.db.MorningRoomDatabase.databaseWriteExecutor.execute(() -> {
            mMorningDao.insert(morning);
        });
    }
}
