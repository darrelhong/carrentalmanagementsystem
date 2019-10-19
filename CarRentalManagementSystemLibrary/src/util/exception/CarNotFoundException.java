package util.exception;

/**
 *
 * @author darre
 */
public class CarNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>CarNotFoundException</code> without
     * detail message.
     */
    public CarNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CarNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CarNotFoundException(String msg) {
        super(msg);
    }
}
