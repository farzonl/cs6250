package com.tzutalin.dlib;

public final class Constants {
    private static Constants object;

    public Constants() {
    }

    /**
     * getFaceShapeModelPath
     * @return default face shape model path
     */
//    public static String getFaceShapeModelPath() {
//        File sdcard = Environment.getExternalStorageDirectory();
//        String targetPath = sdcard.getAbsolutePath() + File.separator + "shape_predictor_68_face_landmarks.dat";
//        return targetPath;
//    }

    public static String getFaceShapeModelPath() {
        String targetPath = "shape_predictor_68_face_landmarks.dat";
        return "shape_predictor_68_face_landmarks.dat";
    }

//    public static void initConstants(Context ctx) {
//        if (object == null) {
//            object = new Constants();
//        }
//    }

    public static void initConstants() {
        if (object == null) {
            object = new Constants();
        }
    }
}
