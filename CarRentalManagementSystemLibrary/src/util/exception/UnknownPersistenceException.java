package util.exception;

public class UnknownPersistenceException extends Exception {

    /**
     * Creates a new instance of <code>UnknownPersistenceException</code>
     * without detail message.
     */
    public UnknownPersistenceException() {
    }

    /**
     * Constructs an instance of <code>UnknownPersistenceException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public UnknownPersistenceException(String msg) {
        super(msg);
    }
}
