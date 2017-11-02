package cs6250.benchmarkingsuite.imageprocessing.effects;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.InputStream;

import cs6250.benchmarkingsuite.imageprocessing.R;
import cs6250.benchmarkingsuite.imageprocessing.staticfiles.Storage;

import static android.R.attr.height;
import static android.R.attr.width;

public class FaceDetectionEffect extends Effect {

    public Mat applyTo(Mat aInputFrame) {
        Mat grayscaleImage = new Mat(aInputFrame.cols(), aInputFrame.rows(), CvType.CV_8UC4);

        Imgproc.cvtColor(aInputFrame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);

        MatOfRect faces = new MatOfRect();

        int absoluteFaceSize = (int) (aInputFrame.rows() * 0.2);

        CascadeClassifier cascadeClassifier = Storage.getCascadeClassifier();

        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }

        // If there are any faces found, draw a rectangle around it
        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++) {
            Imgproc.rectangle(aInputFrame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
        }

        return aInputFrame;
    }

    public String toString() {
        return "Face Detection";
    }
}
