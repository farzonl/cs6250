package cs6250.benchmarkingsuite.imageprocessing.cloud;

/**
 * Created by farzon on 10/14/17.
 */

public final class CloudClientSingelton {
    private static volatile CloudClientSingelton instance = null;
    public CloudClient cloudClient;
    private boolean bUseCloud = false;
    private Thread td;

    public boolean shouldUseCloud()
    {
        return bUseCloud;
    }

    public boolean toggleUseCloud()
    {
        bUseCloud = !bUseCloud;
        if(bUseCloud)
        {
            td   = new Thread(cloudClient);
            td.start();
        }
        else
        {
            try {
                td.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return bUseCloud;
    }
    private CloudClientSingelton() {
        cloudClient = new CloudClient();

    }

    public static CloudClientSingelton getInstance() {
        if (instance == null) {
            synchronized(CloudClientSingelton.class) {
                if (instance == null) {
                    instance = new CloudClientSingelton();
                }
            }
        }
        return instance;
    }
}