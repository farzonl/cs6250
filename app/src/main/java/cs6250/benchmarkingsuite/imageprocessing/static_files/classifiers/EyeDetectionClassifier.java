package cs6250.benchmarkingsuite.imageprocessing.static_files.classifiers;

import android.content.Context;
import android.util.Log;

import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cs6250.benchmarkingsuite.imageprocessing.R;

public class EyeDetectionClassifier {
    private static EyeDetectionClassifier object;
    private static CascadeClassifier cascadeClassifier;

    public static void initEyeDetectionClassifier(Context ctx) {
        if (object == null) {
            object = new EyeDetectionClassifier(ctx);
        }
    }

    public static CascadeClassifier getCascadeClassifier() {
        return cascadeClassifier;
    }

    public EyeDetectionClassifier(Context ctx) {
        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = ctx.getResources().openRawResource(R.raw.haarcascade_eye_tree_eyeglasses);
            File cascadeDir = ctx.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, " haarcascade_eye_tree_eyeglasses.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            // Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }
    }

    public static void initEyeDetectionClassifier() {
        if (object == null) {
            object = new EyeDetectionClassifier();
        }
    }
    public EyeDetectionClassifier() {
        String classifierPath = "../xml/haarcascade_eye_tree_eyeglasses.xml";
        String resource = getClass().getResource(classifierPath).getPath();
        cascadeClassifier = new CascadeClassifier();
        cascadeClassifier.load(resource);
    }
}
