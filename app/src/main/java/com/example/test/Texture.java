package com.example.test;

import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glTexParameteri;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

public class Texture {
    private int[] ID=new int[1];
    private int imWidth,imHeight;
    private float texScaleX,texScaleY;

    public Texture(String filename)
    {

        GLES30.glGenTextures(1,ID,0);
        GLES30.glBindTexture(GL_TEXTURE_2D, ID[0]);

        Bitmap bit=null;
        AssetManager asset = App.getContext().getAssets();
        InputStream is;
        try {
            is = asset.open(filename);
            BitmapFactory.Options options = new BitmapFactory.Options();
            bit=BitmapFactory.decodeStream(is, null, options);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imWidth = bit.getWidth();
        imHeight = bit.getHeight();
        texScaleX = 1.0f / (float)(imWidth - 1);
        texScaleY = 1.0f / (float)(imHeight - 1);

        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D,0, GLES30.GL_RGBA,bit,0);
        glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);

    }

    public int getID()
    {
        return ID[0];
    }

    public float[] getImgSize()
    {
        return new float[]{imWidth,imHeight};
    }
}
