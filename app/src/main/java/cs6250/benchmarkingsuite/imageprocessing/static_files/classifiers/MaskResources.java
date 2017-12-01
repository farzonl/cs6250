package cs6250.benchmarkingsuite.imageprocessing.static_files.classifiers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import cs6250.benchmarkingsuite.imageprocessing.R;

public final class MaskResources {
    private Context ctx;
    private static Mat mask;

    private static MaskResources object;

    public MaskResources(Context ctx) {
        this.ctx = ctx;
        Bitmap bmpMask = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.sunglasses);
        mask = new Mat(bmpMask.getWidth(), bmpMask.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bmpMask, mask);
    }

    public static void initMaskResources(Context ctx) {
        if (object == null) {
            object = new MaskResources(ctx);
        }
    }

    public static void initMaskResources() {
        if (object == null) {
            object = new MaskResources();
        }
    }

    public MaskResources() {
        mask = Imgcodecs.imread("../images/sunglasses.png", Imgcodecs.CV_LOAD_IMAGE_COLOR);
    }

    public static Mat getMask() {
        return mask;
    }
}
