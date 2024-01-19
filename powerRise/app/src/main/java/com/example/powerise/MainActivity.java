package com.example.powerise;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private SensorActivity lightSensorActivity;
    private MorningViewModel mMorningViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final MorningListAdapter adapter = new MorningListAdapter(new MorningListAdapter.MorningDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMorningViewModel = new ViewModelProvider(this).get(MorningViewModel.class);
        mMorningViewModel.getAllMornings().observe(this, mornings -> {
            // Update the cached copy of the mornings in the adapter.
            adapter.submitList(mornings);
        });

        lightSensorActivity = new SensorActivity(this, mMorningViewModel);


    }

    @Override
    protected void onResume() {
        super.onResume();
        // Call the onResume method of SensorActivity
        lightSensorActivity.onResume();
    }
    public void startSoundRecorder(View view) {
        Intent intent = new Intent(this, SoundRecorder.class);
        startActivity(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Call the onPause method of SensorActivity
        lightSensorActivity.onPause();
    }
}

