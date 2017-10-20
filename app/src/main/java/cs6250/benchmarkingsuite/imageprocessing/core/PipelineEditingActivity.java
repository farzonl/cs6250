package cs6250.benchmarkingsuite.imageprocessing.core;

import java.util.ArrayList;

import cs6250.benchmarkingsuite.imageprocessing.R;
import cs6250.benchmarkingsuite.imageprocessing.effects.GrayscaleEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.Effect;
import cs6250.benchmarkingsuite.imageprocessing.cloud.CloudClientSingelton;

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

/**
 * Activity where the user is able to edit the pipeline by inserting and removing effects.
 */
public class PipelineEditingActivity extends Activity implements OnClickListener {
	
	private final String TAG = "PipelineEditingActivity";
	
	//The effects view and the pipeline view.
	LinearLayout effectsLinearLayout;
	LinearLayout pipelineLinearLayout;
	LinearLayout cloudEnableLayout;

	//Effect buttons
	Button buttonNone;
	Button buttonGrayScale;
	
	//Static Buttons
	Button buttonClearPipeline;
	Button buttonCancel;
	Button buttonOk;
	RadioButton  CloudOnRBtn;
	RadioButton  CloudOffRBtn;

	EditText ipTextBox;
	EditText portTextBox;

	//Current pipeline
	ArrayList<Effect> effects;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_pipeline_editting);
		//Gets the views from the layout
		effectsLinearLayout = this.findViewById(R.id.effectsLinearLayout);
		pipelineLinearLayout = this.findViewById(R.id.pipelinesLinearLayout);
		cloudEnableLayout = this.findViewById(R.id.cloudEnableLayout);
		effects = (ArrayList<Effect>) this.getIntent().getSerializableExtra("effects");
		ipTextBox = this.findViewById(R.id.ip_address);
		portTextBox = this.findViewById(R.id.PortNumber);
		if(effects == null) {
			Log.e(TAG, "effects list is null");
			effects = new ArrayList<Effect>();
		}
		
		setupButtons();
	}

	@Override
	public void onClick(View v) {
		ViewParent vp = v.getParent();

		if(vp.equals(cloudEnableLayout))
		{
			if( v == CloudOnRBtn)
			{
				if(ipTextBox.length() > 7 || portTextBox.length() == 0)
				{
					CloudOffRBtn.setChecked(true);
				}
				else
				{
					String sIpText = ipTextBox.getText().toString();
					String sPortText = portTextBox.getText().toString();
					CloudClientSingelton.getInstance(sIpText, sPortText);
				}
			}
		}

		if(vp.equals(effectsLinearLayout)) {
			Button newButton = new Button(this);
			Effect newEffect = null;
			
			//Checks which effect button was pressed to add it to the pipeline
			if (v == buttonGrayScale) {
				newEffect = new GrayscaleEffect();
			}
			
			if(newEffect != null) {
				effects.add(newEffect);
				
				newButton.setText(newEffect.toString());
				newButton.setOnClickListener(this);
				pipelineLinearLayout.addView(newButton);
			}
		} else if(vp.equals(pipelineLinearLayout)) {
			int index = pipelineLinearLayout.indexOfChild(v);
			pipelineLinearLayout.removeViewAt(index);
			effects.remove(index);
		} else {
			if(v == buttonClearPipeline) {
				pipelineLinearLayout.removeAllViews();
				effects.clear();
			} else if(v == buttonCancel) {
				this.setResult(RESULT_CANCELED);
				this.finish();
			} else if(v == buttonOk) {
				if(CloudOnRBtn.isChecked())
				{
					String sIpText = ipTextBox.getText().toString();
					String sPortText = portTextBox.getText().toString();
					CloudClientSingelton.getInstance(sIpText, sPortText).setUseCloud(true);
				}
				if(CloudOffRBtn.isChecked())
				{
					CloudClientSingelton.getInstance().setUseCloud(false);
				}
				Intent result = new Intent();
				result.putExtra("result", effects);
				this.setResult(RESULT_OK, result);
				this.finish();
			}
		}
	}
	
	/**
	 * Sets up the view of the effects that can be added to the pipeline.
	 */
	private void setupButtons() {
		//Effect Buttons
		buttonGrayScale = new Button(this);
		buttonGrayScale.setText("Grayscale");
		buttonGrayScale.setOnClickListener(this);
		effectsLinearLayout.addView(buttonGrayScale);
		
		//Static Buttons
		CloudClientSingelton cloudInstance = CloudClientSingelton.getInstance();
		String ipAddress = cloudInstance.getIPAddress();
		if(ipAddress != null && !ipAddress.isEmpty())
		{
			ipTextBox.setText(ipAddress);
		}

		int portNumber = cloudInstance.getPortNumber();
		if(portNumber > 0)
		{
			portTextBox.setText("" + portNumber);
		}

		CloudOnRBtn = (RadioButton) findViewById(R.id.CloudOnRadBtn);
		CloudOffRBtn = (RadioButton) findViewById(R.id.CloudOffRadBtn);

		if (cloudInstance.shouldUseCloud()) {CloudOffRBtn.setChecked(true);} else { CloudOffRBtn.setChecked(true); };
		CloudOnRBtn.setOnClickListener(this);


		buttonClearPipeline = this.findViewById(R.id.button_edit_pipeline_clear);
		buttonClearPipeline.setOnClickListener(this);
		
		buttonCancel = this.findViewById(R.id.button_edit_pipeline_cancel);
		buttonCancel.setOnClickListener(this);
		
		buttonOk = this.findViewById(R.id.button_edit_pipeline_ok);
		buttonOk.setOnClickListener(this);
		
		//Pipeline Buttons
		for(Effect e : effects) {
			Button newButton = new Button(this);
			newButton.setText(e.toString());
			newButton.setOnClickListener(this);
			pipelineLinearLayout.addView(newButton);
		}
	}
}
