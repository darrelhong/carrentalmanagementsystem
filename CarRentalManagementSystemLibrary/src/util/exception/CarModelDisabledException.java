package util.exception;

/**
 *
 * @author darre
 */
public class CarModelDisabledException extends Exception {

    /**
     * Creates a new instance of <code>CarModelDisabledException</code> without
     * detail message.
     */
    public CarModelDisabledException() {
    }

    /**
     * Constructs an instance of <code>CarModelDisabledException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CarModelDisabledException(String msg) {
        super(msg);
    }
}
