package cs6250.benchmarkingsuite.imageprocessing.effects;

import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.Point;
import com.tzutalin.dlib.VisionDetRet;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import cs6250.benchmarkingsuite.imageprocessing.static_files.Constants;
import cs6250.benchmarkingsuite.imageprocessing.static_files.classifiers.FaceSwapResources;

public class FaceSwapEffect extends Effect{
    private Mat face;

    @Override
    public Mat applyTo(Mat frame) {
        Mat mask = FaceSwapResources.getMask();
        Mat elli = FaceSwapResources.getElli();

        FaceDet faceDet = new FaceDet(Constants.getFaceShapeModelPath());

        List<VisionDetRet> results = faceDet.detect(frame);
        org.opencv.core.Point tl = new org.opencv.core.Point();
        org.opencv.core.Point br = new org.opencv.core.Point();
        org.opencv.core.Point center = new org.opencv.core.Point();

        Scalar lineColor = new Scalar(255, 0, 0, 255);
        Scalar textColor = new Scalar(0, 255, 0, 255);
        int lineWidth = 3;

        for (VisionDetRet ret : results) {

            ArrayList<Point> landmarks = ret.getFaceLandmarks();

            int left = 1000;
            int right = 0;
            int top = 1000;
            int bottom = 0;

            for (Point point : landmarks) {
                left = Math.min(left, point.x);
                right = Math.max(right, point.x);
                top = Math.min(top, point.y);
                bottom = Math.max(bottom, point.y);
            }

            tl = new org.opencv.core.Point(left, top);
            br = new org.opencv.core.Point(right, bottom);
            //Imgproc.rectangle(frame, tl, br, new Scalar(0, 255, 0, 255), 8);

            Mat maskResize = new Mat();

            int width = Math.abs((int) (br.x - tl.x));
            int height = Math.abs((int) (br.y - tl.y));

            //Resize mask
            Size size = new Size(width, height);
            Imgproc.resize(mask, maskResize, size);

            Mat face = maskResize.clone();

            //Copy prof face to the matrix
            frame.rowRange(top, bottom).colRange(left, right).copyTo(face.rowRange(0, height).colRange(0, width));

            //bitwise_and prof's face and mask
            Mat imgInv = maskResize.clone();
            Core.bitwise_and(face, maskResize, imgInv);

            //Resize Elli's Img
            Mat elliResize = new Mat();
            Imgproc.resize(elli, elliResize, size);

            //bitwise_not resizemask
            Mat maskResizeInvert = new Mat();
            Core.bitwise_not(maskResize, maskResizeInvert);

            //bitwise_and maskResizeInvert and elli
            Mat elliInvert = new Mat();
            Core.bitwise_and(maskResizeInvert, elliResize, elliInvert);

            //bitwise_or imgInv and elliInv
            Mat res = new Mat();
            Core.bitwise_or(imgInv, elliInvert, res);

            //Copy to the original image
            res.copyTo(frame.rowRange(top, bottom).colRange(left, right));

            res.release();
            elliInvert.release();
            elliResize.release();
            maskResize.release();
            face.release();
        }


        faceDet.release();
        return frame;
    }


    public String toString() {
        return "Face Swap";
    }
}
