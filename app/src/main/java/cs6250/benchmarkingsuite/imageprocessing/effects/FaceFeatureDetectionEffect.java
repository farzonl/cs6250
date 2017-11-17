package cs6250.benchmarkingsuite.imageprocessing.effects;

import org.opencv.core.Mat;

/**
 * Created by thang911 on 11/17/17.
 */

public class FaceFeatureDetectionEffect extends Effect {
    @Override
    public Mat applyTo(Mat frame) {
        return frame;
    }

    public String toString() {
        return "Face Feature Detection";
    }

}
