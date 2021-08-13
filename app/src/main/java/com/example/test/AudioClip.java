package com.example.test;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class AudioClip {

    public MediaPlayer mPlayer;

    public AudioClip(String url){
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            long start=System.nanoTime();
            mPlayer.setDataSource(url);
            mPlayer.prepare();
            System.out.println("Time to prepare Audio : "+(System.nanoTime()-start));

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start()
    {
        mPlayer.start();
    }

    public double getTime()
    {
        return (double)mPlayer.getCurrentPosition()/1e3;
    }
}
