package com.example.myapplication;

import android.util.Log;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Utils {
    /**
     * Generate new points to add to model. Generates shin and thigh points.
     **/
    private static final String TAG = "Utils";
    public static HashMap<String, HashMap<String, Float>> generate_extra_points(Map<String, Map<String, Float>> m) {
        if (m == null) {
            Log.e(TAG, "input map is null");
        }
        HashMap<String, HashMap<String, Float>> extra_points = new HashMap<>();
        HashMap<String, Float> left_shin = new HashMap<>();
        float x = m.get(Landmarks.LEFT_KNEE.getLabel()).get("x") + m.get(Landmarks.LEFT_HEEL.getLabel()).get("x");
        float y = m.get(Landmarks.LEFT_KNEE.getLabel()).get("y") + m.get(Landmarks.LEFT_HEEL.getLabel()).get("y");
        float z = m.get(Landmarks.LEFT_KNEE.getLabel()).get("z") + m.get(Landmarks.LEFT_HEEL.getLabel()).get("z");
        left_shin.put("x", x/2);
        left_shin.put("y", y/2);
        left_shin.put("z", z/2);
        extra_points.put("left_shin", left_shin);

        HashMap<String, Float> right_shin = new HashMap<>();
        x = m.get(Landmarks.RIGHT_KNEE.getLabel()).get("x") + m.get(Landmarks.RIGHT_HEEL.getLabel()).get("x");
        y = m.get(Landmarks.RIGHT_KNEE.getLabel()).get("y") + m.get(Landmarks.RIGHT_HEEL.getLabel()).get("y");
        z = m.get(Landmarks.RIGHT_KNEE.getLabel()).get("z") + m.get(Landmarks.RIGHT_HEEL.getLabel()).get("z");
        right_shin.put("x", x/2);
        right_shin.put("y", y/2);
        right_shin.put("z", z/2);
        extra_points.put("right_shin", right_shin);

        HashMap<String, Float> left_thigh = new HashMap<>();
        x = m.get(Landmarks.LEFT_HIP.getLabel()).get("x") + m.get(Landmarks.LEFT_KNEE.getLabel()).get("x");
        y = m.get(Landmarks.LEFT_HIP.getLabel()).get("y") + m.get(Landmarks.LEFT_KNEE.getLabel()).get("y");
        z = m.get(Landmarks.LEFT_HIP.getLabel()).get("z") + m.get(Landmarks.LEFT_KNEE.getLabel()).get("z");
        left_thigh.put("x", x/2);
        left_thigh.put("y", y/2);
        left_thigh.put("z", z/2);
        extra_points.put("left_thigh", left_thigh);

        HashMap<String, Float> right_thigh = new HashMap<>();
        x = m.get(Landmarks.RIGHT_HIP.getLabel()).get("x") + m.get(Landmarks.RIGHT_KNEE.getLabel()).get("x");
        y = m.get(Landmarks.RIGHT_HIP.getLabel()).get("y") + m.get(Landmarks.RIGHT_KNEE.getLabel()).get("y");
        z = m.get(Landmarks.RIGHT_HIP.getLabel()).get("z") + m.get(Landmarks.RIGHT_KNEE.getLabel()).get("z");
        right_thigh.put("x", x/2);
        right_thigh.put("y", y/2);
        right_thigh.put("z", z/2);
        extra_points.put("right_thigh", right_thigh);

        HashMap<String, Float> right_bicep = new HashMap<>();
        x = m.get(Landmarks.RIGHT_ELBOW.getLabel()).get("x") + m.get(Landmarks.RIGHT_SHOULDER.getLabel()).get("x");
        y = m.get(Landmarks.RIGHT_ELBOW.getLabel()).get("y") + m.get(Landmarks.RIGHT_SHOULDER.getLabel()).get("y");
        z = m.get(Landmarks.RIGHT_ELBOW.getLabel()).get("z") + m.get(Landmarks.RIGHT_SHOULDER.getLabel()).get("z");
        right_bicep.put("x", x/2);
        right_bicep.put("y", y/2);
        right_bicep.put("z", z/2);
        extra_points.put("right_bicep", right_bicep);

        HashMap<String, Float> left_bicep = new HashMap<>();
        x = m.get(Landmarks.LEFT_ELBOW.getLabel()).get("x") + m.get(Landmarks.LEFT_SHOULDER.getLabel()).get("x");
        y = m.get(Landmarks.LEFT_ELBOW.getLabel()).get("y") + m.get(Landmarks.LEFT_SHOULDER.getLabel()).get("y");
        z = m.get(Landmarks.LEFT_ELBOW.getLabel()).get("z") + m.get(Landmarks.LEFT_SHOULDER.getLabel()).get("z");
        left_bicep.put("x", x/2);
        left_bicep.put("y", y/2);
        left_bicep.put("z", z/2);
        extra_points.put("left_bicep", left_bicep);

        float x1 = (float) ((2.0/3.0) * m.get(Landmarks.RIGHT_SHOULDER.getLabel()).get("x") + (1.0/3.0) * m.get(Landmarks.RIGHT_HIP.getLabel()).get("x"));
        float y1 = (float) ((2.0/3.0) * m.get(Landmarks.RIGHT_SHOULDER.getLabel()).get("y") + (1.0/3.0) * m.get(Landmarks.RIGHT_HIP.getLabel()).get("y"));
        float z1 = (float) ((2.0/3.0) * m.get(Landmarks.RIGHT_SHOULDER.getLabel()).get("z") + (1.0/3.0) * m.get(Landmarks.RIGHT_HIP.getLabel()).get("z"));
        float x2 = (float) ((1.0/3.0) * m.get(Landmarks.RIGHT_SHOULDER.getLabel()).get("x") + (2.0/3.0) * m.get(Landmarks.RIGHT_HIP.getLabel()).get("x"));
        float y2 = (float) ((1.0/3.0) * m.get(Landmarks.RIGHT_SHOULDER.getLabel()).get("y") + (2.0/3.0) * m.get(Landmarks.RIGHT_HIP.getLabel()).get("y"));
        float z2 = (float) ((1.0/3.0) * m.get(Landmarks.RIGHT_SHOULDER.getLabel()).get("z") + (2.0/3.0) * m.get(Landmarks.RIGHT_HIP.getLabel()).get("z"));
        HashMap<String, Float> right_chest = new HashMap<>();
        right_chest.put("x", x1);
        right_chest.put("y", y1);
        right_chest.put("z", z1);
        extra_points.put("right_chest", right_chest);
        HashMap<String, Float> right_ab = new HashMap<>();
        right_ab.put("x", x2);
        right_ab.put("y", y2);
        right_ab.put("z", z2);
        extra_points.put("right_ab", right_ab);

        x1 = (float) ((2.0/3.0) * m.get(Landmarks.LEFT_SHOULDER.getLabel()).get("x") + (1.0/3.0) * m.get(Landmarks.LEFT_HIP.getLabel()).get("x"));
        y1 = (float) ((2.0/3.0) * m.get(Landmarks.LEFT_SHOULDER.getLabel()).get("y") + (1.0/3.0) * m.get(Landmarks.LEFT_HIP.getLabel()).get("y"));
        z1 = (float) ((2.0/3.0) * m.get(Landmarks.LEFT_SHOULDER.getLabel()).get("z") + (1.0/3.0) * m.get(Landmarks.LEFT_HIP.getLabel()).get("z"));
        x2 = (float) ((1.0/3.0) * m.get(Landmarks.LEFT_SHOULDER.getLabel()).get("x") + (2.0/3.0) * m.get(Landmarks.LEFT_HIP.getLabel()).get("x"));
        y2 = (float) ((1.0/3.0) * m.get(Landmarks.LEFT_SHOULDER.getLabel()).get("y") + (2.0/3.0) * m.get(Landmarks.LEFT_HIP.getLabel()).get("y"));
        z2 = (float) ((1.0/3.0) * m.get(Landmarks.LEFT_SHOULDER.getLabel()).get("z") + (2.0/3.0) * m.get(Landmarks.LEFT_HIP.getLabel()).get("z"));
        HashMap<String, Float> left_chest = new HashMap<>();
        left_chest.put("x", x1);
        left_chest.put("y", y1);
        left_chest.put("z", z1);
        extra_points.put("left_chest", left_chest);
        HashMap<String, Float> left_ab = new HashMap<>();
        left_ab.put("x", x2);
        left_ab.put("y", y2);
        left_ab.put("z", z2);
        extra_points.put("left_ab", left_ab);
        return extra_points;
    }

    /**
     * Matrix and vector multiplication operation
     */
    public static double[] multiply(double[][] matrix, float[] vector) {
        return Arrays.stream(matrix)
                .mapToDouble(row -> IntStream.range(0, row.length)
                        .mapToDouble(col -> row[col] * vector[col])
                        .sum())
                .toArray();
    }

    /**
     * Function to rotate points about the x-axis
     * @param xs list of x coordinates
     * @param ys list of y coordinates
     * @param zs list of z coordinates
     * @param theta angle to rotate by
     */
    public static void rotate(ArrayList<Float> xs, ArrayList<Float> ys, ArrayList<Float> zs, double theta){

        double[][] rotateX = {
                {1,0,0},
                {0, Math.cos(theta), -Math.sin(theta)},
                {0, Math.sin(theta), Math.cos(theta)}
        };
        for (int i = 0; i < xs.size(); i++) {
            float[] p = {xs.get(i), ys.get(i), zs.get(i)};
            double[] rotated_p = Utils.multiply(rotateX, p);
            xs.set(i, (float) rotated_p[0]);
            ys.set(i, (float) rotated_p[1]);
            zs.set(i, (float) rotated_p[2]);
        }
    }

    public static float translate(ArrayList<Float> xs, ArrayList<Float> ys, ArrayList<Float> zs) {
        /*
        get max dimen, divide by two, add to each val
         */
        float xmin = Collections.min(xs);
        float ymin = Collections.min(ys);
        float zmin = Collections.min(zs);
        float x_length = Math.abs(Collections.max(xs) - xmin);
        float y_length = Math.abs(Collections.max(ys) - ymin);
        float z_length = Math.abs(Collections.max(zs) - zmin);
        float max_dimension = Collections.max(Arrays.asList(x_length, y_length, z_length));
        for (int i = 0; i < xs.size(); i++) {
            if (max_dimension == x_length) xs.set(i, xs.get(i) + Math.abs(xmin));
            else xs.set(i, xs.get(i) + (max_dimension / 2));
            ys.set(i, ys.get(i) + Math.abs(ymin));
            if (max_dimension == z_length) zs.set(i, zs.get(i) + Math.abs(zmin));
            else zs.set(i, zs.get(i) + (max_dimension / 2));
        }
        return max_dimension;
    }

    public static byte[] formatData(int[] new_xs, int[] new_ys, int[] new_zs) {
        int[][][] cube = new int[8][8][8];
        for (int i = 0; i < new_xs.length; i++) {
            cube[new_ys[i]][new_xs[i]][new_zs[i]] = 1;
        }
        byte[] data = new byte[64];
        int index = 0;
        for (int y = 0; y < cube.length; y++) {
            for (int x = 0; x < cube[0].length; x++) {
                int newByte = 0;
                for (int z = 0; z < cube[0][0].length; z++) {
                    newByte = newByte << 1;
                    if (cube[y][x][z] == 1) {
                        newByte += 1;
                    }
                }
                data[index] = (byte) newByte;
                index++;
            }
        }
        return data;
    }
    public static boolean checkLandmarksFormat(Map<String, Map<String, Float>> model_points) {
        if (model_points.size() == 0) {
            return false;
        };
        for (Map.Entry<String, Map<String, Float>> entry : model_points.entrySet()){
            Map<String, Float> val = entry.getValue();
            if (val.isEmpty()) return false;
        }
        return true;
    }

    public static void filterNoise(float thresh, ArrayList<Float> prev_xs, ArrayList<Float> prev_ys, ArrayList<Float> prev_zs, ArrayList<Float> xs, ArrayList<Float> ys, ArrayList<Float> zs) {
        int size = prev_xs.size();
        for (int i = 0; i < size; i++) {
            if (Math.abs(prev_xs.get(i) - xs.get(i)) >= thresh) prev_xs.set(i, xs.get(i));
            if (Math.abs(prev_ys.get(i) - ys.get(i)) >= thresh) prev_ys.set(i, ys.get(i));
            if (Math.abs(prev_zs.get(i) - zs.get(i)) >= thresh) prev_zs.set(i, zs.get(i));
        }
    }
}
