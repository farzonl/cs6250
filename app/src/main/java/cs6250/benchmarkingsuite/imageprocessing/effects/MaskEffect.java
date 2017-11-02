package cs6250.benchmarkingsuite.imageprocessing.effects;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import cs6250.benchmarkingsuite.imageprocessing.staticfiles.Storage;

import static android.R.attr.height;
import static org.opencv.core.Core.addWeighted;
import static org.opencv.imgproc.Imgproc.resize;

public class MaskEffect extends Effect {
    private byte[] byteArray;

    @Override
    public Mat applyTo(Mat frame) {

        Bitmap bmp = Storage.getbmp();
        Mat newFrame = new Mat(bmp.getWidth(), bmp.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bmp, newFrame);
        //Imgproc.cvtColor(newFrame, newFrame, Imgproc.COLOR_RGBA2RGB);

        Size size = new Size(frame.cols() * 0.2, frame.rows() * 0.2);
        resize(newFrame, newFrame, size);

        Mat grayscaleImage = new Mat(frame.cols(), frame.rows(), CvType.CV_8UC4);

        Imgproc.cvtColor(frame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);

        MatOfRect faces = new MatOfRect();

        int absoluteFaceSize = (int) (height * 0.2);

        CascadeClassifier cascadeClassifier = Storage.getCascadeClassifier();

        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }

        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++) {
            int x = (int) Math.round(facesArray[i].tl().x);
            int y = (int) Math.round(facesArray[i].tl().y);
            newFrame.copyTo(frame.rowRange(x, newFrame.rows() + x).colRange(y, newFrame.cols() + y));

            //Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
        }

        return frame;
    }

    public String toString() {
        return "MaskEffect";
    }
}
