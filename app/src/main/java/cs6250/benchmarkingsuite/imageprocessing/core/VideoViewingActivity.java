package cs6250.benchmarkingsuite.imageprocessing.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.opencv.android.JavaCameraView;
import cs6250.benchmarkingsuite.imageprocessing.cloud.CloudClientSingelton;
import java.util.ArrayList;

import cs6250.benchmarkingsuite.imageprocessing.R;
import cs6250.benchmarkingsuite.imageprocessing.effects.Effect;
import cs6250.benchmarkingsuite.imageprocessing.metrics.BandwidthMeasurement;
import cs6250.benchmarkingsuite.imageprocessing.pipeline.LocalEffectTask;

public class VideoViewingActivity extends Activity {

    private static final String TAG = "VideoViewingActivity";
    private static final int EDIT_PIPELINE = 1;
    // The ImageProcessor that catches OpenCV frames and communicates with the pipeline.
    ImageProcessor imageProcessor;
    // A list of effects that is kept in persistence in case the editing activity is canceled.
    ArrayList<Effect> effectList;
    ArrayList<String> compressions;
    // Menu items that can be selected from the menu.
    MenuItem mItemEditPipeline;
    MenuItem mItemClearPipeline;
    // The view that passes frames to the ImageProcessor from the camera and displays the frames from the pipeline.
    private JavaCameraView mView;

    TextView downloadBandwidth, uploadBandwidth, hostCpuUtilization, serverCpuUtilization;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Sets fullscreen and keeps the screen on.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_video_viewing);
        Button button = findViewById(R.id.pipeline);

        downloadBandwidth = (TextView) findViewById(R.id.text_view_idDownloadBandwidth);
        uploadBandwidth = (TextView) findViewById(R.id.text_view_idUploadBandwidth);

        hostCpuUtilization = (TextView) findViewById(R.id.text_view_idHostCpuUtil);
        serverCpuUtilization = (TextView) findViewById(R.id.text_view_idServerCpuUtil);

        downloadBandwidth.setVisibility(View.INVISIBLE);
        uploadBandwidth.setVisibility(View.INVISIBLE);
        hostCpuUtilization.setVisibility(View.INVISIBLE);
        serverCpuUtilization.setVisibility(View.INVISIBLE);

        button.setOnClickListener(new StartPipeline(this));
    }

    public void switchToEffectEditor() {
        Intent editPipelineIntent = new Intent(this, PipelineEditingActivity.class);
        effectList = imageProcessor.getEffects();
        compressions = new ArrayList<>(); //imageProcessor.getCompressions();
        Bundle bundle = new Bundle();
        bundle.putSerializable("cs6250.benchmarkingsuite.imageprocessing.core.effects", effectList);
        bundle.putSerializable("cs6250.benchmarkingsuite.imageprocessing.core.compressions", compressions);
        editPipelineIntent.putExtras(bundle);

        // Start the editing activity.
        this.startActivityForResult(editPipelineIntent, EDIT_PIPELINE);
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();

        //Shuts off the camera view so that it doesn't use unneeded resources.
        mView.disableView();
        mView.setCvCameraViewListener((ImageProcessor) null); // incredibly stupid have to cast before setting null
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        //Restarts the camera view to start retrieving frames.
        mView.setCvCameraViewListener(imageProcessor);
        mView.enableView();
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();

        //Safety checks in case the activity's resources were deallocated.
        if (imageProcessor == null) {
            imageProcessor = new ImageProcessor();
        }

        mView = this.findViewById(R.id.frameView);
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();

        //Problems occur if the same processor is used after the activity stops.
        imageProcessor = null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu");
        mItemEditPipeline = menu.add("Edit Pipeline");
        mItemClearPipeline = menu.add("Clear Pipeline");
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");

        if(CloudClientSingelton.getInstance().shouldUseCloud())
        {
            downloadBandwidth.setVisibility(View.VISIBLE);
            uploadBandwidth.setVisibility(View.VISIBLE);
            hostCpuUtilization.setVisibility(View.VISIBLE);
            serverCpuUtilization.setVisibility(View.VISIBLE);
            /*BandwidthMeasurement measurements =  CloudClientSingelton.getInstance().getBandwidthMeasurement();
            String upBandwidthMsg = "Upload bandwidth: " + String.format("%.2f", measurements.getUploadBandwidth()) + " MBps";
            String dwnBandwidthMsg = "Download bandwidth: " + String.format("%.2f", measurements.getDownloadBandwidth()) + " MBps";
            String cpuUtilizationMsg  = "host cpu util: " + measurements.getHostCpuUtil();
            String serverUtilizationMsg  = "server cpu util: " + measurements.getServerCpuUtil();

            downloadBandwidth.setText(dwnBandwidthMsg);
            uploadBandwidth.setText(upBandwidthMsg);
            hostCpuUtilization.setText(cpuUtilizationMsg);
            serverCpuUtilization.setText(serverUtilizationMsg);*/

        }
        if (requestCode == EDIT_PIPELINE) {
            if (resultCode == RESULT_OK) {
                Log.i(TAG, "RESULT_OK");

                Bundle b = data.getExtras();
                effectList = (ArrayList<Effect>) b.getSerializable("cs6250.benchmarkingsuite.imageprocessing.core.effects");
                compressions = (ArrayList<String>) b.getSerializable("cs6250.benchmarkingsuite.imageprocessing.core.compressions");

                // Safety check
                if (effectList == null) {
                    Log.e(TAG, "Got null effects list");
                    effectList = new ArrayList<>();
                }
                if (compressions == null) {
                    Log.e(TAG, "Got null compression list");
                    compressions = new ArrayList<>();
                }
            }

            // Load the effects in to the new ImageProcessor
            imageProcessor = new ImageProcessor();
            for (Effect effect : effectList) {
                imageProcessor.addEffect(new LocalEffectTask(effect));
            }
            imageProcessor.setCompress(compressions.size() == 1 ? compressions.get(0) : "");
        }
    }
}
