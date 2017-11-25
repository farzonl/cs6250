package cs6250.benchmarkingsuite.imageprocessing.effects;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import cs6250.benchmarkingsuite.imageprocessing.static_files.classifiers.FaceDetectionClassifier;
import cs6250.benchmarkingsuite.imageprocessing.static_files.classifiers.Storage;

public class SimpleMaskEffect extends Effect {
    private Mat face;

    @Override
    public Mat applyTo(Mat frame) {
        Mat mask = Storage.getMask();

        Mat grayscaleImage = new Mat(frame.cols(), frame.rows(), CvType.CV_8UC4);

        Imgproc.cvtColor(frame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);

        MatOfRect faces = new MatOfRect();

        int absoluteFaceSize = (int) (frame.rows() * 0.2);

        CascadeClassifier cascadeClassifier = FaceDetectionClassifier.getCascadeClassifier();

        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }

        // If there are any faces found, draw a rectangle around it
        Rect[] facesArray = faces.toArray();

        for (int i = 0; i < facesArray.length; i++) {
            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
            Imgproc.putText(frame, " Face", facesArray[i].tl(),
                    Core.FONT_HERSHEY_PLAIN, 1.5, new Scalar(0, 255, 0, 255));

            Point tl = facesArray[i].tl();
            Point br = facesArray[i].br();

            int width = Math.abs((int) (br.x - tl.x));
            int height = Math.abs((int) (br.y - tl.y));

            Size size = new Size(width, height);

            Mat maskResize = new Mat();
            Log.e("mask", "" + mask.width() + " " + mask.height());

            Imgproc.resize(mask, maskResize, size);

            int left = (int) Math.round(facesArray[i].tl().x);
            int top = (int) Math.round(facesArray[i].tl().y);
            int right = (int) Math.round(facesArray[i].br().x);
            int bottom = (int) Math.round(facesArray[i].br().y);

            Mat face = maskResize.clone();

            frame.rowRange(top, bottom).colRange(left, right).copyTo(face.rowRange(0, width).colRange(0, height));

            Mat res = maskResize.clone();

            Core.bitwise_and(face, maskResize, res);

            res.copyTo(frame.rowRange(top, bottom).colRange(left, right));

            res.release();
            maskResize.release();
        }

        grayscaleImage.release();
        faces.release();
        return frame;
    }

    public String toString() {
        return "Simple Mask Effect";
    }
}
