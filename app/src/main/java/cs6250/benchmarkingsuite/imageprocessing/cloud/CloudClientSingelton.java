package cs6250.benchmarkingsuite.imageprocessing.cloud;

/**
 * Created by farzon on 10/14/17.
 */

public final class CloudClientSingelton {
    private static volatile CloudClientSingelton instance = null;
    public CloudClient cloudClient;
    private boolean m_bUseCloud = false;
    private Thread td;

    public String getIPAddress()
    {
        if(cloudClient == null)
        {
            return "";
        }
        return cloudClient.serverIP;
    }

    public int getPortNumber()
    {
        if(cloudClient == null)
        {
            return -1;
        }
        return cloudClient.port;
    }
    private void suspendThread()
    {
        try {
            if (td != null && td.isAlive()) {
                td.join();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setServerIPandPort(String serverIP, int port)
    {
        suspendThread();
        setServerIP(serverIP, false);
        setServerPort(port, false);
        setUseCloud(m_bUseCloud);
    }

    public void setServerIP(String serverIP)
    {
        setServerIP(serverIP, true);
    }

    private void setServerIP(String serverIP, boolean bsuspendThread)
    {
        if(serverIP == cloudClient.serverIP)
        {
            return;
        }

        if(bsuspendThread)
        {
            suspendThread();
        }
        cloudClient.serverIP = serverIP;

        if(bsuspendThread) {
            setUseCloud(m_bUseCloud);
        }
    }

    public void setServerPort(int serverPort)
    {
        setServerPort(serverPort, true);
    }

    private void setServerPort(int serverPort, boolean bsuspendThread)
    {
        if(serverPort == cloudClient.port)
        {
            return;
        }

        if(bsuspendThread) {
            suspendThread();
        }

        cloudClient.port = serverPort;

        if(bsuspendThread) {
            setUseCloud(m_bUseCloud);
        }
    }

    public boolean shouldUseCloud()
    {
        return m_bUseCloud;
    }

    public void setUseCloud(boolean bUseCloud)
    {
        m_bUseCloud = bUseCloud;
        if(m_bUseCloud)
        {
            td   = new Thread(cloudClient);
            td.start();
        }
        else
        {
            suspendThread();
        }

    }
    private CloudClientSingelton() {
        cloudClient = new CloudClient();

    }

    public static CloudClientSingelton getInstance(String serverIP, String port) {
        int Port = Integer.parseInt(port);
        if (instance != null) {
            instance.setServerIPandPort(serverIP, Port);
        }

        return getInstance();
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