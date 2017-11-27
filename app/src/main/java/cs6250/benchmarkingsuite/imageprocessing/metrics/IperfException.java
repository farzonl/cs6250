package cs6250.benchmarkingsuite.imageprocessing.metrics;

/**
 * Created by farzon on 11/22/17.
 */

public class IperfException extends Exception /*RuntimeException*/ {

    public IperfException() {
    }

    public IperfException(String message) {
        super(message);
    }

    public IperfException(String message, Throwable cause) {
        super(message, cause);
    }

    public IperfException(Throwable cause) {
        super(cause);
    }

}
