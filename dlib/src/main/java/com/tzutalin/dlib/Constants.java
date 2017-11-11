package com.tzutalin.dlib;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public final class Constants {
    private static Constants object;

    public Constants() {
    }

    /**
     * getFaceShapeModelPath
     * @return default face shape model path
     */
    public static String getFaceShapeModelPath() {
        File sdcard = Environment.getExternalStorageDirectory();
        String targetPath = sdcard.getAbsolutePath() + File.separator + "shape_predictor_68_face_landmarks.dat";
        return targetPath;
    }

    public static void initConstants(Context ctx) {
        if (object == null) {
            object = new Constants();
        }
    }
}
