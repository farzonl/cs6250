package cs6250.benchmarkingsuite.imageprocessing.metrics;

/**
 * Created by farzon on 11/17/17.
 */

public class BenchTimer {
    private long startTime;
    private long stopTime;

    public BenchTimer() {
        startTime = 0;
        stopTime  = 0;
    }

    public void InitTimer() {
        startTime = System.nanoTime();
    }

    public void StopTimer() {
        stopTime = System.nanoTime();
    }

    public long TimeElapsedInMSec() {
        return (stopTime - startTime) / 1000000;
    }
}
