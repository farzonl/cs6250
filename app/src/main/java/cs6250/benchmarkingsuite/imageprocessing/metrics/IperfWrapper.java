package cs6250.benchmarkingsuite.imageprocessing.metrics;

/**
 * Created by farzon on 11/22/17.
 */

public class IperfWrapper {

    static {
        System.loadLibrary("iperf");
    }

    public static final char ROLE_SERVER = 's';
    public static final char ROLE_CLIENT = 'c';

    private long testRef = 0;

    private native long newTestImpl();
    private native void freeTestImpl(long ref);
    private native void testRoleImpl(long ref, char role);
    private native void defaultsImpl(long ref);
    private native void hostnameImpl(long ref, String host);
    private native void tempFileTemplateImpl(long ref, String template);
    private native void durationImpl(long ref, int duration);
    private native void logFileImpl(long ref, String logfile);
    private native void runClientImpl(long ref);
    private native void outputJsonImpl(long ref, boolean useJson);

    public native long getUploadedBytes();
    public native long getDownloadedBytes();
    public native double getTimeTaken();
    private native double[] getHostCpuUtilization(long ref);
    private native double[] getServerCpuUtilization(long ref);

    public double[] getHostCpuUtilization()
    {
     return getHostCpuUtilization(testRef);
    }

    public double[] getServerCpuUtilization()
    {
        return getServerCpuUtilization(testRef);
    }

    public IperfWrapper newTest() throws IperfException {
        if (testRef!=0) {
            freeTest();
        }
        testRef = newTestImpl();
        if (testRef==0) {
            throw new IperfException("Failed to initialize test");
        }
        return this;
    }

    public void freeTest() {
        if (testRef!=0) {
            freeTestImpl(testRef);
            testRef=0;
        }
    }

    public IperfWrapper testRole(char role) {
        testRoleImpl(testRef, role);
        return this;
    }

    public IperfWrapper defaults() {
        defaultsImpl(testRef);
        return this;
    }

    public IperfWrapper hostname(String host) {
        hostnameImpl(testRef, host);
        return this;
    }

    public IperfWrapper tempFileTemplate(String template) {
        tempFileTemplateImpl(testRef, template);
        return this;
    }

    public IperfWrapper durationInSeconds(int duration) {
        durationImpl(testRef, duration);
        return this;
    }

    public IperfWrapper logfile(String file) {
        logFileImpl(testRef, file);
        return this;
    }

    public IperfWrapper runClient() {
        runClientImpl(testRef);
        return this;
    }

    public IperfWrapper outputJson(boolean useJson) {
        outputJsonImpl(testRef, useJson);
        return this;
    }
}
