package cs6250.benchmarkingsuite.imageprocessing.cloud;

import android.util.Log;

import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;

import cs6250.benchmarkingsuite.imageprocessing.server.IBenchProtocol;
import cs6250.benchmarkingsuite.imageprocessing.metrics.BandwidthMeasurement;

public class CloudClient implements Runnable {

    NettyTransceiver transceiver;
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
            transceiver = new NettyTransceiver(new InetSocketAddress(serverAddr, port));
            client = SpecificRequestor.getClient(IBenchProtocol.Callback.class, transceiver);
            synchronized (syncObj) {
                syncObj.notifyAll();
            }
            Log.v("CloudClient", "Connecting to server " + serverIP);
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
        } else {
            client.addIdentityEffect();
        }
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
        } else {
            client.addCartoonEffect();
        }
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
        } else {
            client.addFaceDetectionEffect();
        }
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
        } else {
            client.addMaskEffect();
        }
    }

    public void addBlurEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        } else {
            client.addBlurEffect();
        }
    }

    public void addColorSaturationEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        } else {
            client.addColorSaturationEffect();
        }
    }

    public void addMotionHistoryEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        } else {
            client.addMotionHistoryEffect();
        }
    }

    public void addNegativeEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        } else {
            client.addNegativeEffect();
        }
    }

    public void addSepiaEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        } else {
            client.addSepiaEffect();
        }
    }

    public void addVerticalEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        } else {
            client.addVerticalEffect();
        }
    }

    public void addXrayEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        } else {
            client.addXrayEffect();
        }
    }

    public void addHorizontalFlipEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        } else {
            client.addHorizontalFlipEffect();
        }
    }

    public void addHoughCircleEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        } else {
            client.addHoughCircleEffect();
        }
    }

    public void addHoughLineEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        } else {
            client.addHoughLineEffect();
        }
    }

    public void addEdgeDetectionEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        } else {
            client.addEdgeDetectionEffect();
        }
    }

    public void addGradientMagnitudeEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        } else {
            client.addGradientMagnitudeEffect();
        }
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
        } else {
            client.addMotionDetectionEffect();
        }
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
        } else {
            client.addCheckerBoardDetectionEffect();
        }
    }

    public void addFrames(List<ByteBuffer> frames, org.apache.avro.ipc.Callback<List<ByteBuffer>> callback) throws java.io.IOException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        } else {
            client.addFrames(frames, callback);
        }
    }

    public void addCompressedFrames(java.util.List<java.nio.ByteBuffer> frames, String algorithm, org.apache.avro.ipc.Callback<java.util.List<java.nio.ByteBuffer>> callback) throws java.io.IOException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        } else {
            client.addCompressedFrames(frames, algorithm, callback);
        }
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
        } else {
            client.clearEffects();
        }
    }
}
