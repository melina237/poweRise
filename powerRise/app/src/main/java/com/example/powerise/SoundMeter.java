package com.example.powerise;

import android.media.MediaRecorder;
import android.util.Log;

public class SoundMeter {

    private MediaRecorder mRecorder = null;
    private boolean isRecording = false;

    public void start() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");

            try {
                mRecorder.prepare();
                mRecorder.start();
                isRecording = true;
            } catch (Exception e) {
                e.printStackTrace();
                isRecording = false;
            }
        }
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            isRecording = false;
        }
    }

    public double getAmplitude() {
        if (mRecorder != null) {
            try {
                return (mRecorder.getMaxAmplitude() / 2700.0);
            } catch (IllegalStateException e) {
                // This exception is thrown if getMaxAmplitude() is called before starting the recorder
                Log.e("SoundMeter", "getMaxAmplitude() called in illegal state: " + e.getMessage());
                return 0;
            } catch (Exception e) {
                // Handle other unexpected exceptions
                Log.e("SoundMeter", "Error in getAmplitude: " + e.getMessage());
                return 0;
            }
        } else {
            // MediaRecorder is not initialized
            Log.e("SoundMeter", "getAmplitude() called with null MediaRecorder");
            return 0;
        }
    }


    public boolean isRecording() {
        return isRecording;
    }
}
