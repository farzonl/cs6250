package cs6250.benchmarkingsuite.imageprocessing.staticfiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cs6250.benchmarkingsuite.imageprocessing.R;

public final class Storage {
    private Context ctx;
    private static Mat mask;

    private static Storage object;

    public Storage(Context ctx) {
        this.ctx = ctx;
        Bitmap bmpMask = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.sunglasses);
        mask = new Mat(bmpMask.getWidth(), bmpMask.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bmpMask, mask);
    }

    public static void initStorage(Context ctx) {
        if (object == null) {
            object = new Storage(ctx);
        }
    }

    public static Mat getMask() {
        return mask;
    }
}
