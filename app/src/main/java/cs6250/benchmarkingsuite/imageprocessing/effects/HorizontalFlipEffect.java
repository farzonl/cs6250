package cs6250.benchmarkingsuite.imageprocessing.effects;

import org.opencv.core.Core;
import org.opencv.core.Mat;
/**
 * @author Farzon Lotfi
 * @version 1.0
 * class that mirrors an image
 *  
 */
public class HorizontalFlipEffect extends Effect {

	/**
	 * the function that does the axis flip to mirror the matrix
	 * @param frame A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	@Override
	public Mat applyTo(Mat frame) {
		Mat newFrame = new Mat();
		Core.flip(frame, newFrame, 1);
		
		return newFrame;
	}

	public String toString() {
		return "Horizontal Flip";
	}
}
