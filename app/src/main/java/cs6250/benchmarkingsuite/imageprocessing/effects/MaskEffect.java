package cs6250.benchmarkingsuite.imageprocessing.effects;

import android.graphics.Bitmap;
import android.util.Log;

import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.photo.Photo;

import java.util.ArrayList;
import java.util.List;

import cs6250.benchmarkingsuite.imageprocessing.staticfiles.Storage;

import static android.R.attr.height;
import static org.opencv.core.Core.addWeighted;
import static org.opencv.core.Core.bitwise_and;

public class MaskEffect extends Effect {
    private Mat face;

    @Override
    public Mat applyTo(Mat frame) {
        Mat mask = Storage.getMask();

        Bitmap bmp = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(frame, bmp);
        FaceDet faceDet = new FaceDet(Constants.getFaceShapeModelPath());

        List<VisionDetRet> results = faceDet.detect(bmp);
        org.opencv.core.Point tl = new org.opencv.core.Point();
        org.opencv.core.Point br = new org.opencv.core.Point();
        org.opencv.core.Point center = new org.opencv.core.Point();

        Scalar lineColor = new Scalar(255, 0, 0, 255);
        Scalar textColor = new Scalar(0, 255, 0, 255);
        int lineWidth = 3;

        for (VisionDetRet ret : results) {

//            tl = new org.opencv.core.Point(ret.getLeft(), ret.getTop());
//            br = new org.opencv.core.Point(ret.getRight(), ret.getBottom());
//
//            Log.e("TL", " "+ ret.getTop() + " " + ret.getLeft());
//            Log.e("BR", " " + ret.getBottom() + " "+ ret.getRight());

//            Imgproc.rectangle(frame, tl, br, new Scalar(0, 255, 0, 255), 8);
//            Imgproc.putText(frame, " Face" , tl,
//                    Core.FONT_HERSHEY_PLAIN, 3 , textColor);

            ArrayList<android.graphics.Point> landmarks = ret.getFaceLandmarks();
            Log.e("Length", " " + landmarks.size());

            int left = 1000;
            int right = 0;
            int top = 1000;
            int bottom = 0;

            for (android.graphics.Point point : landmarks) {
                left = Math.min(left, point.x);
                right = Math.max(right, point.x);
                top = Math.min(top, point.y);
                bottom = Math.max(bottom, point.y);
            }

            tl = new org.opencv.core.Point(left, top);
            br = new org.opencv.core.Point(right, bottom);
            //Imgproc.rectangle(frame, tl, br, new Scalar(0, 255, 0, 255), 8);

            Mat maskResize = new Mat();
            Log.e("mask", "" + mask.width() + " " + mask.height());

            int width = Math.abs((int) (br.x - tl.x));
            int height = Math.abs((int) (br.y - tl.y));

            Size size = new Size(width, height);
            Imgproc.resize(mask, maskResize, size);

            Mat face = maskResize.clone();

            frame.rowRange(top, bottom).colRange(left, right).copyTo(face.rowRange(0, height).colRange(0, width));

            Mat res = maskResize.clone();

            Log.e("Result", "" + res.width() + " " + res.height());

            Core.bitwise_and(face, maskResize, res);

            res.copyTo(frame.rowRange(top, bottom).colRange(left, right));

            res.release();
            maskResize.release();
            face.release();
        }


        faceDet.release();
        return frame;
    }


    public String toString() {
        return "MaskEffect";
    }
}