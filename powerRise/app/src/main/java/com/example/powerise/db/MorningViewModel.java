package com.example.powerise.db;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MorningViewModel extends AndroidViewModel {

    private com.example.powerise.db.MorningRepository mRepository;

    private final LiveData<List<com.example.powerise.db.Morning>> mAllmornings;

    public MorningViewModel (Application application) {
        super(application);
        mRepository = new MorningRepository(application);
        mAllmornings = mRepository.getAllmornings();
    }

    LiveData<List<com.example.powerise.db.Morning>> getAllWords() { return mAllmornings; }

    public void insert(com.example.powerise.db.Morning morning) { mRepository.insert(morning); }
}
