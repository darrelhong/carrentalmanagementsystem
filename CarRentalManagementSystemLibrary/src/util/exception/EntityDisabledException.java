package util.exception;

/**
 *
 * @author darre
 */
public class EntityDisabledException extends Exception {

    /**
     * Creates a new instance of <code>EntityDisabledException</code> without
     * detail message.
     */
    public EntityDisabledException() {
    }

    /**
     * Constructs an instance of <code>EntityDisabledException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EntityDisabledException(String msg) {
        super(msg);
    }
}
