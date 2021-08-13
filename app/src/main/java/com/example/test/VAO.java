package com.example.test;

import android.opengl.GLES30;

import java.nio.FloatBuffer;

public class VAO {
    private int[] ID = new int[1];
    private int[] VBO = new int[2];
    private  int vertexCount;

    private Shader shader;

    public VAO()
    {
        GLES30.glGenVertexArrays(1, ID, 0);
        GLES30.glGenBuffers(2, VBO, 0);
        GLES30.glBindVertexArray(ID[0]);
        vertexCount=0;
    }

    public void load(FloatBuffer vertexBuffer,int vertexCount,int coordsPerVertex,int location)
    {
        this.vertexCount=vertexCount;
        bind();

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,VBO[location]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertexCount*coordsPerVertex * 4, vertexBuffer, GLES30.GL_DYNAMIC_DRAW);
        GLES30.glVertexAttribPointer(location, coordsPerVertex, GLES30.GL_FLOAT, true, 4*coordsPerVertex, 0);
        GLES30.glEnableVertexAttribArray(location);

    }

    public void load(FloatBuffer vertexBuffer,int vertexCount,int coordsPerVertex,int location,boolean normalized)
    {
        this.vertexCount=vertexCount;
        bind();

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,VBO[location]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertexCount*coordsPerVertex * 4, vertexBuffer, GLES30.GL_DYNAMIC_DRAW);
        GLES30.glVertexAttribPointer(location, coordsPerVertex, GLES30.GL_FLOAT, normalized, 4*coordsPerVertex, 0);
        GLES30.glEnableVertexAttribArray(location);

    }

    public void bind()
    {
        GLES30.glBindVertexArray(ID[0]);
    }

    public int getId()
    {
        return ID[0];
    }

    public int getVertexCount()
    {
        return vertexCount;
    }
}
