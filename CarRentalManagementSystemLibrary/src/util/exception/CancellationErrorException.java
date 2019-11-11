package util.exception;

/**
 *
 * @author darre
 */
public class CancellationErrorException extends Exception {

    /**
     * Creates a new instance of <code>CancellationErrorException</code> without
     * detail message.
     */
    public CancellationErrorException() {
    }

    /**
     * Constructs an instance of <code>CancellationErrorException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CancellationErrorException(String msg) {
        super(msg);
    }
}
