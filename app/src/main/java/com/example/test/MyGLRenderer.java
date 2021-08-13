package com.example.test;

import static android.opengl.GLES20.glDisable;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class MyGLRenderer implements GLSurfaceView.Renderer {
    private final String TAG = "hello";
    private final ArrayList<Model> models = new ArrayList<Model>();
    private Clip clip;

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        Shader def = new Shader("2D");

        Face roy = new Face("roy.png");
        roy.setShader(def);

        AudioClip audioClip = new AudioClip("http://stream.mcgroce.com/examples/1626423333.0863907.wav");
        clip=new Clip(roy, audioClip);

        clip.startAudio();
    }

    public void onDrawFrame(GL10 unused) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        GLES30.glClearColor((float) java.lang.Math.sin(System.nanoTime()/1e9), (float) java.lang.Math.cos(System.nanoTime()/1e9), 1.0f-(float) java.lang.Math.sin(System.nanoTime()/1e9), 1.0f);
        glDisable(GLES30.GL_DEPTH_TEST);

        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA,GLES30.GL_ONE_MINUS_SRC_ALPHA);

        clip.render();
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }
}