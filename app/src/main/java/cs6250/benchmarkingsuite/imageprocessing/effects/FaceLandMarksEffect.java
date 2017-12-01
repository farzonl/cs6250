package cs6250.benchmarkingsuite.imageprocessing.effects;

import com.tzutalin.dlib.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import cs6250.benchmarkingsuite.imageprocessing.static_files.Constants;

public class FaceLandMarksEffect extends Effect {

    @Override
    public Mat applyTo(Mat frame) {
        FaceDet faceDet = new FaceDet(Constants.getFaceShapeModelPath());

        List<VisionDetRet> results = faceDet.detect(frame);

        org.opencv.core.Point tl = new org.opencv.core.Point();
        org.opencv.core.Point br = new org.opencv.core.Point();
        org.opencv.core.Point center = new org.opencv.core.Point();

        Scalar lineColor = new Scalar(255, 0, 0, 255);
        Scalar textColor = new Scalar(0, 255, 0, 255);
        int lineWidth = 3;

        for (VisionDetRet ret : results) {

            tl = new org.opencv.core.Point(ret.getLeft(), ret.getTop());
            br = new org.opencv.core.Point(ret.getRight(), ret.getBottom());


            Imgproc.rectangle(frame, tl, br, new Scalar(0, 255, 0, 255), 8);
            Imgproc.putText(frame, " Face" , tl,
                    Core.FONT_HERSHEY_PLAIN, 3 , textColor);

            ArrayList<Point> landmarks = ret.getFaceLandmarks();

            for (Point point : landmarks) {
                center = new org.opencv.core.Point(point.x, point.y);
                Imgproc.circle(frame, center, 3, new Scalar(0, 255, 0, 255), 2);

            }
        }

        faceDet.release();
        return frame;
    }

    public String toString() {
        return "FaceLandMarks";
    }
}
