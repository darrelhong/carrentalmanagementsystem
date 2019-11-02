package util.exception;

/**
 *
 * @author darre
 */
public class OutletClosedException extends Exception {

    /**
     * Creates a new instance of <code>OutletClosedException</code> without
     * detail message.
     */
    public OutletClosedException() {
    }

    /**
     * Constructs an instance of <code>OutletClosedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public OutletClosedException(String msg) {
        super(msg);
    }
}
