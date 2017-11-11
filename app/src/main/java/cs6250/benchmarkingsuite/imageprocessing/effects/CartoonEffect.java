package cs6250.benchmarkingsuite.imageprocessing.effects;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


public class CartoonEffect extends Effect {

    @Override
    public Mat applyTo(Mat frame) {

        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);

        Mat dstMat = frame.clone();

        Imgproc.bilateralFilter(frame, dstMat, 9, 9, 7);
        Imgproc.cvtColor(dstMat, dstMat, Imgproc.COLOR_RGB2RGBA);


        //Convert to grayscale and apply median blur
        Mat imgGray = new Mat();
        Imgproc.cvtColor(frame, imgGray, Imgproc.COLOR_RGBA2GRAY);

        Mat dstBlur = imgGray.clone();
        Imgproc.medianBlur(imgGray, dstBlur, 7);

        Mat imgEdge = dstBlur.clone();

        // Detect and enhance Edges
        Imgproc.adaptiveThreshold(dstBlur,imgEdge, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY,9,2);

        //Convert back to color and bit-And with color image
        Imgproc.cvtColor(imgEdge, imgEdge, Imgproc.COLOR_GRAY2RGBA);

        Mat res = dstMat.clone();

        Core.bitwise_and(dstMat, imgEdge, res);
        Imgproc.cvtColor(res, res, Imgproc.COLOR_RGB2RGBA);

        frame.release();
        dstBlur.release();
        imgEdge.release();
        imgGray.release();

        System.gc();
        System.runFinalization();
        return res;
    }

    public String toString() {
        return "Cartoon Effect";
    }
}
