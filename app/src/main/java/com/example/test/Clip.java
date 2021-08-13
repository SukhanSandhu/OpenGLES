package com.example.test;

public class Clip {
    private AudioClip audioClip;
    private Face face;
    private int offset;

    public Clip(Face face ,AudioClip audioClip)
    {
        this.face=face;
        this.audioClip=audioClip;
    }

    public void startAudio()
    {
        audioClip.start();
    }

    public void updateFaceDataOffset()
    {
        double time=audioClip.getTime();
        offset=(int)(time*62.5f);
        if(offset+10 > face.getFramesLoaded()) {
            System.out.println("PAUSED");
            while (offset >= face.getFramesLoaded()) {
                audioClip.mPlayer.pause();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            audioClip.mPlayer.start();
        }
    }

    public void render()
    {
        updateFaceDataOffset();
        face.updateWarpedPts(offset,face.sem);
        face.render();
    }
}
