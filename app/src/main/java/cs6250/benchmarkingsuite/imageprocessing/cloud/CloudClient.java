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

public class CloudClient implements Runnable {
	

	NettyTransceiver transceiver;
	IBenchProtocol.Callback client;
	public String serverIP;
	public int port;

	//BandwidthMeasurement bandwidth;
	
	@Override
	public void run() {
		try {
			InitializeClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void InitializeClient() throws IOException {
		
		Log.v("CloudClient", "Initializing client");
		//bandwidth = new BandwidthMeasurement(serverIP);
		
		InetAddress serverAddr = null;
		
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
		client.addGrayscaleEffect();
	}

	public void addCartoonEffect() throws org.apache.avro.AvroRemoteException {
		client.addCartoonEffect();
	}


//	public void addIdentityEffect() throws org.apache.avro.AvroRemoteException {
//		client.addIdentityEffect();
//	}

    public void addFrames(List<ByteBuffer> frames, org.apache.avro.ipc.Callback<List<ByteBuffer>> callback) throws java.io.IOException {
    	client.addFrames(frames, callback);
    }

    public void clearEffects() throws org.apache.avro.AvroRemoteException {
    	client.clearEffects();
    }

}


