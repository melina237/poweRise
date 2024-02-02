package com.example.powerise;


import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.Random;

public class AlarmSound {

    private MediaPlayer mediaPlayer;
    private final Context context;
    private final Random random = new Random();

    // Array of alarm sounds
    private final int[] rawFiles = new int[]{R.raw.chipi, R.raw.power, R.raw.fellas, R.raw.loud, R.raw.notice,
           R.raw.sigma};

    public AlarmSound(Context context) {
        this.context = context;
    }

    public void playAudio() {
        stopAudio(); // Ensure any previous audio is stopped
        mediaPlayer = MediaPlayer.create(context, rawFiles[random.nextInt(rawFiles.length)]);
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                Log.i("Alarm", "Playback completed");
                stopAudio();
                playAudio(); // Loop the sound, with another file
            });
        }
    }

    public void stopAudio() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
