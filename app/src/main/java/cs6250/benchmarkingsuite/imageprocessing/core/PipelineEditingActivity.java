package cs6250.benchmarkingsuite.imageprocessing.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.opencv.core.Mat;

import java.util.ArrayList;

import cs6250.benchmarkingsuite.imageprocessing.R;
import cs6250.benchmarkingsuite.imageprocessing.cloud.CloudClientSingelton;
import cs6250.benchmarkingsuite.imageprocessing.compressions.Bzip2Compression;
import cs6250.benchmarkingsuite.imageprocessing.compressions.Compression;
import cs6250.benchmarkingsuite.imageprocessing.compressions.DeflateCompression;
import cs6250.benchmarkingsuite.imageprocessing.compressions.GzipCompression;
import cs6250.benchmarkingsuite.imageprocessing.compressions.Lz4Compression;
import cs6250.benchmarkingsuite.imageprocessing.compressions.Pack200Compression;
import cs6250.benchmarkingsuite.imageprocessing.compressions.SnappyCompression;
import cs6250.benchmarkingsuite.imageprocessing.compressions.ZstdCompression;
import cs6250.benchmarkingsuite.imageprocessing.effects.CartoonEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.CheckerBoardDetectionEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.Effect;
import cs6250.benchmarkingsuite.imageprocessing.effects.FaceDetectionEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.GrayscaleEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.MaskEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.MotionDetectionEffect;

/**
 * Activity where the user is able to edit the pipeline by inserting and removing effects.
 */
public class PipelineEditingActivity extends Activity {

    private final String TAG = "PipelineEditingActivity";

    // Static UI elements
    LinearLayout effectsLinearLayout;
    LinearLayout pipelineLinearLayout;
    LinearLayout compressLinearLayout;
    LinearLayout compressesLinearLayout;
    CheckBox enableCloud;
    EditText ipTextBox;
    EditText portTextBox;

    // Current pipeline
    ArrayList<Effect> effects;
    ArrayList<Compression> compressions;

