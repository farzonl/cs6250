/**
 * Created by farzon on 9/21/17.
 */

package cs6250.benchmarkingsuite.imageprocessing.core;
import org.opencv.android.OpenCVLoader;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;

import com.tzutalin.dlib.Constants;

import cs6250.benchmarkingsuite.imageprocessing.staticfiles.CarDetectionClassifier;
import cs6250.benchmarkingsuite.imageprocessing.staticfiles.CatDetectionClassifier;
import cs6250.benchmarkingsuite.imageprocessing.staticfiles.EyeDetectionClassifier;
import cs6250.benchmarkingsuite.imageprocessing.staticfiles.FaceDetectionClassifier;
import cs6250.benchmarkingsuite.imageprocessing.staticfiles.FaceSwapResources;
import cs6250.benchmarkingsuite.imageprocessing.staticfiles.NoseDetectionClassifier;
import cs6250.benchmarkingsuite.imageprocessing.staticfiles.MouthDetectionClassifier;
import cs6250.benchmarkingsuite.imageprocessing.staticfiles.Storage;

/**
 * Starting point of the app that makes sure that the user has the OpenCV package installed on their device.
 */
public class OpenCVInitActivity extends Activity {
    private final String TAG = "OpenCVInitActivity";
    static { System.loadLibrary("opencv_java3"); }

    private void checkPermissions()
    {

        // First check android version
        if (Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1) {
//Check if permission is already granted
//thisActivity is your activity. (e.g.: MainActivity.this)
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // Give first an explanation, if needed.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            1);
                }
            }
        }
    }

    /**
     * Starts the VideoViewingActivity if OpenCV is present on the system.
     * If OpenCV is not present, then the user is prompted to install it.
     */
    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if(status == LoaderCallbackInterface.SUCCESS) {
                Log.i(TAG, "OpenCV loaded successfully");

                checkPermissions();

                Intent i = new Intent(mAppContext, VideoViewingActivity.class);
                mAppContext.startActivity(i);
            } else {
                Log.e(TAG, "OpenCV did not load successfully");
                super.onManagerConnected(status);
            }
        }
    };

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Storage str = new Storage(getApplicationContext());

        FaceDetectionClassifier faceDetectionClassifier = new FaceDetectionClassifier(getApplicationContext());
        EyeDetectionClassifier eyeDetectionClassifier = new EyeDetectionClassifier(getApplicationContext());
        MouthDetectionClassifier mouthDetectionClassifier = new MouthDetectionClassifier(getApplicationContext());
        NoseDetectionClassifier noseDetectionClassifier = new NoseDetectionClassifier(getApplicationContext());
        CarDetectionClassifier carDetectionClassifier = new CarDetectionClassifier(getApplicationContext());
        CatDetectionClassifier catDetectionClassifier = new CatDetectionClassifier(getApplicationContext());

        FaceSwapResources faceSwapResources = new FaceSwapResources(getApplicationContext());

        Constants cs = new Constants();
//        MovementDetector.initMovementDetector();

        Log.i(TAG, "Trying to load OpenCV library");
        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, mOpenCVCallBack))
        {
            Log.e(TAG, "Cannot connect to OpenCV Manager");
        }
    }
}