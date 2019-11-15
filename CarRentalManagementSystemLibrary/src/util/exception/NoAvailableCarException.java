package util.exception;

public class NoAvailableCarException extends Exception {

    /**
     * Creates a new instance of <code>NoAvailableCarException</code> without
     * detail message.
     */
    public NoAvailableCarException() {
    }

    /**
     * Constructs an instance of <code>NoAvailableCarException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoAvailableCarException(String msg) {
        super(msg);
    }
}
