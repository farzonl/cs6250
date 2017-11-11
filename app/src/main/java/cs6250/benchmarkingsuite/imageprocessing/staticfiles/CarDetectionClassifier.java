package cs6250.benchmarkingsuite.imageprocessing.staticfiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cs6250.benchmarkingsuite.imageprocessing.R;

public class CarDetectionClassifier {
    private static CarDetectionClassifier object;
    private static CascadeClassifier cascadeClassifier;

    public static void initStorage(Context ctx) {
        if (object == null) {
            object = new CarDetectionClassifier(ctx);
        }
    }

    public static CascadeClassifier getCascadeClassifier() {
        return cascadeClassifier;
    }

    public CarDetectionClassifier(Context ctx) {
        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = ctx.getResources().openRawResource(R.raw.cars);
            File cascadeDir = ctx.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "cars.xml");
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
}