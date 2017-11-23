package cs6250.benchmarkingsuite.imageprocessing.metrics;

import java.io.Closeable;

/**
 * Created by farzon on 11/22/17.
 */

public class IperfWrapper implements Closeable {

    static {
        System.loadLibrary("iperf");
    }

    public static final char ROLE_SERVER = 's';
    public static final char ROLE_CLIENT = 'c';

    private native boolean initNativeIperfImpl();
    private native void cleanUp();
    private native void testRoleImpl(char role);
    private native void defaultsImpl();
    private native void hostnameImpl(String host);
    private native void tempFileTemplateImpl(String template);
    private native void durationImpl(int duration);
    private native void logFileImpl(String logfile);
    private native void runClientImpl();
    private native void outputJsonImpl(boolean useJson);

    public native long getUploadedBytes();
    public native long getDownloadedBytes();
    public native double getTimeTaken();
    public native double[] getHostCpuUtilization();
    public native double[] getServerCpuUtilization();

    public IperfWrapper initNativeIperf() throws IperfException {

        boolean didSucceed = initNativeIperfImpl();
        if (!didSucceed) {
            throw new IperfException("Failed to initialize test");
        }
        return this;
    }

    @Override
    public void close() {
            cleanUp();
    }

    public IperfWrapper testRole(char role) {
        testRoleImpl(role);
        return this;
    }

    public IperfWrapper defaults() {
        defaultsImpl();
        return this;
    }

    public IperfWrapper hostname(String host) {
        hostnameImpl(host);
        return this;
    }

    public IperfWrapper tempFileTemplate(String template) {
        tempFileTemplateImpl(template);
        return this;
    }

    public IperfWrapper durationInSeconds(int duration) {
        durationImpl(duration);
        return this;
    }

    public IperfWrapper logfile(String file) {
        logFileImpl(file);
        return this;
    }

    public IperfWrapper runClient() {
        runClientImpl();
        return this;
    }

    public IperfWrapper outputJson(boolean useJson) {
        outputJsonImpl(useJson);
        return this;
    }
}
