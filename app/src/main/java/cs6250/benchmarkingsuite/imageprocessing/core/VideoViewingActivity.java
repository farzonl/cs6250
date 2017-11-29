package cs6250.benchmarkingsuite.imageprocessing.core;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import org.opencv.android.JavaCameraView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cs6250.benchmarkingsuite.imageprocessing.R;
import cs6250.benchmarkingsuite.imageprocessing.cloud.CloudClientSingelton;
import cs6250.benchmarkingsuite.imageprocessing.effects.Effect;
import cs6250.benchmarkingsuite.imageprocessing.metrics.AndroidDefaults;
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

    TextView downloadBandwidth, uploadBandwidth, hostCpuUtilization, serverCpuUtilization, iperfLabel;
    Button iperfLogBtn;
    CheckBox enabledIperf;

    DateFormat date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_viewing);

        Button button = findViewById(R.id.pipeline);

        downloadBandwidth = findViewById(R.id.text_view_idDownloadBandwidth);
        uploadBandwidth = findViewById(R.id.text_view_idUploadBandwidth);

        hostCpuUtilization = findViewById(R.id.text_view_idHostCpuUtil);
        serverCpuUtilization = findViewById(R.id.text_view_idServerCpuUtil);
        iperfLogBtn = findViewById(R.id.iperLog);
        iperfLabel = findViewById(R.id.textView_iperf);
        enabledIperf = findViewById(R.id.enableIperf);

        button.setOnClickListener(new StartPipeline(this));

        date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        enabledIperf.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    BandwidthMeasurement.init(CloudClientSingelton.getInstance().getIPAddress(), new AndroidDefaults(VideoViewingActivity.this));
                    final BandwidthMeasurement measurements = BandwidthMeasurement.getInstance();
                    measurements.setUseIperf(true);
                    AsyncTask.execute(new Runnable() {
                        public void run() {
                            measurements.waitTillIperfComplete();
                            VideoViewingActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    iperfLogBtn.setVisibility(View.VISIBLE);
                                    downloadBandwidth.setVisibility(View.VISIBLE);
                                    uploadBandwidth.setVisibility(View.VISIBLE);
                                    hostCpuUtilization.setVisibility(View.VISIBLE);
                                    serverCpuUtilization.setVisibility(View.VISIBLE);

                                    String upBandwidthMsg = "Up bw: " + String.format("%.2f", measurements.getUploadBandwidth()) + " MBps";
                                    String dwnBandwidthMsg = "Dn bw: " + String.format("%.2f", measurements.getDownloadBandwidth()) + " MBps";
                                    String cpuUtilizationMsg = "hstcpu: " + measurements.getHostCpuUtil();
                                    String serverUtilizationMsg = "servcpu: " + measurements.getServerCpuUtil();

                                    downloadBandwidth.setText(dwnBandwidthMsg);
                                    uploadBandwidth.setText(upBandwidthMsg);
                                    hostCpuUtilization.setText(cpuUtilizationMsg);
                                    serverCpuUtilization.setText(serverUtilizationMsg);
                                    enabledIperf.setChecked(false);
                                }
                            });
                        }
                    });
                } else {
                    BandwidthMeasurement bandwidth = BandwidthMeasurement.getInstance();
                    if (bandwidth != null) {
                        bandwidth.setUseIperf(false);
                    }
                }
            }
        });
    }

    public void switchToEffectEditor() {
        Intent editPipelineIntent = new Intent(this, PipelineEditingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("cs6250.benchmarkingsuite.imageprocessing.core.effects", effectList);
        bundle.putSerializable("cs6250.benchmarkingsuite.imageprocessing.core.compressions", compressions);
        editPipelineIntent.putExtras(bundle);

        // Start the editing activity.
        this.startActivityForResult(editPipelineIntent, EDIT_PIPELINE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Shuts off the camera view so that it doesn't use unneeded resources.
        mView.disableView();

        // incredibly stupid have to cast before setting null
        mView.setCvCameraViewListener((ImageProcessor) null);

        Debug.stopMethodTracing();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Restarts the camera view to start retrieving frames.
        mView.setCvCameraViewListener(imageProcessor);
        mView.enableView();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Safety checks in case the activity's resources were deallocated.
        if (imageProcessor == null) {
            imageProcessor = new ImageProcessor();
        }

        mView = this.findViewById(R.id.frameView);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Problems occur if the same processor is used after the activity stops.
        imageProcessor = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mItemEditPipeline = menu.add("Edit Pipeline");
        mItemClearPipeline = menu.add("Clear Pipeline");
        return true;
    }

    public void onPerfLogClicked(View view) {
        Intent perfLogViewIntent = new Intent(this, IperfLogViewerActivity.class);
        startActivity(perfLogViewIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO: Move to onResume()?
        if (CloudClientSingelton.getInstance().shouldUseCloud()) {
            enabledIperf.setVisibility(View.VISIBLE);
            iperfLabel.setVisibility(View.VISIBLE);
        }
        if (requestCode == EDIT_PIPELINE) {
            if (resultCode == RESULT_OK) {

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
            imageProcessor.clearPipeline();
            for (Effect effect : effectList) {
                imageProcessor.addEffect(new LocalEffectTask(effect));
            }
            imageProcessor.setCompress(compressions.size() == 1 ? compressions.get(0) : "");

            // Begin Profiling new Pipeline
            // To view File: View -> Tool Windows -> Device File Explorer
            // sdcard -> Android -> data -> imageprocessing -> files
            // bufferSize: "If not given, it defaults to 8MB." -> "not given" = 0?
            Debug.startMethodTracingSampling(date.format(new Date()), 0, 50);
        }
    }
}
