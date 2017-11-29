package cs6250.benchmarkingsuite.imageprocessing.core;


import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import java.io.IOException;
import android.widget.TextView;

import cs6250.benchmarkingsuite.imageprocessing.Util.FileUtils;
import cs6250.benchmarkingsuite.imageprocessing.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class IperfLogViewerActivity extends Activity {

    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iperf_log_viewer);
        handler.post(new Runnable() {
            public void run() {
                TextView tv = findViewById(R.id.fullscreen_content);
                try {
                    String result = FileUtils.readFileToString(IperfLogViewerActivity.this.getCacheDir() + "/output.txt");
                    tv.setText(result);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
