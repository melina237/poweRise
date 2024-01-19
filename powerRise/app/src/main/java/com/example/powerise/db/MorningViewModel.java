package com.example.powerise.db;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MorningViewModel extends AndroidViewModel {

    private MorningRepository mRepository;

    private final LiveData<List<Morning>> mAllmornings;

    public MorningViewModel (Application application) {
        super(application);
        mRepository = new MorningRepository(application);
        mAllmornings = mRepository.getAllmornings();
    }

    public LiveData<List<Morning>> getAllMornings() { return mAllmornings; }

    public void insert(Morning morning) { mRepository.insert(morning); }
}
