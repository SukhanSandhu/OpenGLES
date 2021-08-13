package com.example.test;


import android.opengl.GLES30;

import java.nio.FloatBuffer;

public class Model
{
    private VAO vao;
    private Shader shader;
    private Texture texture;

    public Model(FloatBuffer vertexData,int coordsPerVertex)
    {
        vao=new VAO();

        vao.load(vertexData,vertexData.array().length/coordsPerVertex,coordsPerVertex,0);
    }

    public Model(FloatBuffer vertexData,int coordsPerVertex,boolean NDC)
    {
        vao=new VAO();

        vao.load(vertexData,vertexData.array().length/coordsPerVertex,coordsPerVertex,0,!NDC);
    }

    public void attachShader(Shader shader)
    {
        this.shader=shader;
    }

    public void render()
    {
        vao.bind();
        shader.use();
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture.getID());
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vao.getVertexCount());
        //GLES30.glDrawArrays(GLES30.GL_LINES, 0, vao.getVertexCount());
    }

    public void setColorData(float[] colorData)
    {
        FloatBuffer colorBuffer=FloatBuffer.wrap(colorData);
        vao.load(colorBuffer,colorData.length/3,3,1);
    }

    public void setVertexData(FloatBuffer vertexData,int coordsPerVertex,int location)
    {
        vao.load(vertexData,vertexData.array().length/coordsPerVertex,coordsPerVertex,location);
    }

    public void setTexture(String filename,FloatBuffer textureCoords)
    {
        texture=new Texture(filename);
        vao.load(textureCoords,textureCoords.array().length/2,2,1);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture.getID());
    }

    public void setTexture(Texture texture)
    {
        this.texture=texture;
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture.getID());
    }

    public Shader getShader()
    {
        return shader;
    }

}
