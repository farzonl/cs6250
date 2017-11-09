package cs6250.benchmarkingsuite.imageprocessing.effects;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
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
import org.opencv.photo.Photo;

import cs6250.benchmarkingsuite.imageprocessing.staticfiles.Storage;

import static android.R.attr.height;
import static org.opencv.core.Core.addWeighted;
import static org.opencv.core.Core.bitwise_and;
import static org.opencv.imgproc.Imgproc.resize;

public class MaskEffect extends Effect {
    private byte[] byteArray;
    private int width = 100;
    private int height = 100;
    private Mat face;
//    private Mat mask;

    @Override
    public Mat applyTo(Mat frame) {
//        Mat mask = Storage.getMask();
//        mask = null;

//        Mat grayscaleImage = new Mat(frame.cols(), frame.rows(), CvType.CV_8UC4);
//
//        Imgproc.cvtColor(frame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
//
//        MatOfRect faces = new MatOfRect();
//
//        int absoluteFaceSize = (int) (frame.rows() * 0.2);
//
//
//        CascadeClassifier cascadeClassifier = Storage.getCascadeClassifier();
//
//        if (cascadeClassifier != null) {
//            cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2,
//                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
//        }
//
//        // If there are any faces found, draw a rectangle around it
//        Rect[] facesArray = faces.toArray();
//
//        for (int i = 0; i < facesArray.length; i++) {
//            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
//            Imgproc.putText(frame, " Face", facesArray[i].tl(),
//                    Core.FONT_HERSHEY_PLAIN, 1.5, new Scalar(0, 255, 0, 255));
//
//            Point tl = facesArray[i].tl();
//            Point br = facesArray[i].br();
//            Log.e("l", "" + tl.x);
//            Log.e("t", "" + tl.y);
//            Log.e("r", "" + br.x);
//            Log.e("b", "" + br.y);
//
//            width = Math.abs((int) (br.x - tl.x));
//            height = Math.abs((int) (br.y - tl.y));
//
//            resize(mask, mask, new Size(width, height));
//
//            int left = (int) Math.round(facesArray[i].tl().x);
//            int top = (int) Math.round(facesArray[i].tl().y);
//            int right = (int) Math.round(facesArray[i].br().x);
//            int bottom = (int) Math.round(facesArray[i].br().y);
//
//            face = mask.clone();
//
//            frame.rowRange(top, bottom).colRange(left, right).copyTo(face.rowRange(0, width).colRange(0, height));
//
//            Mat res = mask.clone();
//
////            Log.e("face", "width: " + face.width() + "height: " + face.height() + "type: " + face.type());
////            Log.e("mask", "width: " + mask.width() + "height: " + mask.height() + "type: " + mask.type());
//
//            Core.bitwise_and(face, mask, res);
//
//            res.copyTo(frame.rowRange(top, bottom).colRange(left, right));
//        }

        return frame;
    }

    public String toString() {
        return "MaskEffect";
    }
}
