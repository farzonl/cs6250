package cs6250.benchmarkingsuite.imageprocessing.core;

import java.util.ArrayList;

import cs6250.benchmarkingsuite.imageprocessing.R;
import cs6250.benchmarkingsuite.imageprocessing.effects.GrayscaleEffect;
import cs6250.benchmarkingsuite.imageprocessing.effects.Effect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Activity where the user is able to edit the pipeline by inserting and removing effects.
 */
public class PipelineEditingActivity extends Activity implements OnClickListener {
	
	private final String TAG = "PipelineEditingActivity";
	
	//The effects view and the pipeline view.
	LinearLayout effectsLinearLayout;
	LinearLayout pipelineLinearLayout;
	
	//Effect buttons
	Button buttonNone;
	Button buttonGrayScale;
	
	//Static Buttons
	Button buttonClearPipeline;
	Button buttonCancel;
	Button buttonOk;
	
	//Current pipeline
	ArrayList<Effect> effects;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_pipeline_editting);
		
		//Gets the views from the layout
		effectsLinearLayout = this.findViewById(R.id.effectsLinearLayout);
		pipelineLinearLayout = this.findViewById(R.id.pipelinesLinearLayout);
		
		effects = (ArrayList<Effect>) this.getIntent().getSerializableExtra("effects");
		if(effects == null) {
			Log.e(TAG, "effects list is null");
			effects = new ArrayList<Effect>();
		}
		
		setupButtons();
	}

	@Override
	public void onClick(View v) {
		ViewParent vp = v.getParent();

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
