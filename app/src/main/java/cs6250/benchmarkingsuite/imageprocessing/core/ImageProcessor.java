package cs6250.benchmarkingsuite.imageprocessing.core;

import java.util.ArrayList;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.*;
import android.util.Log;

import cs6250.benchmarkingsuite.imageprocessing.effects.*;
import cs6250.benchmarkingsuite.imageprocessing.pipeline.*;


public class ImageProcessor implements CvCameraViewListener {	
	private static final String TAG = "ImageProcessor";
	
	//The underlying pipeline system for the image processor to pass frames to.
	CloudFrameProcessor frameProcessor;
	
	public ImageProcessor() {
		frameProcessor = new CloudFrameProcessor(new EffectTask[]{new LocalEffectTask(new IdentityEffect())});
	}
	
	/**
	 * Gets the list of effects in the underlying pipeline system
	 * @return A list of effects in the underlying pipeline system
	 */
	public ArrayList<Effect> getEffects() {
		return frameProcessor.getEffects();
	}

	public void addEffect(EffectTask effect) {
		if(effect != null) {
			frameProcessor.addEffect(effect);
		}
	}

	public void clearPipeline() {
		frameProcessor.clearEffects();		
	}

	@Override
	public void onCameraViewStarted(int width, int height) {
		Log.d(TAG, "onCameraViewStarted");
		frameProcessor.start();
	}

	@Override
	public void onCameraViewStopped() {
		Log.d(TAG, "onCameraViewStoppped");
		frameProcessor.stop();
	}

	@Override
	public Mat onCameraFrame(Mat inputFrame) {
		//Makes a clone of the inputFrame to pass to the pipeline.
		Mat newFrame = inputFrame.clone();
		frameProcessor.addFrame(newFrame);
		
		//Gets a frame from the pipeline
		newFrame = frameProcessor.getFrame();
		
		//Makes sure that the frame passed back is the size needed by OpenCV to display it.
		if(newFrame != null && newFrame.width() < inputFrame.width()) {
			int diff = inputFrame.width() - newFrame.width();
			Scalar value = new Scalar(0,0,0);
			Mat mat = new Mat();
			Core.copyMakeBorder(newFrame, mat, 0, 0, diff/2, diff - diff/2, Core.BORDER_CONSTANT, value);
			newFrame = mat;
		}
		
		return newFrame;
	}
}
