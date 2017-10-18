package cs6250.benchmarkingsuite.imageprocessing.pipeline;

import org.opencv.core.Mat;

import android.util.Log;

import cs6250.benchmarkingsuite.imageprocessing.effects.Effect;

/**
 * Applies an effect to frames locally, on the device.
 */
public class LocalEffectTask extends EffectTask {

	private static final String TAG = "Android Video Editor";

	public LocalEffectTask(Effect effect) {
		super(effect);
	}

	@Override
	public void run() {
		while (true) {
			Mat frame;
			try {
				frame = inputQueue.take();
			} catch (InterruptedException e) {
				Log.w(TAG, "LocalEffectTask caught InterruptedException while trying to take from queue.");
				break;
			}
			if (frame != null) {
				Log.d("LocalEffectTask", effect.toString() + " - inputQueue size: " + inputQueue.size());
				frame = effect.applyTo(frame);
				if (Thread.interrupted()) {
					break;
				}

				outputQueue.offer(frame);
				Log.d("LocalEffectTask", effect.toString() + " - outputQueue size: " + outputQueue.size());
			}
			if (Thread.interrupted()) {
				break;
			}
		}
	}
}
