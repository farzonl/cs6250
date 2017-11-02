package cs6250.benchmarkingsuite.imageprocessing.effects;

import org.opencv.core.Mat;

public class MotionDetectionEffect extends Effect {

    @Override
    public Mat applyTo(Mat frame) {
        MotionDetector motionDetector = new MotionDetector(240);
        return motionDetector.detect(frame);
    }

    public String toString() {
        return "Motion Detection Effect";
    }
}