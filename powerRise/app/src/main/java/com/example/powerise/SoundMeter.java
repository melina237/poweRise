package com.example.powerise;

import android.media.MediaRecorder;

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
        if (mRecorder != null && isRecording) {
            return mRecorder.getMaxAmplitude();
        } else {
            return 0.0;
        }
    }

    public boolean isRecording() {
        return isRecording;
    }
}
