package cs6250.benchmarkingsuite.imageprocessing.static_files.classifiers;

import android.content.Context;
import android.util.Log;

import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cs6250.benchmarkingsuite.imageprocessing.R;

public class MouthDetectionClassifier {
    private static MouthDetectionClassifier object;
    private static CascadeClassifier cascadeClassifier;

    public static void initStorage(Context ctx) {
        if (object == null) {
            object = new MouthDetectionClassifier(ctx);
        }
    }

    public static CascadeClassifier getCascadeClassifier() {
        return cascadeClassifier;
    }

    public MouthDetectionClassifier(Context ctx) {
        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = ctx.getResources().openRawResource(R.raw.haarcascade_mcs_mouth);
            File cascadeDir = ctx.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "haarcascade_mcs_mouth.xml");
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

    public static void initStorage() {
        if (object == null) {
            object = new MouthDetectionClassifier();
        }
    }
    public MouthDetectionClassifier() {
        String classifierPath = "../xml_files/haarcascade_mcs_mouth.xml";
        String resource = getClass().getResource(classifierPath).getPath();
        cascadeClassifier = new CascadeClassifier();
        cascadeClassifier.load(resource);
    }
}
