package cs6250.benchmarkingsuite.imageprocessing.staticfiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import cs6250.benchmarkingsuite.imageprocessing.R;

public class FaceSwapResources {
    private Context ctx;
    private static FaceSwapResources object;
    private static Mat mask;
    private static Mat elli;

    public FaceSwapResources(Context ctx) {
        this.ctx = ctx;
        Bitmap bmpMask = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.faceswapmask);
        mask = new Mat(bmpMask.getWidth(), bmpMask.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bmpMask, mask);

        Bitmap bmpElli = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.elli);
        elli = new Mat(bmpMask.getWidth(), bmpMask.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bmpElli, elli);
    }

    public static void initStorage(Context ctx) {
        if (object == null) {
            object = new FaceSwapResources(ctx);
        }
    }

    public static Mat getMask() {
        return mask;
    }

    public static Mat getElli() {
        return elli;
    }
}
