package util.exception;

/**
 *
 * @author darre
 */
public class RentalRecordNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>RentalRecordNotFoundException</code>
     * without detail message.
     */
    public RentalRecordNotFoundException() {
    }

    /**
     * Constructs an instance of <code>RentalRecordNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public RentalRecordNotFoundException(String msg) {
        super(msg);
    }
}
