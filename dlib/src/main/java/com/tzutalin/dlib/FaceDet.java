package com.tzutalin.dlib;

import org.opencv.core.Mat;

import java.util.Arrays;
import java.util.List;

/**
 * Created by houzhi on 16-10-20.
 * Modified by tzutalin on 16-11-15
 */
public class FaceDet {
    private static final String TAG = "dlib";

    // accessed by native methods
    @SuppressWarnings("unused")
    private long mNativeFaceDetContext;
    private String mLandMarkPath = "";

    static {
        try {
            System.loadLibrary("android_dlib");
            jniNativeClassInit();
        } catch (UnsatisfiedLinkError e) {
        }
    }

    @SuppressWarnings("unused")
    public FaceDet() {
        jniInit(mLandMarkPath);
    }

    public FaceDet(String landMarkPath) {
        mLandMarkPath = landMarkPath;
        jniInit(mLandMarkPath);
    }


    public List<VisionDetRet> detect(String path) {
        VisionDetRet[] detRets = jniDetect(path);
        return Arrays.asList(detRets);
    }

    public List<VisionDetRet> detect(Mat mat) {
        VisionDetRet[] detRets = jniBitmapDetect(mat.getNativeObjAddr());
        return Arrays.asList(detRets);
    }



    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public void release() {
        jniDeInit();
    }

    private native static void jniNativeClassInit();

    private synchronized native int jniInit(String landmarkModelPath);

    private synchronized native int jniDeInit();

    private synchronized native VisionDetRet[] jniBitmapDetect(long matAddr);

    private synchronized native VisionDetRet[] jniDetect(String path);
}