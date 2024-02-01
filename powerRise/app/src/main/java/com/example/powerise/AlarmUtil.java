package com.example.powerise;


import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.Random;

public class AlarmUtil {

    private MediaPlayer mediaPlayer;
    private final Context context;
    private final Random random = new Random();
    private final int[] rawFiles = new int[]{R.raw.chipi, R.raw.power, R.raw.fellas, R.raw.loud, R.raw.notice,
            R.raw.phonk, R.raw.sigma};

    public AlarmUtil(Context context) {
        this.context = context;
    }

    // Method to start playing audio
    public void playAudio() {
        stopAudio(); // Ensure any previous audio is stopped
        mediaPlayer = MediaPlayer.create(context, rawFiles[random.nextInt(rawFiles.length)]);
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                Log.i("Alarm", "Playback completed");
                stopAudio();
                playAudio(); // Loop the sound
            });
        }
    }

    // Method to stop playing audio
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
