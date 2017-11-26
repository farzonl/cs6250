package cs6250.benchmarkingsuite.imageprocessing.cloud;

import android.util.Log;

import org.apache.avro.ipc.DatagramTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;

import cs6250.benchmarkingsuite.imageprocessing.server.IBenchProtocol;

/**
 * Created by farzon on 11/26/17.
 */

public class UDPCloudClient implements Runnable {
    DatagramTransceiver transceiver;
    IBenchProtocol.Callback client;
    public String serverIP;
    public int port;
    final private Object syncObj = new Object();

    //BandwidthMeasurement bandwidth;

    @Override
    public void run() {
        try {
            InitializeClient();
        } catch (IOException e) {
            Log.e("CloudClient", "Failed to initialize InitializeClient");
        }
    }

    private void InitializeClient() throws IOException {
        Log.v("CloudClient", "Initializing client");
        InetAddress serverAddr = null;

        try {
            serverAddr = InetAddress.getByName(serverIP);
            transceiver = new DatagramTransceiver(new InetSocketAddress(serverAddr, port));
            client = SpecificRequestor.getClient(IBenchProtocol.Callback.class, transceiver);
            synchronized (syncObj) {
                syncObj.notifyAll();
            }
            Log.v("UDPCloudClient", "Connecting to server " + serverIP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addGrayscaleEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        client.addGrayscaleEffect();
    }

    public void addIdentityEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        Thread effectThread = new Thread(new Runnable() {
            @Override
            public void run()  {
                try {
                    client.addIdentityEffect();
                } catch (org.apache.avro.AvroRemoteException ex) {}
            }
        });
        effectThread.start();
        try {
            effectThread.join();
        } catch(InterruptedException ie) {}
    }

    public void addCartoonEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        Thread effectThread = new Thread(new Runnable() {
            @Override
            public void run()  {
                try {
                    client.addCartoonEffect();
                } catch (org.apache.avro.AvroRemoteException ex) {}
            }
        });
        effectThread.start();
        try {
            effectThread.join();
        } catch(InterruptedException ie) {}
    }

    public void addFaceDetectionEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        Thread effectThread = new Thread(new Runnable() {
            @Override
            public void run()  {
                try {
                    client.addFaceDetectionEffect();
                } catch (org.apache.avro.AvroRemoteException ex) {}
            }
        });
        effectThread.start();
        try {
            effectThread.join();
        } catch(InterruptedException ie) {}
    }

    public void addMaskEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        Thread effectThread = new Thread(new Runnable() {
            @Override
            public void run()  {
                try {
                    client.addMaskEffect();
                } catch (org.apache.avro.AvroRemoteException ex) {}
            }
        });
        effectThread.start();
        try {
            effectThread.join();
        } catch(InterruptedException ie) {}
    }

    public void addMotionDetectionEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        Thread effectThread = new Thread(new Runnable() {
            @Override
            public void run()  {
                try {
                    client.addMotionDetectionEffect();
                } catch (org.apache.avro.AvroRemoteException ex) {}
            }
        });
        effectThread.start();
        try {
            effectThread.join();
        } catch(InterruptedException ie) {}
    }

    public void addCheckerBoardDetectionEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        Thread effectThread = new Thread(new Runnable() {
            @Override
            public void run()  {
                try {
                    client.addCheckerBoardDetectionEffect();
                } catch (org.apache.avro.AvroRemoteException ex) {}
            }
        });
        effectThread.start();
        try {
            effectThread.join();
        } catch(InterruptedException ie) {}
    }

    public void addFrames(final List<ByteBuffer> frames, final org.apache.avro.ipc.Callback<List<ByteBuffer>> callback) throws java.io.IOException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        Thread effectThread = new Thread(new Runnable() {
            @Override
            public void run()  {
                try {
                    client.addFrames(frames, callback);
                } catch (IOException ex) {}
            }
        });
        effectThread.start();
        try {
            effectThread.join();
        } catch(InterruptedException ie) {}
    }

    public void addCompressedFrames(final java.util.List<java.nio.ByteBuffer> frames, final String algorithm, final org.apache.avro.ipc.Callback<java.util.List<java.nio.ByteBuffer>> callback) throws java.io.IOException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        Thread effectThread = new Thread(new Runnable() {
            @Override
            public void run()  {
                try {
                    client.addCompressedFrames(frames, algorithm, callback);
                } catch (IOException ex) {}
            }
        });
        effectThread.start();
        try {
            effectThread.join();
        } catch(InterruptedException ie) {}
    }

    public void clearEffects() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        Thread effectThread = new Thread(new Runnable() {
            @Override
            public void run()  {
                try {
                    client.clearEffects();
                } catch (org.apache.avro.AvroRemoteException ex) {}
            }
        });
        effectThread.start();
        try {
            effectThread.join();
        } catch(InterruptedException ie) {}
    }
}

