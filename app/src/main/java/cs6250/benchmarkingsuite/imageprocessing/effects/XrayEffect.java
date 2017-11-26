package cs6250.benchmarkingsuite.imageprocessing.effects;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * @author Farzon Lotfi
 * @version 1.0
 * class that generates an xray image when called
 *  
 */
public class XrayEffect extends Effect {

	/**
	 * similar to getting the negative but grayscale reduces color variation 
	 * @param frame -A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	@Override
	public Mat applyTo(Mat frame) {
		Mat newFrame = new Mat(frame.size(), frame.type());
		Imgproc.cvtColor(frame, newFrame, Imgproc.COLOR_RGBA2GRAY);
		Imgproc.cvtColor(newFrame, newFrame, Imgproc.COLOR_GRAY2RGBA);
		Imgproc.cvtColor(frame, newFrame, Imgproc.COLOR_RGBA2RGB);
		Core.bitwise_not(newFrame, newFrame);
		
		return newFrame;
	}

	public String toString() {
		return "X-ray";
	}
}
