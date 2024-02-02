package com.example.powerise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.powerise.db.morning.MorningListAdapter;
import com.example.powerise.db.morning.MorningViewModel;

public class StatisticsActivity extends AppCompatActivity {

    MorningViewModel mMorningViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        // Create the DiffCallback
        MorningListAdapter.MorningDiff morningDiff = new MorningListAdapter.MorningDiff(this);

        // Create the Adapter with the DiffCallback
        final MorningListAdapter adapter = new MorningListAdapter(morningDiff, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up Swipe-to-Delete
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Set up ViewModel
        mMorningViewModel = new ViewModelProvider(this).get(MorningViewModel.class);
        mMorningViewModel.getAllMornings().observe(this, adapter::submitList);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    public void goToHome (View view){
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);

    }

}

