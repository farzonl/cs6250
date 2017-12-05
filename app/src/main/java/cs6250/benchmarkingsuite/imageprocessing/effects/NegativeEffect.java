package cs6250.benchmarkingsuite.imageprocessing.effects;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * @author Farzon
 * @version 1.0
 * class that generates an negative image when called
 *  
 */
public class NegativeEffect extends Effect {

	/**
	 * bitwise nots the color channels without alpha
	 * @param frame A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	@Override
	public Mat applyTo(Mat frame) {
		Mat newFrame = new Mat(frame.size(), frame.type());
		Imgproc.cvtColor(frame, newFrame, Imgproc.COLOR_RGBA2RGB);
		Core.bitwise_not(newFrame, newFrame);
		frame.release();
		return newFrame;
	}

	public String toString() {
		return "Negative";
	}
}
