package com.example.myapplication;

import android.util.Log;

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

    public static double translate(ArrayList<Float> xs, ArrayList<Float> ys, ArrayList<Float> zs) {
        float xmin = Collections.min(xs);
        float ymin = Collections.min(ys);
        float zmin = Collections.min(zs);
        float x_length = Math.abs(Collections.max(xs) - xmin);
        float y_length = Math.abs(Collections.max(ys) - ymin);
        float z_length = Math.abs(Collections.max(zs) - zmin);
        float max_dimension = Collections.max(Arrays.asList(x_length, y_length, z_length));
        float middle_translation = 0;
        if (max_dimension == x_length) {
            middle_translation = Math.abs(xmin);
        } else if (max_dimension == y_length) {
            middle_translation = Math.abs(ymin);
        } else if (max_dimension == z_length) {
            middle_translation = Math.abs(zmin);
        }

        for (int i = 0; i < xs.size(); i++) {
            xs.set(i, xs.get(i) + middle_translation);
            ys.set(i, ys.get(i) + middle_translation);
            zs.set(i, zs.get(i) + middle_translation);
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
}
