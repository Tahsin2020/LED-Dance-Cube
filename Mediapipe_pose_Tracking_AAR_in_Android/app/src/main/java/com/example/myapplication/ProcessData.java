package com.example.myapplication;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ProcessData {
    static ArrayList<Float> prev_xs = new ArrayList<>(Collections.nCopies(16, 0F));
    static ArrayList<Float> prev_ys = new ArrayList<>(Collections.nCopies(16, 0F));
    static ArrayList<Float> prev_zs = new ArrayList<>(Collections.nCopies(16, 0F));
    static final float ZSCALE = 0.5F;
    static final float THRESH = 0.1F;
    public static byte[] process(Map<String, Map<String, Float>> model_points){
        HashSet<String> whitelist = new HashSet<>(Arrays.asList("11", "12", "13", "14", "15", "16", "23", "24", "25", "26", "27", "28"));
        ArrayList<Float> xs = new ArrayList<>();
        ArrayList<Float> ys = new ArrayList<>();
        ArrayList<Float> zs = new ArrayList<>();

        //populate xs ys zs
        for (Map.Entry<String, Map<String, Float>> entry : model_points.entrySet()) {
            String k = entry.getKey();
            Map<String, Float> v = entry.getValue();
            if (whitelist.contains((k))) {
                xs.add(v.get("x"));
                ys.add(v.get("y"));
                zs.add(v.get("z"));
            }
        }

        //add extra pose landmarks
        HashMap<String, HashMap<String, Float>> extra_points = Utils.generate_extra_points(model_points);
        for (HashMap<String, Float> v : extra_points.values()) {
            xs.add(v.get("x"));
            ys.add(v.get("y"));
            zs.add(v.get("z"));
        }

        //normalized coord changes
        float xhips = (model_points.get(Landmarks.LEFT_HIP.getLabel()).get("x") + model_points.get(Landmarks.RIGHT_HIP.getLabel()).get("x")) / 2;
        float yhips = (model_points.get(Landmarks.LEFT_HIP.getLabel()).get("y") + model_points.get(Landmarks.RIGHT_HIP.getLabel()).get("y")) / 2;
        float zhips = (model_points.get(Landmarks.LEFT_HIP.getLabel()).get("z") + model_points.get(Landmarks.RIGHT_HIP.getLabel()).get("z")) / 2;

        xs.replaceAll(aFloat -> aFloat - xhips);
        ys.replaceAll(aFloat -> aFloat - yhips);
        ys.replaceAll(aFloat -> aFloat * -1);

        float minz = Collections.min(zs);
        float zrange = Math.abs(Collections.max(zs) - minz);
        zs.replaceAll(aFloat -> (aFloat - minz) / zrange);
        float zhip_normalized = (zhips - minz) / zrange;
        zs.replaceAll(aFloat -> (aFloat - zhip_normalized) * ZSCALE);

        //Rotate coords about the X axis
        double theta = Math.PI/8;
        Utils.rotate(xs, ys, zs, theta);

//        System.out.println("\nDATA POINTS\n");
//        System.out.println("Xs: " + xs);
//        System.out.println("Ys: " + ys);
//        System.out.println("Zs: " + zs);
        //translate from origin from hips to corner of cube
        double max_dimension = Utils.translate(xs, ys, zs);

        Utils.filterNoise(THRESH, prev_xs, prev_ys, prev_zs, xs, ys, zs);

        int[] new_xs = new int[xs.size()];
        int[] new_ys = new int[xs.size()];
        int[] new_zs = new int[xs.size()];
        //scale down to 8x8x8
        double scale_factor = max_dimension/7.0;
        for (int i = 0; i < xs.size(); i++) {
            int x = (int) Math.round(prev_xs.get(i) / scale_factor);
            if (x < 0) x = 0;
            else if (x > 7) {
                x = 7;
            }
            int y = (int) Math.round(prev_ys.get(i) / scale_factor);
            if (y < 0) y = 0;
            else if (y > 7) {
                y = 7;
            }
            int z = (int) Math.round(prev_zs.get(i) / scale_factor);
            if (z < 0) z = 0;
            else if (z > 7) z = 7;

            new_xs[i] = x;
            new_ys[i] = y;
            new_zs[i] = z;
        }
//        System.out.println("\nDATA POINTS\n");
//        System.out.println("Xs: " + Arrays.toString(new_xs));
//        System.out.println("Ys: " + Arrays.toString(new_ys));
//        System.out.println("Zs: " + Arrays.toString(new_zs));
        byte[] data = Utils.formatData(new_xs, new_ys, new_zs);

        return data;
        //Send "data" to DE1-SOC board
    }
}
