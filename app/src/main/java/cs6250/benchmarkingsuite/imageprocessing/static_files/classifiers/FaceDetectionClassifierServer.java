package cs6250.benchmarkingsuite.imageprocessing.static_files.classifiers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.opencv.objdetect.CascadeClassifier;

public class FaceDetectionClassifierServer {
    private static FaceDetectionClassifier object;
    private static CascadeClassifier cascadeClassifier;

    public static CascadeClassifier getCascadeClassifier() {
        return cascadeClassifier;
    }

    public static void initFaceDetectionClassifier() {
        if (object == null) {
            object = new FaceDetectionClassifier();
        }
    }
    public FaceDetectionClassifierServer() {
        String classifierPath = "../xml/lbpcascade_frontalface.xml";
        String resource = getClass().getResource(classifierPath).getPath();
        cascadeClassifier = new CascadeClassifier();
        cascadeClassifier.load(resource);
    }
}
