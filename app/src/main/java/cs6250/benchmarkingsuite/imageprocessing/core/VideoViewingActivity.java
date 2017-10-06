package cs6250.benchmarkingsuite.imageprocessing.core;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.Window;
import org.opencv.android.JavaCameraView;
import android.view.Menu;

import cs6250.benchmarkingsuite.imageprocessing.R;

/**
 * Main Activity where the effects are viewed on screen as being aplied to the camera image.
 */
public class VideoViewingActivity extends Activity {

    private static final String TAG = "VideoViewingActivity";

    //The view that passes frames to the ImageProcessor from the camera and displays the frames from the pipeline.
    private JavaCameraView mView;

    //TODO: add class to catch OpenCV frames and communicates with the pipeline.


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        //Sets fullscreen and keeps the screen on.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_video_viewing);
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();

        //Shuts off the camera view so that it doesn't use unneeded resources.
        mView.disableView();
        //mView.setCvCameraViewListener(null);
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        //TODO: Restarts the camera view to start retrieving frames.
        mView.enableView();
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();

        mView = (JavaCameraView) this.findViewById(R.id.frameView);
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();

        //TODO: clear out pipline (java wrappers for native code means we have to do some memory managemrnt)
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu");

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "Menu Item selected " + item);
        if(item == mItemEditPipeline)
        {
            Intent intentPipeLineEdit = new Intent(this, PipelineEditingActivity.class);
        }
    }
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
    }*/
}
