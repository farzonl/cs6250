package cs6250.benchmarkingsuite.imageprocessing.pipeline;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.opencv.core.Mat;

//import android.util.Log;

import cs6250.benchmarkingsuite.imageprocessing.effects.Effect;
import cs6250.benchmarkingsuite.imageprocessing.effects.IdentityEffect;

public class Pipeline implements IPipeline {
	private static final String TAG = "Pipeline";

	protected BlockingQueue<Mat> unprocessedFrameQueue, processedFrameQueue;

	protected boolean running;

	protected ArrayList<Thread> effectThreads;
	protected ArrayList <EffectTask> effectTasks;


	public Pipeline(BlockingQueue<Mat> unprocessedFrameQueue, BlockingQueue<Mat> processedFrameQueue, EffectTask[] effects) {
		this.unprocessedFrameQueue = unprocessedFrameQueue;
		this.processedFrameQueue = processedFrameQueue;
		effectThreads = new ArrayList<Thread>();
		effectTasks = new ArrayList<EffectTask>();
		running = false;

		if (effects != null && effects.length >= 1) {
			BlockingQueue<Mat> queue = unprocessedFrameQueue;
			for (int i = 0; i < effects.length - 1; i++) {
				EffectTask effect = effects[i];
				effect.setInputQueue(queue);
				queue = new LinkedBlockingQueue<Mat>(2);
				effect.setOutputQueue(queue);
				effectTasks.add(effect);
			}
			EffectTask effect = effects[effects.length - 1];
			effect.setInputQueue(queue);
			effect.setOutputQueue(processedFrameQueue);
			effectTasks.add(effect);
			createThreads();
		}
	}


	public Pipeline(Pipeline pipeline) {
		this(pipeline.unprocessedFrameQueue, pipeline.processedFrameQueue, pipeline.effectTasks.toArray(new EffectTask[0]));
	}


	public ArrayList<Effect> getEffects() {
		ArrayList<Effect> effects = new ArrayList<Effect>();
		for(EffectTask et : effectTasks) {
			if(!(et.getEffect() instanceof IdentityEffect)) {
				effects.add(et.getEffect());
			}
		}
		return effects;
	}


	public void start() {
		//Log.d(TAG, "start()");
		for (Thread thread : effectThreads) {
			if (thread.getState() == Thread.State.NEW) {
				thread.start();
			}
		}
		running = true;
	}


	public void stop() {
		//Log.d(TAG, "stop()");
		for (Thread thread : effectThreads) {
			thread.interrupt();
		}
		running = false;
	}


	public void addEffect(EffectTask effect) {
		//Log.d(TAG, "addEffect(" + effect + ")");
		effect.setOutputQueue(processedFrameQueue);
		if (!effectTasks.isEmpty()) {
			Thread oldEffectThread = effectThreads.remove(effectThreads.size() - 1);
			oldEffectThread.interrupt();
			EffectTask oldEffectTask = effectTasks.get(effectTasks.size() - 1);
			BlockingQueue<Mat> queue = new LinkedBlockingQueue<Mat>(2);
			oldEffectTask.setOutputQueue(queue);
			effectThreads.add(new Thread(oldEffectTask));
			effect.setInputQueue(queue);
		} else {
			effect.setInputQueue(unprocessedFrameQueue);
		}
		effectTasks.add(effect);
		effectThreads.add(new Thread(effect));
		if (running) {
			start();
		}
	}


	public void addEffect(int index, EffectTask effect) {
		if (index >= effectTasks.size()) {
			addEffect(effect);
		} else if (index == 0) {
			EffectTask nextEffectTask = effectTasks.get(index);
			effect.inputQueue = nextEffectTask.inputQueue;
			BlockingQueue<Mat> queue = new LinkedBlockingQueue<Mat>(2);
			effect.outputQueue = queue;
			nextEffectTask.inputQueue = queue;
			effectTasks.add(index, effect);
			Thread effectThread = new Thread(effect);
			effectThreads.add(index, effectThread);
			Thread nextEffectThread = effectThreads.remove(index + 1);
			nextEffectThread.interrupt();
			nextEffectThread = new Thread(nextEffectTask);
			effectThreads.add(index + 1, nextEffectThread);
		} else {
			EffectTask nextEffectTask = effectTasks.get(index);
			EffectTask previousEffectTask = effectTasks.get(index - 1);
			effect.inputQueue = previousEffectTask.outputQueue;
			BlockingQueue<Mat> queue = new LinkedBlockingQueue<Mat>(2);
			effect.outputQueue = queue;
			nextEffectTask.inputQueue = queue;
			effectTasks.add(index, effect);
			Thread previousEffectThread = effectThreads.remove(index - 1);
			Thread nextEffectThread = effectThreads.remove(index - 1);
			Thread effectThread = new Thread(effect);
			previousEffectThread = new Thread(previousEffectTask);
			nextEffectThread = new Thread(nextEffectTask);
			effectThreads.add(index - 1, previousEffectThread);
			effectThreads.add(index, effectThread);
			effectThreads.add(index + 1, nextEffectThread);
		}
		if (running) {
			start();
		}
	}


	public void removeEffect(int index) {
		if (index < effectTasks.size() && index >= 0) {
			Thread effectThread = effectThreads.remove(index);
			EffectTask effectTask = effectTasks.remove(index);
			effectThread.interrupt();
			if (index < effectTasks.size()) {
				Thread nextEffectThread = effectThreads.remove(index);
				EffectTask nextEffectTask = effectTasks.get(index);
				nextEffectTask.inputQueue = effectTask.inputQueue;
				nextEffectThread.interrupt();
				nextEffectThread = new Thread(nextEffectTask);
				effectThreads.add(index, nextEffectThread);
			} else if (effectTasks.size() == 0) {
				clearEffects();
			} else {
				Thread previousEffectThread = effectThreads.remove(index - 1);
				EffectTask previousEffectTask = effectTasks.get(index - 1);
				previousEffectTask.outputQueue = effectTask.outputQueue;
				previousEffectThread.interrupt();
				previousEffectThread = new Thread(previousEffectTask);
				effectThreads.add(index - 1, previousEffectThread);
			}
			if (running) {
				start();
			}
		}
	}

	public void moveEffect(int startIndex, int endIndex) {
		EffectTask effect = effectTasks.get(startIndex);
		removeEffect(startIndex);
		addEffect(endIndex, effect);
	}

	/**
	 * Stops all threads and removes all effect tasks.
	 * Adds IdentityEffect task to process frames.
	 */
	public void clearEffects() {
		//Log.d(TAG, "clearEffects()");
		effectTasks.clear();

		EffectTask effectTask = new LocalEffectTask(new IdentityEffect());
		effectTask.setInputQueue(unprocessedFrameQueue);
		effectTask.setOutputQueue(processedFrameQueue);

		effectTasks.add(effectTask);
		createThreads();
	}

	private void createThreads() {
		boolean wasRunning = running;
		stop();
		effectThreads.clear();
		for (EffectTask effectTask : effectTasks) {
			effectThreads.add(new Thread(effectTask));
		}
		if (wasRunning) {
			start();
		}
	}
}
