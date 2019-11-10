package util.exception;

/**
 *
 * @author darre
 */
public class TransitNotAssignedException extends Exception {

    /**
     * Creates a new instance of <code>TransitNotAssignedException</code>
     * without detail message.
     */
    public TransitNotAssignedException() {
    }

    /**
     * Constructs an instance of <code>TransitNotAssignedException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public TransitNotAssignedException(String msg) {
        super(msg);
    }
}