    // Chessboard Detector
    Mat pic;
    private View mPic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_pipeline_editing);

        // Gets the views from the layout
        effectsLinearLayout = this.findViewById(R.id.effectsLinearLayout);
        pipelineLinearLayout = this.findViewById(R.id.pipelinesLinearLayout);
        compressLinearLayout = this.findViewById(R.id.compressLinearLayout);
        compressesLinearLayout = this.findViewById(R.id.compressesLinearLayout);
        ipTextBox = this.findViewById(R.id.ip_address);
        portTextBox = this.findViewById(R.id.PortNumber);
        enableCloud = findViewById(R.id.enableOffloading);

        // Add in New Effects here.
        Button buttonGrayScale = new Button(this);
        buttonGrayScale.setText("Grayscale");
        buttonGrayScale.setTag(GrayscaleEffect.class);
        buttonGrayScale.setOnClickListener(new AddEffectListener());
        effectsLinearLayout.addView(buttonGrayScale);

        Button buttonCartoon = new Button(this);
        buttonCartoon.setText("Cartoon");
        buttonCartoon.setTag(CartoonEffect.class);
        buttonCartoon.setOnClickListener(new AddEffectListener());
        effectsLinearLayout.addView(buttonCartoon);

        Button buttonFaceDetection = new Button(this);
        buttonFaceDetection.setText("Face");
        buttonFaceDetection.setTag(FaceDetectionEffect.class);
        buttonFaceDetection.setOnClickListener(new AddEffectListener());
        effectsLinearLayout.addView(buttonFaceDetection);

        Button buttonMask = new Button(this);
        buttonMask.setText("Mask");
        buttonMask.setTag(MaskEffect.class);
        buttonMask.setOnClickListener(new AddEffectListener());
        effectsLinearLayout.addView(buttonMask);

        Button buttonMotionDetection = new Button(this);
        buttonMotionDetection.setText("Motion");
        buttonMotionDetection.setTag(MotionDetectionEffect.class);
        buttonMotionDetection.setOnClickListener(new AddEffectListener());
        effectsLinearLayout.addView(buttonMotionDetection);

        Button buttonCheckerDetection = new Button(this);
        buttonCheckerDetection.setText("Checker");
        buttonCheckerDetection.setTag(CheckerBoardDetectionEffect.class);
        buttonCheckerDetection.setOnClickListener(new AddEffectListener());
        effectsLinearLayout.addView(buttonCheckerDetection);

        // Add in new compressions here
        Button buttonSnappyCompression = new Button(this);
        buttonSnappyCompression.setText("Snappy");
        buttonSnappyCompression.setTag(SnappyCompression.class);
        buttonSnappyCompression.setOnClickListener(new AddCompressionListener());
        compressLinearLayout.addView(buttonSnappyCompression);

        Button buttonGzipCompression = new Button(this);
        buttonGzipCompression.setText("gzip");
        buttonGzipCompression.setTag(GzipCompression.class);
        buttonGzipCompression.setOnClickListener(new AddCompressionListener());
        compressLinearLayout.addView(buttonGzipCompression);

        Button buttonDeflateCompression = new Button(this);
        buttonDeflateCompression.setText("DEFLATE");
        buttonDeflateCompression.setTag(DeflateCompression.class);
        buttonDeflateCompression.setOnClickListener(new AddCompressionListener());
        compressLinearLayout.addView(buttonDeflateCompression);

        Button buttonBzip2Compression = new Button(this);
        buttonBzip2Compression.setText("bzip2");
        buttonBzip2Compression.setTag(Bzip2Compression.class);
        buttonBzip2Compression.setOnClickListener(new AddCompressionListener());
        compressLinearLayout.addView(buttonBzip2Compression);

        Button buttonPack200Compression = new Button(this);
        buttonPack200Compression.setText("Pack200");
        buttonPack200Compression.setTag(Pack200Compression.class);
        buttonPack200Compression.setOnClickListener(new AddCompressionListener());
        compressLinearLayout.addView(buttonPack200Compression);

        Button buttonLz4Compression = new Button(this);
        buttonLz4Compression.setText("LZ4");
        buttonLz4Compression.setTag(Lz4Compression.class);
        buttonLz4Compression.setOnClickListener(new AddCompressionListener());
        compressLinearLayout.addView(buttonLz4Compression);

        Button buttonZstdCompression = new Button(this);
        buttonZstdCompression.setText("Zstandard");
        buttonZstdCompression.setTag(ZstdCompression.class);
        buttonZstdCompression.setOnClickListener(new AddCompressionListener());
        compressLinearLayout.addView(buttonZstdCompression);

        // Get the effects from the previous processing event.
        effects = (ArrayList<Effect>) this.getIntent().getSerializableExtra("effects");
        if (effects == null) {
            Log.e(TAG, "effects list is null");
            effects = new ArrayList<>();
        }

        // Get the effects from the previous processing event.
        compressions = (ArrayList<Compression>) this.getIntent().getSerializableExtra("compressions");
        if (compressions == null) {
            Log.e(TAG, "compressions list is null");
            compressions = new ArrayList<>();
        }

        // Static Buttons
        CloudClientSingelton cloudInstance = CloudClientSingelton.getInstance();
        String ipAddress = cloudInstance.getIPAddress();
        if (ipAddress != null && !ipAddress.isEmpty()) {
            ipTextBox.setText(ipAddress);
        }

        int portNumber = cloudInstance.getPortNumber();
        if (portNumber > 0) {
            portTextBox.setText(Integer.toString(portNumber));
        }

        //ipTextBox.setText("128.61.2.119", TextView.BufferType.EDITABLE);
        //portTextBox.setText("20001", TextView.BufferType.EDITABLE);

        enableCloud.setChecked(cloudInstance.shouldUseCloud());
        onOffloadChecked(enableCloud);

        // Pipeline Buttons
        for (Effect e : effects) {
            Button newButton = new Button(this);
            newButton.setText(e.toString());
            newButton.setOnClickListener(new RemoveEffectListener());
            pipelineLinearLayout.addView(newButton);
        }
        for (Compression c : compressions) {
            Button newButton = new Button(this);
            newButton.setText(c.toString());
            newButton.setOnClickListener(new RemoveCompressionListener());
            compressLinearLayout.addView(newButton);
        }
    }

    public void onOffloadChecked(View view) {
        CheckBox cb = (CheckBox) view;
        this.findViewById(R.id.destinationFields)
                .setVisibility(cb.isChecked() ? View.VISIBLE : View.INVISIBLE);
    }

    public void onCancelClicked(View view) {
        this.setResult(RESULT_CANCELED);
        this.finish();
    }

    public void onPipeClearClicked(View view) {
        pipelineLinearLayout.removeAllViews();
        compressesLinearLayout.removeAllViews();
        effects.clear();
        compressions.clear();
    }

    public void onOkClicked(View view) {
        if (enableCloud.isChecked()) {
            String ipText = ipTextBox.getText().toString();
            String portText = portTextBox.getText().toString();
            CloudClientSingelton.getInstance(ipText, portText).setUseCloud(true);
        } else {
            CloudClientSingelton.getInstance().setUseCloud(false);
        }
        Intent result = new Intent();
        result.putExtra("result", effects);
        result.putExtra("result", compressions);
        this.setResult(RESULT_OK, result);
        this.finish();
    }

    // Attach to the programmatically added available effects buttons to add effect to the pipeline.
    private class AddEffectListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Effect newEffect = null;
            Button newButton = new Button(v.getContext());
            try {
                newEffect = (Effect) ((Class) v.getTag()).getConstructor().newInstance();
            } catch (Exception e) {
                Log.e(TAG, "AddEffectListener" + e.toString());
            }
            effects.add(newEffect);
            newButton.setText(((Button) v).getText());
            newButton.setOnClickListener(new RemoveEffectListener());
            pipelineLinearLayout.addView(newButton);
        }
    }

    // Attach to the programmatically added available compress buttons to add compression to the pipeline.
    private class AddCompressionListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Compression newCompression = null;
            Button newButton = new Button(v.getContext());
            try {
                newCompression = (Compression) ((Class) v.getTag()).getConstructor().newInstance();
            } catch (Exception e) {
                Log.e(TAG, "AddCompressionListener" + e.toString());
            }
            compressions.add(newCompression);
            newButton.setText(((Button) v).getText());
            newButton.setOnClickListener(new RemoveCompressionListener());
            compressesLinearLayout.addView(newButton);
        }
    }

    // Attach to the programmatically added applied effect buttons to remove effects from the pipeline.
    private class RemoveEffectListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            LinearLayout ll = (LinearLayout) v.getParent();
            int index = ll.indexOfChild(v);
            pipelineLinearLayout.removeViewAt(index);
            effects.remove(index);
        }
    }

    // Attach to the programmatically added applied compression buttons to remove compressions from the pipeline.
    private class RemoveCompressionListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            LinearLayout ll = (LinearLayout) v.getParent();
            int index = ll.indexOfChild(v);
            compressesLinearLayout.removeViewAt(index);
            compressions.remove(index);
        }
    }
}
