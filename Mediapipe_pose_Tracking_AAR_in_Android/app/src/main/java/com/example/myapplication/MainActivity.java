/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.myapplication;

import static com.example.myapplication.HomePageActivity.data_output;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

//import com.example.testui.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.mediapipe.components.CameraHelper;
import com.google.mediapipe.components.CameraXPreviewHelper;
import com.google.mediapipe.components.ExternalTextureConverter;
import com.google.mediapipe.components.FrameProcessor;
import com.google.mediapipe.components.PermissionHelper;
import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmark;
import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmarkList;
import com.google.mediapipe.formats.proto.LandmarkProto.Landmark;
import com.google.mediapipe.formats.proto.LandmarkProto.LandmarkList;
import com.google.mediapipe.framework.AndroidAssetUtil;
import com.google.mediapipe.framework.PacketGetter;
import com.google.mediapipe.glutil.EglManager;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Main activity of MediaPipe example apps.
 */
public class MainActivity extends AppCompatActivity {
    //networking variables
    private static final String NTAG = "Networking";
    private static final long fps = 30;
    private static long ms_per_frame = 1000/fps;
    Thread Thread1 = null;

    private static final String TAG = "MainActivity";
    private static final String BINARY_GRAPH_NAME = "pose_tracking_gpu.binarypb";
    private static final String INPUT_VIDEO_STREAM_NAME = "input_video";
    private static final String OUTPUT_VIDEO_STREAM_NAME = "output_video";
    private static final String OUTPUT_LANDMARKS_STREAM_NAME = "pose_landmarks";
    private static final int NUM_HANDS = 2;
    private static final CameraHelper.CameraFacing CAMERA_FACING = CameraHelper.CameraFacing.BACK;
    // Flips the camera-preview frames vertically before sending them into FrameProcessor to be
    // processed in a MediaPipe graph, and flips the processed frames back when they are displayed.
    // This is needed because OpenGL represents images assuming the image origin is at the bottom-left
    // corner, whereas MediaPipe in general assumes the image origin is at top-left.
    private static final boolean FLIP_FRAMES_VERTICALLY = true;

    static {
        // Load all native libraries needed by the app.
        System.loadLibrary("mediapipe_jni");
        System.loadLibrary("opencv_java3");
    }

    // {@link SurfaceTexture} where the camera-preview frames can be accessed.
    private SurfaceTexture previewFrameTexture;
    // {@link SurfaceView} that displays the camera-preview frames processed by a MediaPipe graph.
    private SurfaceView previewDisplayView;
    // Creates and manages an {@link EGLContext}.
    private EglManager eglManager;
    // Sends camera-preview frames into a MediaPipe graph for processing, and displays the processed
    // frames onto a {@link Surface}.
    private FrameProcessor processor;
    // Converts the GL_TEXTURE_EXTERNAL_OES texture from Android camera into a regular texture to be
    // consumed by {@link FrameProcessor} and the underlying MediaPipe graph.
    private ExternalTextureConverter converter;
    // ApplicationInfo for retrieving metadata defined in the manifest.
    private ApplicationInfo applicationInfo;
    // Handles camera access via the {@link CameraX} Jetpack support library.
    private CameraXPreviewHelper cameraHelper;

