package cs6250.benchmarkingsuite.imageprocessing.effects;

import java.io.Serializable;

import org.opencv.core.Mat;


public abstract class Effect implements Serializable {
	

	public Mat applyTo(Mat matrix) {
		Frame frame = new Frame(matrix);
		frame = applyTo(frame);
		return frame.getMat();
	}

	public Frame applyTo(Frame frame) {
		Mat matrix = frame.getMat();
		matrix = applyTo(matrix);
		return new Frame(matrix);
	}
}
