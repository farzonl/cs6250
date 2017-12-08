package cs6250.benchmarkingsuite.imageprocessing.effects;

import org.opencv.core.Core;
import org.opencv.core.Mat;


/**
 * @author Farzon Lotfi
 * @version 1.0
 * the vertical flip effect
 *  
 */
public class VerticalFlipEffect extends Effect {

	/**
	 * flips an image across the x axis
	 * @param frame A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	@Override
	public Mat applyTo(Mat frame) {
		Mat newFrame = new Mat();
		Core.flip(frame, newFrame, 0);
		frame.release();
		return newFrame;
	}

	public String toString() {
		return "Vertical Flip";
	}
}
