package cs6250.benchmarkingsuite.imageprocessing.static_files;

import android.content.Context;

import cs6250.benchmarkingsuite.imageprocessing.static_files.classifiers.*;

public class Resources {
    public static void initServerResources() {
        CarDetectionClassifier.initCarDetectionClassifier();
        CatDetectionClassifier.initCatDetectionClassifier();
        NoseDetectionClassifier.initNoseDetectionClassifier();
        MouthDetectionClassifier.initMouthDetectionClassifier();
        EyeDetectionClassifier.initEyeDetectionClassifier();
        FaceDetectionClassifier.initFaceDetectionClassifier();
        Constants.initConstants();
//        MaskResources.initMaskResources();
//        FaceSwapResources.initFaceSwapResources();
    }

    public static void initServerResources(Context ctx) {
        FaceDetectionClassifier.initFaceDetectionClassifier(ctx);
        EyeDetectionClassifier.initEyeDetectionClassifier(ctx);
        MouthDetectionClassifier.initMouthDetectionClassifier(ctx);
        NoseDetectionClassifier.initNoseDetectionClassifier(ctx);
        CarDetectionClassifier.initCarDetectionClassifier(ctx);
        CatDetectionClassifier.initCatDetectionClassifier(ctx);
        FaceSwapResources.initFaceSwapResources(ctx);
        MaskResources.initMaskResources(ctx);
        Constants.initConstants(ctx);
    }
}
