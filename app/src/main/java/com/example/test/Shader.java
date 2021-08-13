package com.example.test;

import android.opengl.GLES30;

import com.example.test.utility.HelperFunctions;

public class Shader
{
    private int ID;

    public Shader(String name)
    {
        String vsData="";
        String fsData="";
        try {
            vsData = HelperFunctions.readTextFileFromAssets(name+"/vs.glsl");
            fsData = HelperFunctions.readTextFileFromAssets(name+"/fs.glsl");
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        int vs = GLES30.glCreateShader(GLES30.GL_VERTEX_SHADER);
        int fs = GLES30.glCreateShader(GLES30.GL_FRAGMENT_SHADER);

        GLES30.glShaderSource(vs, vsData);
        GLES30.glShaderSource(fs, fsData);
        GLES30.glCompileShader(vs);
        GLES30.glCompileShader(fs);

        ID = GLES30.glCreateProgram();
        GLES30.glAttachShader(ID, vs);
        GLES30.glAttachShader(ID, fs);
        GLES30.glLinkProgram(ID);


        GLES30.glDeleteShader(vs);
        GLES30.glDeleteShader(fs);
    }

    void use()
    {
        GLES30.glUseProgram(ID);
    }

    public int getID()
    {
        return ID;
    }

}
