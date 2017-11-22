package cs6250.benchmarkingsuite.imageprocessing.metrics;

import android.content.Context;

import java.io.File;

/**
 * Created by farzon on 11/22/17.
 */

public class AndroidDefaults implements  GetPathDefaults {

    private Context context;
    File cacheDir;
    public AndroidDefaults(Context context) {
        this.context = context;
        cacheDir = context.getCacheDir();
    }

    public File getPath()
    {
       return cacheDir;
    }
}
