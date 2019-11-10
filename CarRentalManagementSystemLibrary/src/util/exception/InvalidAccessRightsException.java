package util.exception;

/**
 *
 * @author darre
 */
public class InvalidAccessRightsException extends Exception {

    /**
     * Creates a new instance of <code>InvalidAccessRightsException</code>
     * without detail message.
     */
    public InvalidAccessRightsException() {
    }

    /**
     * Constructs an instance of <code>InvalidAccessRightsException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidAccessRightsException(String msg) {
        super(msg);
    }
}
