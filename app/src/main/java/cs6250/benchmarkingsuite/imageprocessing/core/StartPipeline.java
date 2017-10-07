package cs6250.benchmarkingsuite.imageprocessing.core;

/**
 * Created by farzon on 10/6/17.
 */

import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;

public class StartPipeline implements OnClickListener {

    VideoViewingActivity m_activity;
    public StartPipeline(VideoViewingActivity activity)
    {
        m_activity = activity;
    }

    public void onClick(View v)
    {
        m_activity.switchToEffectEditor();
    }
}
