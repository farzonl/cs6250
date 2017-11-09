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
    private static Mat chessboard;

    //private static Storage object;
    private static CascadeClassifier cascadeClassifier;

    public Storage(Context ctx) {
        this.ctx = ctx;
        Bitmap bmpMask = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.sunglasses);
        mask = new Mat(bmpMask.getWidth(), bmpMask.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bmpMask, mask);

//        Bitmap bmpChessboard = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.chessboard);
//        chessboard = new Mat(bmpChessboard.getWidth(), bmpChessboard.getHeight(), CvType.CV_8UC4);
//        Utils.bitmapToMat(bmpChessboard, chessboard);

        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = ctx.getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = ctx.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
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
/*
    public static void initStorage(Context ctx) {
        if (object == null) {
            object = new Storage(ctx);
        }
    }

*/
    public static CascadeClassifier getCascadeClassifier() {
        return cascadeClassifier;
    }

    public static Mat getMask() {
        return mask;
    }

    public static Mat getChessboard() {
        return chessboard;
    }
}
