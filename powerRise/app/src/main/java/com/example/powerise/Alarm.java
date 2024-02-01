package com.example.powerise;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import java.util.Random;

public class Alarm extends Activity {

    private MediaPlayer mediaPlayer;
    // Array of audio file resource IDs
    private final Random random = new Random();
    int[] rawFiles = new int[]{R.raw.chipi,R.raw.power,R.raw.fellas,R.raw.loud,R.raw.notice,
            R.raw.phonk,R.raw.sigma};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm); // Set your layout here

        // Initialize MediaPlayer and set the audio source

        initializeMediaPlayer();

    }

    private void initializeMediaPlayer() {


        // Example using a raw resource. Replace R.raw.your_audio_file with your audio file.
        playAudio();
        // Optional: Set a listener to handle completion of audio playback
        mediaPlayer.setOnCompletionListener(mp -> {
            // Code to execute after the audio finishes playing
            Log.i("Alarm", "Playback completed");
            stopAudio();
            playAudio();
        });
    }

    // Method to start playing audio
    public void playAudio() {
        mediaPlayer = MediaPlayer.create(this, rawFiles[random.nextInt(rawFiles.length)]);
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    // Method to stop playing audio
    public void stopAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer resource when the Activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
