package cs6250.benchmarkingsuite.imageprocessing.pipeline;


import java.io.IOException;
import java.nio.ByteBuffer;

import java.util.ArrayList;
import java.util.List;


import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.Callback;
import org.opencv.core.Mat;
import java.util.concurrent.atomic.AtomicInteger;

import android.util.Log;

import cs6250.benchmarkingsuite.imageprocessing.cloud.CloudClientSingelton;
import cs6250.benchmarkingsuite.imageprocessing.effects.*;

/**
 * Created by farzon on 10/14/17.
 */

public class CloudFrameProcessor extends FrameProcessor implements IPipeline, Callback<List<ByteBuffer>> {

    private AtomicInteger numFrames = new AtomicInteger(0);

    public CloudFrameProcessor(EffectTask[] effects) {
        super(effects);
    }

    @Override
    public void addFrame(Mat mat) {

        if (CloudClientSingelton.getInstance().shouldUseCloud()) {
            AddFrameToCloud(new Frame(mat));
        }
        else
        {
            super.addFrame(mat);
        }
    }

    public void AddFrameToCloud(Frame frame)
    {
        numFrames.incrementAndGet();
        List<ByteBuffer> frameBuffer = new ArrayList<ByteBuffer>();
        ByteBuffer pixels = frame.getAsPngByteBuffer();
        // perform the computation on the server
        frameBuffer.add(pixels);
        if (frameBuffer.size() > 0) {
            try {
                CloudClientSingelton.getInstance().cloudClient.addFrames(frameBuffer, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }

    @Override
    public void addEffect(EffectTask effect) {
        super.addEffect(effect);

        if (CloudClientSingelton.getInstance().shouldUseCloud()) {
            try {
                if (effect.getEffect() instanceof GrayscaleEffect) {
                    CloudClientSingelton.getInstance().cloudClient.addGrayscaleEffect();
                } else if (effect.getEffect() instanceof FaceDetectionEffect) {
                    CloudClientSingelton.getInstance().cloudClient.addFaceDetectionEffect();
                } else if (effect.getEffect() instanceof IdentityEffect) {
                    CloudClientSingelton.getInstance().cloudClient.addIdentityEffect();
                } else if (effect.getEffect() instanceof CartoonEffect) {
                    CloudClientSingelton.getInstance().cloudClient.addCartoonEffect();
                } else if (effect.getEffect() instanceof MaskEffect) {
                    CloudClientSingelton.getInstance().cloudClient.addMaskEffect();
                } else if (effect.getEffect() instanceof MotionDetectionEffect) {
                    CloudClientSingelton.getInstance().cloudClient.addMotionDetectionEffect();
                } else if (effect.getEffect() instanceof CheckerBoardDetectionEffect) {
                    CloudClientSingelton.getInstance().cloudClient.addCheckerBoardDetectionEffect();
                }

                /*else if (effect.getEffect() instanceof IdentityEffect) {
					cloudClient.addIdentityEffect();*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void clearEffects() {

        super.clearEffects();
        if(CloudClientSingelton.getInstance().shouldUseCloud())
        {
            try {
                CloudClientSingelton.getInstance().cloudClient.clearEffects();
            } catch (AvroRemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleError(Throwable error) {
        error.printStackTrace();
    }

    @Override
    public void handleResult(List<ByteBuffer> result) {

        Log.e("CloudClient", "Got a result frame " + result.size());
        for (ByteBuffer buf: result) {
            Mat resultMat = new Frame(buf).getMat();
            processedFrameQueue.offer(resultMat);
            numFrames.decrementAndGet();
        }
    }
}
