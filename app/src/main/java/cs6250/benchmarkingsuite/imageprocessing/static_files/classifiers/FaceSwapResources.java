package cs6250.benchmarkingsuite.imageprocessing.static_files.classifiers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import cs6250.benchmarkingsuite.imageprocessing.R;

public class FaceSwapResources {
    private static FaceSwapResources object;
    private static Mat mask;
    private static Mat elli;

    public FaceSwapResources(Context ctx) {
        Bitmap bmpMask = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.faceswapmask);
        mask = new Mat(bmpMask.getWidth(), bmpMask.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bmpMask, mask);

        Bitmap bmpElli = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.elli);
        elli = new Mat(bmpMask.getWidth(), bmpMask.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bmpElli, elli);
    }

    public static void initFaceSwapResources(Context ctx) {
        if (object == null) {
            object = new FaceSwapResources(ctx);
        }
    }

    public static void initFaceSwapResources() {
        if (object == null) {
            object = new FaceSwapResources();
        }
    }

    public FaceSwapResources() {
        mask = Imgcodecs.imread( System.getProperty("user.dir") + "/cs6250/app/src/main/java/cs6250/benchmarkingsuite/imageprocessing/static_files/images/faceswapmask.png", Imgcodecs.CV_LOAD_IMAGE_COLOR);
        elli = Imgcodecs.imread( System.getProperty("user.dir") + "/cs6250/app/src/main/java/cs6250/benchmarkingsuite/imageprocessing/static_files/images/elli.png", Imgcodecs.CV_LOAD_IMAGE_COLOR);
    }

    public static Mat getMask() {
        return mask;
    }

    public static Mat getElli() {
        return elli;
    }
}
