package com.example.test.utility;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.test.App;
import com.example.test.Int;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HelperFunctions {
    public static String readTextFileFromAssets(String fileName) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            String string = "";
            AssetManager am = App.getContext().getAssets();
            InputStream is = am.open(fileName);

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while (true) {
                try {
                    if ((string = reader.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stringBuilder.append(string).append("\n");
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (stringBuilder.toString());
    }

    public static Bitmap loadImageFromStorage(String imageName) {
        Bitmap b = null;
        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File f = new File(path, imageName);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;

    }

    public static List<Float> readReferenceFile(String URL) {

        List<Float> list = new ArrayList<Float>();
        try {
            URL url = new URL(URL);

            Scanner scanner = new Scanner(new InputStreamReader(url.openStream()));

            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] arrOfStr = data.split(" ");
                for (int i = 0; i < 2; i++) {
                    float val = Float.parseFloat(arrOfStr[i]);
                    list.add(val);
                }
            }
            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }

    public static synchronized void readWarpedFile(String URL, int refLen, List<Float> destList, Int loadedTill, Semaphore sem) {
        synchronized (destList) {
            long start = System.nanoTime();
            //List<Float> list=new ArrayList<Float>(20000);
            try {
                URL url = new URL(URL);
                Scanner scanner = new Scanner(new InputStreamReader(url.openStream()));

                System.out.println("connecting " + (System.nanoTime() - start));
                start = System.nanoTime();

                while (scanner.hasNextLine()) {
                    String data = scanner.nextLine();
                    String[] arrOfStr = data.split(" ");
                    for (int i = 0; i < refLen; i++) {
                        float val = Float.parseFloat(arrOfStr[i]);
                        destList.add(val);
                    }
                    loadedTill.val = loadedTill.val + 1;
                }
                scanner.close();
                System.out.println("loading " + (System.nanoTime() - start));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Integer> readTriangulationFile(String URL) {

        List<Integer> list = new ArrayList<Integer>();
        try {
            URL url = new URL(URL);
            Scanner scanner = new Scanner(new InputStreamReader(url.openStream()));

            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] arrOfStr = data.split(" ");
                for (int i = 0; i < 3; i++) {
                    int val = Integer.parseInt(arrOfStr[i]);
                    int val1 = val * 2;
                    list.add(val1);
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static FloatBuffer loadTexCoords(List<Integer> tri_idx, List<Float> ref_pts, float[] imgSize) {
        float[] tex = new float[tri_idx.size() * 2];
        int index = 0;
        for (int idx = 0; idx < tri_idx.size(); idx++) {
            int val = tri_idx.get(idx);
            float ref_pt1 = ref_pts.get(val);
            float ref_pt2 = ref_pt1 * (1 / imgSize[0]);
            float ref_pt3 = ref_pts.get(val + 1);
            float ref_pt4 = ref_pt3 * (1 / imgSize[1]);

//            if (ref_pt2 < -20){
//                ref_pt2 = -20.0f;
//            }
//            else{
//                if (ref_pt2 > 20){
//                    ref_pt2 = 20.0f;
//                }
//            }
//
//            if (ref_pt4 < -20){
//                ref_pt4 = -20.0f;
//            }
//            else{
//                if (ref_pt4 > 20){
//                    ref_pt4 = 20.0f;
//                }
//            }

            tex[index] = ref_pt2;
            tex[index + 1] = ref_pt4;

            index = index + 2;
        }
        return FloatBuffer.wrap(tex);

    }


}
