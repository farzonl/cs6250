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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.apache.commons.compress.compressors.CompressorStreamFactory;

import java.util.ArrayList;

import cs6250.benchmarkingsuite.imageprocessing.R;
import cs6250.benchmarkingsuite.imageprocessing.effects.CarDetectionEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.CartoonEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.CatDetectionEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.EyeDetectionEffect;
import cs6250.benchmarkingsuite.imageprocessing.cloud.CloudClientSingelton;
import cs6250.benchmarkingsuite.imageprocessing.effects.Effect;
import cs6250.benchmarkingsuite.imageprocessing.effects.FaceDetectionEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.FaceFeatureDetectionEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.FaceLandMarksEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.FaceSwapEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.GrayscaleEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.MaskEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.NoseDetectionEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.MouthDetectionEffect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Activity where the user is able to edit the pipeline by inserting and removing effects.
 */
public class PipelineEditingActivity extends Activity {

    private final String TAG = "PipelineEditingActivity";

    // Static UI elements
    LinearLayout effectsLinearLayout;
    LinearLayout pipelineLinearLayout;
    RadioGroup compressRadioGroup;
    LinearLayout compressesLinearLayout;
    CheckBox enableCloud;
    EditText ipTextBox;
    EditText portTextBox;

    // Current pipeline
    ArrayList<Effect> effects;
    ArrayList<String> compressions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_pipeline_editing);

        // Gets the views from the layout
        effectsLinearLayout = this.findViewById(R.id.effectsLinearLayout);
        pipelineLinearLayout = this.findViewById(R.id.pipelinesLinearLayout);
        compressRadioGroup = this.findViewById(R.id.compressRadioGroup);
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

		Button buttonCarDetection = new Button(this);
		buttonCarDetection.setText("Car");
		buttonCarDetection.setTag(CarDetectionEffect.class);
		buttonCarDetection.setOnClickListener(new AddEffectListener());
		effectsLinearLayout.addView(buttonCarDetection);

		Button buttonCatDetection = new Button(this);
		buttonCatDetection.setText("Cat");
		buttonCatDetection.setTag(CatDetectionEffect.class);
		buttonCatDetection.setOnClickListener(new AddEffectListener());
		effectsLinearLayout.addView(buttonCatDetection);

		Button buttonNoseDetection = new Button(this);
		buttonNoseDetection.setText("Nose");
		buttonNoseDetection.setTag(NoseDetectionEffect.class);
		buttonNoseDetection.setOnClickListener(new AddEffectListener());
		effectsLinearLayout.addView(buttonNoseDetection);

		Button buttonMouthDetection = new Button(this);
		buttonMouthDetection.setText("Mouth");
		buttonMouthDetection.setTag(MouthDetectionEffect.class);
		buttonMouthDetection.setOnClickListener(new AddEffectListener());
		effectsLinearLayout.addView(buttonMouthDetection);

		Button buttonEyeDetection = new Button(this);
		buttonEyeDetection.setText("Eye");
		buttonEyeDetection.setTag(EyeDetectionEffect.class);
		buttonEyeDetection.setOnClickListener(new AddEffectListener());
		effectsLinearLayout.addView(buttonEyeDetection);

		Button buttonFaceDetection = new Button(this);
		buttonFaceDetection.setText("Face");
		buttonFaceDetection.setTag(FaceDetectionEffect.class);
		buttonFaceDetection.setOnClickListener(new AddEffectListener());
		effectsLinearLayout.addView(buttonFaceDetection);

		Button buttonFaceFeatureDetection = new Button(this);
		buttonFaceFeatureDetection.setText("FaceFeature");
		buttonFaceFeatureDetection.setTag(FaceFeatureDetectionEffect.class);
		buttonFaceFeatureDetection.setOnClickListener(new AddEffectListener());
		effectsLinearLayout.addView(buttonFaceFeatureDetection);

		Button buttonFaceLandMarksDetection = new Button(this);
		buttonFaceLandMarksDetection.setText("FaceLM");
		buttonFaceLandMarksDetection.setTag(FaceLandMarksEffect.class);
		buttonFaceLandMarksDetection.setOnClickListener(new AddEffectListener());
		effectsLinearLayout.addView(buttonFaceLandMarksDetection);

		Button buttonFaceSwapDetection = new Button(this);
		buttonFaceSwapDetection.setText("FaceSwap");
		buttonFaceSwapDetection.setTag(FaceSwapEffect.class);
		buttonFaceSwapDetection.setOnClickListener(new AddEffectListener());
		effectsLinearLayout.addView(buttonFaceSwapDetection);

		Button buttonMask = new Button(this);
		buttonMask.setText("Mask");
		buttonMask.setTag(MaskEffect.class);
		buttonMask.setOnClickListener(new AddEffectListener());
		effectsLinearLayout.addView(buttonMask);

        // Add in new compressions here
        RadioButton buttonSnappyCompression = new RadioButton(this);
        buttonSnappyCompression.setText("Snappy");
        buttonSnappyCompression.setTag(CompressorStreamFactory.SNAPPY_FRAMED);
        compressRadioGroup.addView(buttonSnappyCompression);

        RadioButton buttonGzipCompression = new RadioButton(this);
        buttonGzipCompression.setText("gzip");
        buttonGzipCompression.setTag(CompressorStreamFactory.GZIP);
        compressRadioGroup.addView(buttonGzipCompression);

        RadioButton buttonDeflateCompression = new RadioButton(this);
        buttonDeflateCompression.setText("DEFLATE");
        buttonDeflateCompression.setTag(CompressorStreamFactory.DEFLATE);
        compressRadioGroup.addView(buttonDeflateCompression);

        RadioButton buttonBzip2Compression = new RadioButton(this);
        buttonBzip2Compression.setText("bzip2");
        buttonBzip2Compression.setTag(CompressorStreamFactory.BZIP2);
        compressRadioGroup.addView(buttonBzip2Compression);

        RadioButton buttonPack200Compression = new RadioButton(this);
        buttonPack200Compression.setText("Pack200");
        buttonPack200Compression.setTag(CompressorStreamFactory.PACK200);
        compressRadioGroup.addView(buttonPack200Compression);

        RadioButton buttonLz4Compression = new RadioButton(this);
        buttonLz4Compression.setText("LZ4");
        buttonLz4Compression.setTag(CompressorStreamFactory.LZ4_FRAMED);
        compressRadioGroup.addView(buttonLz4Compression);

        compressRadioGroup.setOnCheckedChangeListener(new AddCompressionListener());

        // Get the effects from the previous processing event.
        Bundle bundle = this.getIntent().getExtras();
        effects = (ArrayList<Effect>) bundle.getSerializable("cs6250.benchmarkingsuite.imageprocessing.core.effects");
        if (effects == null) {
            Log.v(TAG, "effects list is null");
            effects = new ArrayList<>();
        }

        // Get the effects from the previous processing event.
        compressions = (ArrayList<String>) bundle.getSerializable("cs6250.benchmarkingsuite.imageprocessing.core.compressions");
        if (compressions == null) {
            Log.v(TAG, "compressions list is null");
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

        ipTextBox.setText("128.61.10.45");
        portTextBox.setText("20001");

        enableCloud.setChecked(cloudInstance.shouldUseCloud());
        onOffloadChecked(enableCloud);

        // Pipeline Buttons
        for (Effect e : effects) {
            Button newButton = new Button(this);
            newButton.setText(e.toString());
            newButton.setOnClickListener(new RemoveEffectListener());
            pipelineLinearLayout.addView(newButton);
        }
        for (String c : compressions) {
            Button newButton = new Button(this);
            newButton.setText(c);
            newButton.setOnClickListener(new RemoveCompressionListener());
            compressesLinearLayout.addView(newButton);
            compressRadioGroup.check(compressRadioGroup.findViewWithTag(c).getId());
        }
    }

    public void onOffloadChecked(View view) {
        CheckBox cb = (CheckBox) view;
        this.findViewById(R.id.destinationFields)
                .setVisibility(cb.isChecked() ? View.VISIBLE : View.INVISIBLE);
        this.findViewById(R.id.availCompress)
                .setVisibility(cb.isChecked() ? View.VISIBLE : View.INVISIBLE);
        this.findViewById(R.id.appliedCompress)
                .setVisibility(cb.isChecked() ? View.VISIBLE : View.INVISIBLE);
    }

    public void onCancelClicked(View view) {
        this.setResult(RESULT_CANCELED);
        this.finish();
    }

    public void onPipeClearClicked(View view) {
        pipelineLinearLayout.removeAllViews();
        compressesLinearLayout.removeAllViews();
        compressRadioGroup.clearCheck();
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
        Bundle bundle = new Bundle();
        bundle.putSerializable("cs6250.benchmarkingsuite.imageprocessing.core.effects", effects);
        bundle.putSerializable("cs6250.benchmarkingsuite.imageprocessing.core.compressions", compressions);
        result = result.putExtras(bundle);
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
    private class AddCompressionListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            String newCompression = null;
            if (i == -1) {
                // Compress choice cleared
                if (compressesLinearLayout.getChildCount() > 0) {
                    // if there was on selected before, then clear it
                    compressesLinearLayout.getChildAt(0).callOnClick();
                }
                return;
            }

            Button newButton = new Button(radioGroup.getContext());
            RadioButton rb = radioGroup.findViewById(i);
            try {
                newCompression = (String) rb.getTag();
            } catch (Exception e) {
                Log.e(TAG, "AddCompressionListener" + e.toString());
            }

            /* ---- To maintain only one compression ---- */
            compressions.clear();
            compressesLinearLayout.removeAllViews();
            /* ------------------------------------------ */

            compressions.add(newCompression);
            newButton.setText(rb.getText());
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
            compressRadioGroup.clearCheck();
        }
    }
}
