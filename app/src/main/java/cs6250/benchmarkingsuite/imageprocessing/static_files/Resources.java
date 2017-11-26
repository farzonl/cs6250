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
        FaceDetectionClassifier.initFaceDetectionClassifier(ctx);
        EyeDetectionClassifier.initEyeDetectionClassifier(ctx);
        MouthDetectionClassifier.initMouthDetectionClassifier(ctx);
        NoseDetectionClassifier.initNoseDetectionClassifier(ctx);
        CarDetectionClassifier.initCarDetectionClassifier(ctx);
        CatDetectionClassifier.initCatDetectionClassifier(ctx);
        FaceSwapResources.initFaceSwapResources(ctx);
        MaskResources.initMaskResources(ctx);
        Constants cs = new Constants();
    }
}
