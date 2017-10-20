package cs6250.benchmarkingsuite.imageprocessing.pipeline;

//java util
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.HashSet;
import java.util.ArrayList;

//opencv
import org.opencv.core.Mat;

//android
//import android.util.Log;

//cs6250
import cs6250.benchmarkingsuite.imageprocessing.effects.Effect;

public class FrameProcessor implements IPipeline {

	private static final String TAG = "FrameProcessor";

	protected BlockingQueue<Mat> unprocessedFrameQueue, processedFrameQueue;

	protected HashSet<Pipeline> pipelines;

	public FrameProcessor(EffectTask[] effects) {
		unprocessedFrameQueue = new LinkedBlockingQueue<Mat>(2);
		processedFrameQueue = new LinkedBlockingQueue<Mat>(2);
		pipelines = new HashSet<Pipeline>();
		addPipeline(effects);
	}

	public void addFrame(Mat frame) {
		//Log.d("FrameProcessor", "unprocessedFrameQueue size: " + unprocessedFrameQueue.size());
		unprocessedFrameQueue.offer(frame);
	}


	public Mat getFrame() {
		//Log.d("FrameProcessor", "processedFrameQueue size: "
		//		+ processedFrameQueue.size());
		return processedFrameQueue.poll();
	}


	public ArrayList<Effect> getEffects() {
		if (!pipelines.isEmpty()) {
			return pipelines.iterator().next().getEffects();
		} else {
			return new ArrayList<Effect>();
		}
	}


	public void addEffect(EffectTask effect) {
		for (Pipeline pipeline : pipelines) {
			pipeline.addEffect(effect);
		}
	}

	public void clearEffects() {
		for (Pipeline pipeline : pipelines) {
			pipeline.clearEffects();
		}
	}

	public void start() {
		for (Pipeline pipeline : pipelines) {
			pipeline.start();
		}
	}

	public void stop() {
		for (Pipeline pipeline : pipelines) {
			pipeline.stop();
		}
		unprocessedFrameQueue.clear();
		processedFrameQueue.clear();
	}

	private void addPipeline() {
		if (!pipelines.isEmpty()) {
			pipelines.add(new Pipeline(pipelines.iterator().next()));
		}
	}

	private void addPipeline(EffectTask[] effects) {
		pipelines.add(new Pipeline(unprocessedFrameQueue, processedFrameQueue,
				effects));
	}
}
