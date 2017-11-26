package cs6250.benchmarkingsuite.imageprocessing.static_files;

import android.content.Context;

import com.tzutalin.dlib.Constants;

import cs6250.benchmarkingsuite.imageprocessing.static_files.classifiers.*;

/**
 * Created by thang911 on 11/26/17.
 */

public class Resources {
    public static void initServerResources() {
        CarDetectionClassifier.initCarDetectionClassifier();
        CatDetectionClassifier.initCatDetectionClassifier();
        NoseDetectionClassifier.initNoseDetectionClassifier();
        MouthDetectionClassifier.initMouthDetectionClassifier();
        EyeDetectionClassifier.initEyeDetectionClassifier();
        FaceDetectionClassifier.initFaceDetectionClassifier();
        MaskResources.initMaskResources();
        FaceSwapResources.initFaceSwapResources();
    }

    public static void initServerResources(Context ctx) {
        FaceDetectionClassifier.initFaceDetectionClassifier(ctx.getApplicationContext());
        EyeDetectionClassifier.initEyeDetectionClassifier(ctx.getApplicationContext());
        MouthDetectionClassifier.initMouthDetectionClassifier(ctx.getApplicationContext());
        NoseDetectionClassifier.initNoseDetectionClassifier(ctx.getApplicationContext());
        CarDetectionClassifier.initCarDetectionClassifier(ctx.getApplicationContext());
        CatDetectionClassifier.initCatDetectionClassifier(ctx.getApplicationContext());
        FaceSwapResources.initFaceSwapResources(ctx.getApplicationContext());
        MaskResources.initMaskResources(ctx.getApplicationContext());
        Constants cs = new Constants();
    }
}
