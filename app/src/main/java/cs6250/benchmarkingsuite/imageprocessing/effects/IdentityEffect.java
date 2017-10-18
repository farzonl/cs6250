package cs6250.benchmarkingsuite.imageprocessing.effects;

import org.opencv.core.Mat;

/**
 * @author farzon
 * the original unedited frame
 *  
 */
public class IdentityEffect extends Effect {

	/**
	 * this function does nothing to the input matrix.
	 * @param A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	@Override
	public Mat applyTo(Mat frame) {
		return frame;
	}

	public String toString() {
		return "Identity";
	}
}
