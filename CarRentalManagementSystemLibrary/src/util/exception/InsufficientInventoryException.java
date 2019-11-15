package util.exception;

public class InsufficientInventoryException extends Exception {

    /**
     * Creates a new instance of <code>InsufficientInventoryException</code>
     * without detail message.
     */
    public InsufficientInventoryException() {
    }

    /**
     * Constructs an instance of <code>InsufficientInventoryException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InsufficientInventoryException(String msg) {
        super(msg);
    }
}