    private AppBarConfiguration appBarConfiguration;
    //private ActivityMainBinding binding;


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Home_page:
                startActivity(new Intent(this, HomePageActivity.class));
                return true;
            case R.id.patterns:
                Intent intent = new Intent(this, PatternActivity.class);
                startActivity(intent);
                return true;
            case R.id.brightness:
                startActivity(new Intent(this, BrightnessActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayoutResId());

        previewDisplayView = new SurfaceView(this);
        setupPreviewDisplayView();


        try {
            applicationInfo =
                    getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Cannot find application info: " + e);
        }

        // Initialize asset manager so that MediaPipe native libraries can access the app assets, e.g.,
        // binary graphs.
        AndroidAssetUtil.initializeNativeAssetManager(this);

        eglManager = new EglManager(null);
        processor =
                new FrameProcessor(
                        this,
                        eglManager.getNativeContext(),
                        BINARY_GRAPH_NAME,
                        INPUT_VIDEO_STREAM_NAME,
                        OUTPUT_VIDEO_STREAM_NAME);
        processor
                .getVideoSurfaceOutput()
                .setFlipY(FLIP_FRAMES_VERTICALLY);

            // Initialize asset manager so that MediaPipe native libraries can access the app assets, e.g.,
            // binary graphs.
            AndroidAssetUtil.initializeNativeAssetManager(this);
            eglManager = new EglManager(null);
            processor =
                    new FrameProcessor(
                            this,
                            eglManager.getNativeContext(),
                            BINARY_GRAPH_NAME,
                            INPUT_VIDEO_STREAM_NAME,
                            OUTPUT_VIDEO_STREAM_NAME);

            processor
                    .getVideoSurfaceOutput()
                    .setFlipY(FLIP_FRAMES_VERTICALLY);


            //if (Log.isLoggable(TAG, Log.VERBOSE)) {
            processor.addPacketCallback(
                    OUTPUT_LANDMARKS_STREAM_NAME,
                    (packet) -> {
                        Log.v(TAG, "Received pose landmarks packet.");
                        try {

                            byte[] landmarksRaw = PacketGetter.getProtoBytes(packet);
                            LandmarkList poseLandmarks = LandmarkList.parseFrom(landmarksRaw);
                        } catch (InvalidProtocolBufferException exception) {
                            Log.e(TAG, "Failed to get proto.", exception);
                        }
                    });
        
        //}

        // To show verbose logging, run:
        // adb shell setprop log.tag.MainActivity VERBOSE
//        if (Log.isLoggable(TAG, Log.VERBOSE)) {
//Uncomment here         processor.addPacketCallback(
//                OUTPUT_LANDMARKS_STREAM_NAME,
//                (packet) -> {
//                    Log.v(TAG, "Received Pose landmarks packet.");
        processor.addPacketCallback(
                OUTPUT_LANDMARKS_STREAM_NAME,
                (packet) -> {
//                    Log.v(TAG, "Received Pose landmarks packet.");
                    long startTime = System.currentTimeMillis();
                    try {
//                        NormalizedLandmarkList poseLandmarks = PacketGetter.getProto(packet, NormalizedLandmarkList.class);
                        byte[] landmarksRaw = PacketGetter.getProtoBytes(packet);
                        NormalizedLandmarkList poseLandmarks = NormalizedLandmarkList.parseFrom(landmarksRaw);
//                        Log.v(TAG, "[TS:" + packet.getTimestamp() + "] " + getPoseLandmarksDebugString(poseLandmarks));
                        Map<String, Map<String, Float>> model_points = formatLandmarks(poseLandmarks);
                        if (Utils.checkLandmarksFormat(model_points)) {
                            byte[] dataToSend = ProcessData.process(model_points);
//                            Log.v(TAG, "dataToSend created, sending over wifi...");
                            long endTime = System.currentTimeMillis();
                            if (endTime - startTime < ms_per_frame) {
                                Thread.sleep(ms_per_frame - (endTime - startTime));
                            }
                            new Thread(new Thread3(dataToSend)).start();
                        }
                        SurfaceHolder srh = previewDisplayView.getHolder();
//
//                  -- this line cannot Running --
//                    Canvas canvas = null;
//                    try {
////                        NormalizedLandmarkList poseLandmarks = PacketGetter.getProto(packet, NormalizedLandmarkList.class);
//                        byte[] landmarksRaw = PacketGetter.getProtoBytes(packet);
//                        NormalizedLandmarkList poseLandmarks = NormalizedLandmarkList.parseFrom(landmarksRaw);
//                        Log.v(TAG, "[TS:" + packet.getTimestamp() + "] " + getPoseLandmarksDebugString(poseLandmarks));
//                        SurfaceHolder srh = previewDisplayView.getHolder();
////
////                  -- this line cannot Running --
////                    Canvas canvas = null;
////                    try {
////                        canvas= srh.lockCanvas();
////                        synchronized(srh){
////                            Paint paint = new Paint();
////                            paint.setColor(Color.RED);
////                            canvas.drawCircle(10.0f,10.0f,10.0f,paint);
////                        }
////                    }finally{
////                        if(canvas != null){
////                            srh.unlockCanvasAndPost(canvas);
////                        }
////                    }
//////                    processor.getVideoSurfaceOutput().setSurface(srh.getSurface());
//                    } catch (InvalidProtocolBufferException exception) {
//                        Log.e(TAG, "failed to get proto.", exception);
//                    }
//
//                }
//        );
////                    processor.getVideoSurfaceOutput().setSurface(srh.getSurface());
                    } catch (InvalidProtocolBufferException exception) {
                        Log.e(TAG, "failed to get proto.", exception);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        /*processor.addPacketCallback(
                "throttled_input_video_cpu",
                (packet) ->{
                    Log.d("Raw Image","Receive image with ts: "+packet.getTimestamp());
                    Bitmap image = AndroidPacketGetter.getBitmapFromRgba(packet);
                }
        );*/
        PermissionHelper.checkAndRequestCameraPermissions(this);


    }

    protected void onStart() {
        super.onStart();
        if (data_output != null) {
            new Thread(new Thread4((byte) 0x03)).start();
        }
    }

    // Used to obtain the content view for this application. If you are extending this class, and
    // have a custom layout, override this method and return the custom layout.
    protected int getContentViewLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        converter = new ExternalTextureConverter(
                        eglManager.getContext(), 2);
        converter.setFlipY(FLIP_FRAMES_VERTICALLY);
        converter.setConsumer(processor);
        if (PermissionHelper.cameraPermissionsGranted(this)) {
            startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        converter.close();

        // Hide preview display until we re-open the camera again.
        previewDisplayView.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void onCameraStarted(SurfaceTexture surfaceTexture) {
        previewFrameTexture = surfaceTexture;
        // Make the display view visible to start showing the preview. This triggers the
        // SurfaceHolder.Callback added to (the holder of) previewDisplayView.
        previewDisplayView.setVisibility(View.VISIBLE);
    }

    protected Size cameraTargetResolution() {
        return null; // No preference and let the camera (helper) decide.
    }

    public void startCamera() {
        cameraHelper = new CameraXPreviewHelper();
        cameraHelper.setOnCameraStartedListener(
                surfaceTexture -> {
                    onCameraStarted(surfaceTexture);
                });
        CameraHelper.CameraFacing cameraFacing = CameraHelper.CameraFacing.BACK;
        cameraHelper.startCamera(
                this, cameraFacing, /*unusedSurfaceTexture=*/ null, cameraTargetResolution());
    }

    protected Size computeViewSize(int width, int height) {
        return new Size(width, height);
    }

    protected void onPreviewDisplaySurfaceChanged(
            SurfaceHolder holder, int format, int width, int height) {
        // (Re-)Compute the ideal size of the camera-preview display (the area that the
        // camera-preview frames get rendered onto, potentially with scaling and rotation)
        // based on the size of the SurfaceView that contains the display.
        Size viewSize = computeViewSize(width, height);
        Size displaySize = cameraHelper.computeDisplaySizeFromViewSize(viewSize);
        boolean isCameraRotated = cameraHelper.isCameraRotated();

        // Connect the converter to the camera-preview frames as its input (via
        // previewFrameTexture), and configure the output width and height as the computed
        // display size.
        converter.setSurfaceTextureAndAttachToGLContext(
                previewFrameTexture,
                isCameraRotated ? displaySize.getHeight() : displaySize.getWidth(),
                isCameraRotated ? displaySize.getWidth() : displaySize.getHeight());
    }

    private void setupPreviewDisplayView() {
        previewDisplayView.setVisibility(View.GONE);
        ViewGroup viewGroup = findViewById(R.id.preview_display_layout);
        viewGroup.addView(previewDisplayView);
        previewDisplayView
                .getHolder()
                .addCallback(
                        new SurfaceHolder.Callback() {
                            @Override
                            public void surfaceCreated(SurfaceHolder holder) {
                                processor.getVideoSurfaceOutput().setSurface(holder.getSurface());
                                Log.d("Surface","Surface Created");
                            }

                            @Override
                            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                                onPreviewDisplaySurfaceChanged(holder, format, width, height);
                                Log.d("Surface","Surface Changed");
                            }

                            @Override
                            public void surfaceDestroyed(SurfaceHolder holder) {
                                processor.getVideoSurfaceOutput().setSurface(null);
                                Log.d("Surface","Surface destroy");
                            }

                        });
    }

    private static Map<String, Map<String, Float>> formatLandmarks(NormalizedLandmarkList poseLandmarks) {
        ArrayList<PoseLandMark> poseMarkers= new ArrayList<>();
        for (NormalizedLandmark landmark : poseLandmarks.getLandmarkList()) {
            PoseLandMark marker = new PoseLandMark(landmark.getX(),landmark.getY(), landmark.getZ(), landmark.getVisibility());
            poseMarkers.add(marker);
        }
        Map<String, Map<String, Float>> model_points = new HashMap<>();
        for (int i = 0; i < poseMarkers.size(); i++) {
            Map<String, Float> point = new HashMap<>();
            point.put("x", poseMarkers.get(i).getX());
            point.put("y", poseMarkers.get(i).getY());
            point.put("z", poseMarkers.get(i).getZ());
            model_points.put(Integer.toString(i), point);
        }
        return model_points;
    }

    static double getAngle(PoseLandMark firstPoint, PoseLandMark midPoint, PoseLandMark lastPoint) {
        double result =
                Math.toDegrees(
                        Math.atan2(lastPoint.getY() - midPoint.getY(),lastPoint.getX() - midPoint.getX())
                                - Math.atan2(firstPoint.getY() - midPoint.getY(),firstPoint.getX() - midPoint.getX()));
        result = Math.abs(result); // Angle should never be negative
        if (result > 180) {
            result = (360.0 - result); // Always get the acute representation of the angle
        }
        return result;
    }

    static class Thread3 implements Runnable {
        private byte[] dataToSend = new byte[66];
        Thread3(byte[] patternData) {
            this.dataToSend[0] = (byte) 0x20;
            this.dataToSend[65] = (byte) 0x30;
            for (int i = 1; i < dataToSend.length - 1; i++) {
                dataToSend[i] = patternData[i-1];
            }
        }
        Thread3() {}
        @Override
        public void run() {
            try {
                if (data_output != null) {
                    data_output.write(dataToSend);
                    data_output.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class Thread4 implements Runnable {
        private byte dataToSend;
        Thread4(byte dataToSend) {
            this.dataToSend = dataToSend;
        }
        @Override
        public void run() {
            try {
                if (data_output != null) {
                    data_output.write(dataToSend);
                    data_output.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}