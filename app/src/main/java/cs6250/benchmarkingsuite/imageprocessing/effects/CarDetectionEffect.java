package cs6250.benchmarkingsuite.imageprocessing.effects;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import cs6250.benchmarkingsuite.imageprocessing.static_files.classifiers.CarDetectionClassifier;

public class CarDetectionEffect extends  Effect {
    @Override
    public Mat applyTo(Mat frame) {
        Mat grayscaleImage = new Mat(frame.cols(), frame.rows(), CvType.CV_8UC4);

        Imgproc.cvtColor(frame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);

        MatOfRect faces = new MatOfRect();

        int absoluteFaceSize = (int) (frame.rows() * 0.2);

        CascadeClassifier cascadeClassifier = CarDetectionClassifier.getCascadeClassifier();

        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }

        // If there are any faces found, draw a rectangle around it
        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++) {
            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
        }

        faces.release();
        grayscaleImage.release();

        return frame;
    }

    public String toString() {
        return "Car Detection";
    }

}