package cs6250.benchmarkingsuite.imageprocessing.effects;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class GrayscaleEffect extends Effect {

	@Override
	public Mat applyTo(Mat frame) {
		Mat newFrame = new Mat();
		Imgproc.cvtColor(frame, newFrame, Imgproc.COLOR_RGBA2GRAY);
		Imgproc.cvtColor(newFrame, newFrame, Imgproc.COLOR_GRAY2RGBA);
		frame.release();
		return newFrame;
	}

	public String toString() {
		return "Grayscale";
	}
}
