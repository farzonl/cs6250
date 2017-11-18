package cs6250.benchmarkingsuite.imageprocessing.cloud;

import android.util.Log;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;

import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;

import cs6250.benchmarkingsuite.imageprocessing.server.IBenchProtocol;
import cs6250.benchmarkingsuite.imageprocessing.metrics.BandwidthMeasurement;

public class CloudClient implements Runnable {
	

	NettyTransceiver transceiver;
	IBenchProtocol.Callback client;
	public String serverIP;
	public int port;
	BandwidthMeasurement bandwidth;

	public BandwidthMeasurement getBandwidthMeasurement()
	{
		return bandwidth;
	}

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
		//bandwidth = new BandwidthMeasurement(serverIP);
		
		InetAddress serverAddr = null;
		bandwidth = new BandwidthMeasurement(serverIP);
		try {
			serverAddr  = InetAddress.getByName(serverIP);
			transceiver = new NettyTransceiver(new InetSocketAddress(serverAddr, port));
			client      = SpecificRequestor.getClient(IBenchProtocol.Callback.class, transceiver);
			Log.v("CloudClient", "Connecting to server " + serverIP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addGrayscaleEffect() throws org.apache.avro.AvroRemoteException {
		if (client != null) {
			client.addGrayscaleEffect();
		}

	}

	public void addIdentityEffect() throws org.apache.avro.AvroRemoteException {
		if (client != null) {
			client.addIdentityEffect();
		}
	}

	public void addCartoonEffect() throws org.apache.avro.AvroRemoteException {
		if (client != null) {
			client.addCartoonEffect();
		}
	}

	public void addFaceDetectionEffect() throws org.apache.avro.AvroRemoteException {
		if (client != null) {
			client.addFaceDetectionEffect();
		}
	}

	public void addMaskEffect() throws org.apache.avro.AvroRemoteException {
		if (client != null) {
			client.addMaskEffect();
		}
	}

	public void addMotionDetectionEffect() throws org.apache.avro.AvroRemoteException {
		if (client != null) {
			client.addMotionDetectionEffect();
		}
	}

	public void addCheckerBoardDetectionEffect() throws org.apache.avro.AvroRemoteException {
		if (client != null) {
			client.addCheckerBoardDetectionEffect();
		}
	}

    public void addFrames(List<ByteBuffer> frames, org.apache.avro.ipc.Callback<List<ByteBuffer>> callback) throws java.io.IOException {
		if (client != null) {
			client.addFrames(frames, callback);
		}
    }

	public void addCompressedFrames(java.util.List<java.nio.ByteBuffer> frames, String algorithm, org.apache.avro.ipc.Callback<java.util.List<java.nio.ByteBuffer>> callback) throws java.io.IOException {
		if (client != null) {
			client.addCompressedFrames(frames, algorithm, callback);
		}
	}

    public void clearEffects() throws org.apache.avro.AvroRemoteException {
		if (client != null) {
			client.clearEffects();
		}
    }
}


