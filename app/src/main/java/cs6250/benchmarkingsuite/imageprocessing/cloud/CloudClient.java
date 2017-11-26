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
        }
        client.addIdentityEffect();
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
        client.addCartoonEffect();
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
        client.addFaceDetectionEffect();
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
        client.addMaskEffect();
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
        }
        client.addBlurEffect();
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
        }
        client.addColorSaturationEffect();
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
        }
        client.addMotionHistoryEffect();
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
        }
        client.addNegativeEffect();
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
        }
        client.addSepiaEffect();
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
        }
        client.addVerticalEffect();
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
        }
        client.addXrayEffect();
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
        }
        client.addHorizontalFlipEffect();
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
        }
        client.addHoughCircleEffect();
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
        }
        client.addHoughLineEffect();
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
        }
        client.addEdgeDetectionEffect();
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
        }
        client.addGradientMagnitudeEffect();
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
        }
        client.addFrames(frames, callback);
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
        }
        client.addCompressedFrames(frames, algorithm, callback);
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
        client.clearEffects();
    }


    public void addMouthDetectionEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        client.addMouthDetectionEffect();
    }

    public void addNoseDetectionEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        client.addNoseDetectionEffect();
    }

    public void addEyeDetectionEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        client.addEyeDetectionEffect();
    }

    public void addCarDetectionEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        client.addCarDetectionEffect();
    }

    public void addCatDetectionEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        client.addCatDetectionEffect();
    }

    public void addFaceFeatureDetectionEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        client.addFaceFeatureDetectionEffect();
    }

    public void addFaceLandmarksDetectionEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        client.addFaceLandmarksDetectionEffect();
    }


    public void addFaceSwapEffect() throws org.apache.avro.AvroRemoteException {
        if (client == null) {
            try {
                synchronized (syncObj) {
                    syncObj.wait();
                }
            } catch (InterruptedException e) {
                Log.e("CloudClient", e.getMessage(), e);
            }
        }
        client.addFaceSwapEffect();
    }

}
