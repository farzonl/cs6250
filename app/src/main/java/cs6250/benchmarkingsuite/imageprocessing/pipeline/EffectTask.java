package cs6250.benchmarkingsuite.imageprocessing.pipeline;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import org.opencv.core.Mat;

import cs6250.benchmarkingsuite.imageprocessing.effects.Effect;

public abstract class EffectTask implements Runnable {

	protected BlockingQueue<Mat> inputQueue, outputQueue;
	protected Effect effect;


	public EffectTask(Effect effect, BlockingQueue<Mat> inputQueue, BlockingQueue<Mat> outputQueue) {
		this.effect = effect;
		this.inputQueue = inputQueue;
		this.outputQueue = outputQueue;
	}

	public EffectTask(Effect effect) {
		this(effect, null, null);
	}


	public Effect getEffect() {
		return effect;
	}


	public Queue<Mat> getInputQueue() {
		return inputQueue;
	}


	public void setInputQueue(BlockingQueue<Mat> inputQueue) {
		this.inputQueue = inputQueue;
	}


	public Queue<Mat> getOutputQueue() {
		return outputQueue;
	}


	public void setOutputQueue(BlockingQueue<Mat> outputQueue) {
		this.outputQueue = outputQueue;
	}


	@Override
	public String toString() {
		return effect.toString();
	}
}
