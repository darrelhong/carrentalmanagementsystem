package util.exception;

public class TransitDispatchRecordNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>TransitDispatchRecordNotFound</code>
     * without detail message.
     */
    public TransitDispatchRecordNotFoundException() {
    }

    /**
     * Constructs an instance of <code>TransitDispatchRecordNotFound</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public TransitDispatchRecordNotFoundException(String msg) {
        super(msg);
    }
}
