package com.example.test;

import android.graphics.Bitmap;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.example.test.utility.HelperFunctions;
import com.example.test.utility.Semaphore;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Face {
    private List<Float> refPts;
    private final List<Float> warpedPts;
    private List<Integer> triangulationData;

    private FloatBuffer warpedPtsBuffer;
    private FloatBuffer refPtsBuffer;

    private Bitmap image;

    private Model model;
    private Model BGSquare;
    private Texture face;
    private Texture background;

    private final Int framesLoaded = new Int(0);
    public Thread loadingThread;
    public Semaphore sem=new Semaphore(true);

    public Face(String path)
    {
        face=new Texture(path);
        background=new Texture("roy_bg.png");
        long start= System.nanoTime();
        refPts = HelperFunctions.readReferenceFile("http://stream.mcgroce.com/txt/examples_cartoon/pred_fls_1626423333.0863907_audio_embed/reference_points.txt");
        warpedPts =  Collections.synchronizedList(new ArrayList<Float>(20000));
        loadingThread=new Thread(() -> HelperFunctions.readWarpedFile("http://stream.mcgroce.com/txt/examples_cartoon/pred_fls_1626423333.0863907_audio_embed/warped_points.txt", refPts.size(),warpedPts, framesLoaded,sem)
        );

        loadingThread.start();

//        try {
//            Thread.sleep(20);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        triangulationData = HelperFunctions.readTriangulationFile("http://stream.mcgroce.com/txt/examples_cartoon/pred_fls_1626423333.0863907_audio_embed/triangulation.txt");
        System.out.println("Time to get txt files : "+(System.nanoTime()-start));
        while (framesLoaded.val<10)
        {
            System.out.println("frames Loaded = "+framesLoaded.val);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("frames Loaded = "+framesLoaded.val);
        updateWarpedPts(0,sem);
        model=new Model(warpedPtsBuffer,2);

        FloatBuffer texCoords=HelperFunctions.loadTexCoords(triangulationData,refPts,face.getImgSize());

        model.setVertexData(texCoords,2,1);

        model.setTexture(face);

        float ratio=face.getImgSize()[0]/face.getImgSize()[1];

        float[] NDCSquare=
                {
                       -1,ratio,
                        -1,-ratio,
                        1,-ratio,
                        -1,ratio,
                        1,-ratio,
                        1,ratio
                };

        float[] NDCSquareTexCords=
                {
                        0,0,
                        0,1,
                        1,1,
                        0,0,
                        1,1,
                        1,0

                };
        BGSquare=new Model(FloatBuffer.wrap(NDCSquare),2);
        BGSquare.setVertexData(FloatBuffer.wrap(NDCSquareTexCords),2,1);
        BGSquare.setTexture(background);


    }

    public synchronized void updateWarpedPts(int offset, Semaphore sem)
    {
        synchronized (warpedPts) {

            int st = offset * refPts.size();
            int en = st + refPts.size();

            List<Float> wrp_pts_i = warpedPts.subList(st, en);
            float[] verData = new float[triangulationData.size() * 2];
            int j = 0;
            for (int idx = 0; idx < triangulationData.size(); idx++) {
                int pt = triangulationData.get(idx);
                try {
                    float wrp_pt1 = wrp_pts_i.get(pt);
                    float wrp_pt2 = wrp_pts_i.get(pt + 1);

                    verData[j] = wrp_pt1;
                    verData[j + 1] = wrp_pt2;
                    j = j + 2;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            warpedPtsBuffer = FloatBuffer.wrap(verData);
        }
    }

    public void setShader(Shader shader)
    {
        model.attachShader(shader);
        setMatrices(shader);
        BGSquare.attachShader(new Shader("default"));
    }

    public void render()
    {
        model.setVertexData(warpedPtsBuffer,2,0);
        BGSquare.render();
        model.render();
    }

    public void setMatrices(Shader shader)
    {
        shader.use();
        final float[] mMVPMatrix = new float[16];//model view projection matrix
        final float[] mProjectionMatrix = new float[16];//projection matrix
        final float[] mViewMatrix = new float[16];//view matrix
        final float[] mMVMatrix=new float[16];//model view matrix
        final float[] mModelMatrix=new float[16];//model  matrix

        float[] imgSize=getImageRes();
        int[] viewport=new int[4];
        GLES30.glGetIntegerv(GLES30.GL_VIEWPORT,viewport,0);

        //Matrix.orthoM(mProjectionMatrix, 0, viewport[0], imgSize[0], imgSize[1]*((float) viewport[3]/viewport[2]), viewport[1], 0, 1);
        Matrix.orthoM(mProjectionMatrix, 0, viewport[0], viewport[2], viewport[3], viewport[1], 0, 1);

        System.out.println("viewport : "+viewport[0]+","+viewport[1]+","+viewport[2]+","+viewport[3]);
        System.out.println("image size  : "+imgSize[0]+","+imgSize[1]);

        Matrix.setIdentityM(mMVPMatrix, 0);//set the model view projection matrix to an identity matrix
        Matrix.setIdentityM(mMVMatrix, 0);//set the model view  matrix to an identity matrix
        Matrix.setIdentityM(mModelMatrix, 0);//set the model matrix to an identity matrix

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0,
                0.0f, 0f, 1.0f,
                0f, 0f, 0f,
                0f, 1f, 0.0f);

        Matrix.scaleM(mModelMatrix,0,viewport[2]/imgSize[0],viewport[2]/imgSize[0],1);
//        Matrix.scaleM(mModelMatrix,0,0.3f,0.3f,1);
        float offset=(float)viewport[3]-(imgSize[1]*viewport[2]/imgSize[0]);
        Matrix.translateM(mModelMatrix,0,0,offset/4,0);

//        Matrix.multiplyMM(mViewMatrix,0,mViewMatrix,0,mModelMatrix,0);
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        model.getShader().use();

        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(model.getShader().getID(), "model"), 1, false, mModelMatrix, 0);
        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(model.getShader().getID(), "view"), 1, false, mViewMatrix, 0);
        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(model.getShader().getID(), "projection"), 1, false, mProjectionMatrix, 0);
    }

    float[] getImageRes()
    {
        return face.getImgSize();
    }

    int getFramesLoaded()
    {
        return framesLoaded.val;
    }
}
