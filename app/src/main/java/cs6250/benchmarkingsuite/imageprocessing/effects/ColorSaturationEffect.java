package cs6250.benchmarkingsuite.imageprocessing.effects;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
/**
 * @author Farzon Lotfi
 * @version 1.0
 * class that implements color saturation effect
 */
public class ColorSaturationEffect extends Effect{

	/**
	 * the function that applies color saturation
	 * @param frame A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	@Override
	public Mat applyTo(Mat frame) {
		Mat newFrame = new Mat();
		Imgproc.cvtColor(frame, newFrame, Imgproc.COLOR_RGBA2RGB);
		Imgproc.cvtColor(newFrame, newFrame, Imgproc.COLOR_RGB2HSV);
		
		byte[] buff = new byte[(int) (newFrame.total()*newFrame.channels())];
		newFrame.get(0, 0, buff);
		
		for(int i = 0; i < newFrame.total()*newFrame.channels(); i += 3) {
			buff[i] = 90;
		}
		
		newFrame.put(0, 0, buff);
		Imgproc.cvtColor(newFrame, newFrame, Imgproc.COLOR_HSV2RGB);
		Imgproc.cvtColor(frame, newFrame, Imgproc.COLOR_RGB2RGBA);
		frame.release();
		return newFrame;
	}

	public String toString() {
		return "Color Saturation";
	}
}
