package util.exception;

/**
 *
 * @author darre
 */
public class CarModelNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>CarModelNotFoundException</code> without
     * detail message.
     */
    public CarModelNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CarModelNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CarModelNotFoundException(String msg) {
        super(msg);
    }
}
