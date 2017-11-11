package cs6250.benchmarkingsuite.imageprocessing.effects;

import android.graphics.Bitmap;
import android.graphics.Point;
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
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class FaceSwapEffect extends Effect {

    @Override
    public Mat applyTo(Mat frame) {
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

            tl = new org.opencv.core.Point(ret.getLeft(), ret.getTop());
            br = new org.opencv.core.Point(ret.getRight(), ret.getBottom());

            Log.e("TL", " "+ ret.getTop() + " " + ret.getLeft());
            Log.e("BR", " " + ret.getBottom() + " "+ ret.getRight());

            Imgproc.rectangle(frame, tl, br, new Scalar(0, 255, 0, 255), 8);
            Imgproc.putText(frame, " Face" , tl,
                    Core.FONT_HERSHEY_PLAIN, 3 , textColor);

            ArrayList<Point> landmarks = ret.getFaceLandmarks();
            ArrayList<org.opencv.core.Point> matOfPoints = new ArrayList<>();

            for (Point point : landmarks) {
                center = new org.opencv.core.Point(point.x, point.y);
                matOfPoints.add(center);
            }
            MatOfPoint matOfPoint = new MatOfPoint();
            matOfPoint.fromList(matOfPoints);

            MatOfInt mOi = new MatOfInt();
            Imgproc.convexHull(matOfPoint, mOi);


            Log.e("Length", " " + landmarks.size());

        }

        faceDet.release();
        System.gc();
        System.runFinalization();

        return frame;
    }

    public String toString() {
        return "FaceSwapEffect";
    }
}
