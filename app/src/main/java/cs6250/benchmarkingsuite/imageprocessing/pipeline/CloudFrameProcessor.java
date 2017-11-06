package cs6250.benchmarkingsuite.imageprocessing.pipeline;

import android.util.Log;

import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.Callback;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.opencv.core.Mat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import cs6250.benchmarkingsuite.imageprocessing.cloud.CloudClientSingelton;
import cs6250.benchmarkingsuite.imageprocessing.effects.CartoonEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.CheckerBoardDetectionEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.FaceDetectionEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.Frame;
import cs6250.benchmarkingsuite.imageprocessing.effects.GrayscaleEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.IdentityEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.MaskEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.MotionDetectionEffect;

/**
 * Created by farzon on 10/14/17.
 */

public class CloudFrameProcessor extends FrameProcessor implements IPipeline {

    private AtomicInteger numFrames = new AtomicInteger(0);
    private UncompressedListener uncompressedListener;
    private CompressedListener compressedListener;
    private ByteArrayOutputStream outputBuffer;
    private String compress;
    private static final int DEFAULT_BUFFER_SIZE = 64 * 1024;
    private CompressorStreamFactory csf;

    public CloudFrameProcessor(EffectTask[] effects) {
        super(effects);
        uncompressedListener = new UncompressedListener();
        compressedListener = new CompressedListener();
        compress = "";
        csf = new CompressorStreamFactory();
    }

    @Override
    public void addFrame(Mat mat) {
        if (CloudClientSingelton.getInstance().shouldUseCloud()) {
            AddFrameToCloud(new Frame(mat));
        } else {
            super.addFrame(mat);
        }
    }

    public String getCompress() {
        return compress;
    }

    public void setCompress(String compress) {
        this.compress = compress;
    }

    private void AddFrameToCloud(Frame frame) {
        numFrames.incrementAndGet();
        ByteBuffer pixels = frame.getAsPngByteBuffer();
        List<ByteBuffer> frameBuffer = new ArrayList<>();
        if (compress != null && !compress.isEmpty()) {
            try {
                frameBuffer.add(compress(pixels, compress));
                CloudClientSingelton.getInstance().cloudClient.addCompressedFrames(frameBuffer, compress, compressedListener);
            } catch (IOException e) {
                Log.e("CloudFrameProcessor", "addCompressedFrames" + e.getMessage(), e);
            }
        } else {
            frameBuffer.add(pixels);
            try {
                CloudClientSingelton.getInstance().cloudClient.addFrames(frameBuffer, uncompressedListener);
            } catch (IOException e) {
                Log.e("CloudFrameProcessor", "addFrames" + e.getMessage(), e);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void clearEffects() {

        super.clearEffects();
        if (CloudClientSingelton.getInstance().shouldUseCloud()) {
            try {
                CloudClientSingelton.getInstance().cloudClient.clearEffects();
            } catch (AvroRemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private ByteBuffer compress(ByteBuffer uncompressedData, String compress) throws IOException {
        ByteArrayOutputStream baos = getOutputBuffer(uncompressedData.capacity());
        OutputStream outputStream = null;
        try {
            if (compress.equals("zstd")) {
                outputStream = new ZstdOutputStream(baos);
            } else {
                outputStream = csf.createCompressorOutputStream(compress, baos);
            }
            outputStream.write(uncompressedData.array());
        } catch (IOException ioe) {
            Log.e("CloudFrameProcessor", "Error compressing", ioe);
        } catch (CompressorException ce) {
            Log.e("CloudFrameProcessor", "Unknown compressor", ce);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
        return ByteBuffer.wrap(baos.toByteArray());
    }

    private ByteBuffer decompress(ByteBuffer compressedData, String compress) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(compressedData.array());
        InputStream inputStream = null;
        ByteBuffer toReturn;
        try {
            if (compress.equals("zstd")) {
                inputStream = new ZstdInputStream(bais);
            } else {
                inputStream = csf.createCompressorInputStream(compress, bais);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

            int readCount = -1;

            while ((readCount = inputStream.read(buffer, compressedData.position(), buffer.length)) > 0) {
                baos.write(buffer, 0, readCount);
            }
            toReturn = ByteBuffer.wrap(baos.toByteArray());
        } catch (IOException ioe) {
            toReturn = null;
            Log.e("CloudFrameProcessor", "Error decompressing", ioe);
        } catch (CompressorException ce) {
            toReturn = null;
            Log.e("CloudFrameProcessor", "Unknown Decompress", ce);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return toReturn;
    }

    private ByteArrayOutputStream getOutputBuffer(int suggestedLength) {
        if (outputBuffer == null) {
            outputBuffer = new ByteArrayOutputStream(suggestedLength);
        }
        outputBuffer.reset();
        return outputBuffer;
    }

    private class UncompressedListener implements Callback<List<ByteBuffer>> {

        @Override
        public void handleResult(List<ByteBuffer> result) {
            Log.d("CloudClient", "Got an uncompressed frame " + result.size());
            for (ByteBuffer buf : result) {
                Mat resultMat = new Frame(buf).getMat();
                processedFrameQueue.offer(resultMat);
                numFrames.decrementAndGet();
            }
        }

        @Override
        public void handleError(Throwable error) {
            Log.e("UncompressedListener", error.getMessage(), error);
        }
    }

    private class CompressedListener implements Callback<List<ByteBuffer>> {

        @Override
        public void handleResult(List<ByteBuffer> result) {
            Log.d("CloudClient", "Got a compressed frame " + result.size());
            try {
                for (ByteBuffer buf : result) {
                    Mat resultMat = new Frame(decompress(buf, compress)).getMat();
                    processedFrameQueue.offer(resultMat);
                    numFrames.decrementAndGet();
                }
            } catch (IOException ioe) {
                Log.e("CompressedListener", "decompression", ioe);
            }
        }

        @Override
        public void handleError(Throwable error) {
            Log.e("CompressedListener", error.getMessage(), error);
        }
    }
}
