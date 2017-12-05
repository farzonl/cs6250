package cs6250.benchmarkingsuite.imageprocessing.static_files;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public final class Constants {
    private static Constants object;
    private static boolean isClient = true;

    public Constants() {
    }

    /**
     * getFaceShapeModelPath
     * @return default face shape model path
     */
    public static String getFaceShapeModelPath() {
        if (isClient) {
            File sdcard = Environment.getExternalStorageDirectory();
            String targetPath = sdcard.getAbsolutePath() + File.separator + "shape_predictor_68_face_landmarks.dat";
            return targetPath;
        } else {
            return  System.getProperty("user.dir") + "/cs6250/app/src/main/java/cs6250/benchmarkingsuite/imageprocessing/static_files/xml/shape_predictor_68_face_landmarks.dat";
        }
    }

    public static void initConstants(Context ctx) {
        if (object == null) {
            object = new Constants();
            isClient = true;
        }
    }

    public static void initConstants() {
        if (object == null) {
            object = new Constants();
            isClient = false;
        }
    }
}
