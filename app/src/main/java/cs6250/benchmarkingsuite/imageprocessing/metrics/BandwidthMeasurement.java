package cs6250.benchmarkingsuite.imageprocessing.metrics;

/**
 * Created by farzon on 11/17/17.
 */

import android.util.Log;
import java.io.Closeable;

public class BandwidthMeasurement implements Closeable {

    static String TAG = "BandwidthMeasurement";
    final BenchTimer timer = new  BenchTimer();
    String serverIpString;

    static {
        System.loadLibrary("libiperf");
    }

    // send the message to the server in a thread
    public class ClientClass implements Runnable {

        public boolean TestBandwidth() {

            Log.i("test", "server ip is " + serverIpString);
            boolean status = launchBandwidthTest(serverIpString);

            if (status) {
                Log.e("test", "Error: bandwidth test failed\n");
                return status;
            }

            long bytes_sent     = getUploadedBytes();
            long bytes_recv     = getDownloadedBytes();
            double time_taken   = getTimeTaken();
            hostCpuUtil    = getHostCpuUtilization();
            serverCpuUtil  = getServerCpuUtilization();

            uploadBandwidth     = bytes_sent / ((1 << 20) * time_taken);
            downloadBandwidth   = bytes_recv / ((1 << 20) * time_taken);

            Log.i("test", "test done\n");

            return status;
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

    public native boolean launchBandwidthTest(String serverIp);
    public native long getUploadedBytes();
    public native long getDownloadedBytes();
    public native double getTimeTaken();
    public native long getHostCpuUtilization();
    public native long getServerCpuUtilization();
    public native boolean cleanup();

    private double uploadBandwidth, downloadBandwidth;
    private long hostCpuUtil, serverCpuUtil;
    private Thread td;
    public double getUploadBandwidth() {
        return uploadBandwidth;
    }

    public double getDownloadBandwidth() {
        return downloadBandwidth;
    }

    public long  getHostCpuUtil()
    {
        return hostCpuUtil;
    }

    public long getServerCpuUtil() {
        return serverCpuUtil;
    }

    ClientClass myClient;

    @Override
    public void close()
    {
        cleanup();
        try {
            td.join();
        } catch(InterruptedException ie)
        {

        }
    }
    public BandwidthMeasurement(String serverIp) {
        serverIpString = serverIp;
        myClient = new ClientClass();
        td = new Thread(myClient);
        td.start();
        myClient.TestBandwidth();
    }
}