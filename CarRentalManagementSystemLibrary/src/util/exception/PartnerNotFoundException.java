package util.exception;

/**
 *
 * @author darre
 */
public class PartnerNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>PartnerNotFoundException</code> without
     * detail message.
     */
    public PartnerNotFoundException() {
    }

    /**
     * Constructs an instance of <code>PartnerNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PartnerNotFoundException(String msg) {
        super(msg);
    }
}
