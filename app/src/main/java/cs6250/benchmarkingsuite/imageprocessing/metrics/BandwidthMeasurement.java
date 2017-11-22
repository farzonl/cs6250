package cs6250.benchmarkingsuite.imageprocessing.metrics;

/**
 * Created by farzon on 11/17/17.
 */

import android.util.Log;
import java.io.Closeable;
import java.io.File;

public class BandwidthMeasurement {

    static String TAG = "BandwidthMeasurement";
    final BenchTimer timer = new  BenchTimer();
    String serverIpString;



    // send the message to the server in a thread
    public class ClientClass implements Runnable {

        public boolean TestBandwidth() {

            boolean bStatus = true;
            Log.i("test", "server ip is " + serverIpString);
            IperfWrapper iperf = null;
            try {

                iperf = new IperfWrapper();
                iperf
                        .newTest()
                        .tempFileTemplate(initFileTemplate())
                        .defaults()
                        .testRole(IperfWrapper.ROLE_CLIENT)
                        .hostname(serverIpString)
                        .logfile(path.getPath()+"/output.txt")
                        .outputJson(true)
                        .runClient();

                long bytes_sent     = iperf.getUploadedBytes();
                long bytes_recv     = iperf.getDownloadedBytes();
                double time_taken   = iperf.getTimeTaken();
                hostCpuUtil         = iperf.getHostCpuUtilization()[0];
                serverCpuUtil       = iperf.getServerCpuUtilization()[0];
                uploadBandwidth     = bytes_sent / ((1 << 20) * time_taken);
                downloadBandwidth   = bytes_recv / ((1 << 20) * time_taken);

            }
            catch (IperfException e) {
                Log.e("test", "Error: bandwidth test failed\n");
                e.printStackTrace();
                bStatus = false;
            }
            finally {
                iperf.freeTest();
            }

            Log.i("test", "test done\n");

            return bStatus;
        }

        @Override
        public void run() {
            if (TestBandwidth() == false) {
                Log.e(TAG, "Could not test bandwidth");
                uploadBandwidth = 0;
                downloadBandwidth = 0;
            }
        }
    }

    private double uploadBandwidth, downloadBandwidth;
    private double hostCpuUtil, serverCpuUtil;
    private Thread td;
    public double getUploadBandwidth() {
        return uploadBandwidth;
    }

    public double getDownloadBandwidth() {
        return downloadBandwidth;
    }

    public double  getHostCpuUtil()
    {
        return hostCpuUtil;
    }

    public double getServerCpuUtil() {
        return serverCpuUtil;
    }

    ClientClass myClient;

    private void suspendThread() {
        try {
            if (td != null && td.isAlive()) {
                td.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private GetPathDefaults path;
    private BandwidthMeasurement(String serverIp, GetPathDefaults path) {
        serverIpString = serverIp;
        this.path = path;
    }

    boolean m_bUseIperf = false;

    public boolean isIperfOn()
    {
        return m_bUseIperf;
    }
    public void setUseIperf(boolean bUseIperf) {
        m_bUseIperf = bUseIperf;
        if (m_bUseIperf) {
            myClient = new ClientClass();
            new File(path.getPath()+"/output.txt").delete();
            td = new Thread(myClient);
            td.start();
            myClient.TestBandwidth();
        } else {
            suspendThread();
        }
    }
    public String initFileTemplate()
    {
        File cacheDir = path.getPath();
        File tempFile = new File(cacheDir, "iperf3tempXXXXXX");
        return tempFile.getAbsolutePath();
    }

    private static volatile BandwidthMeasurement instance = null;

    public static BandwidthMeasurement init(String serverIP, GetPathDefaults path) {

        if (instance == null) {
            synchronized (BandwidthMeasurement.class) {
                if (instance == null) {
                    instance = new BandwidthMeasurement(serverIP, path);
                }
            }
        }
        return instance;
    }

    public static BandwidthMeasurement getInstance() {
        return instance;
    }
}