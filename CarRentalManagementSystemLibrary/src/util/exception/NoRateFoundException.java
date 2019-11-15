package util.exception;

public class NoRateFoundException extends Exception {

    /**
     * Creates a new instance of <code>NoRateFoundException</code> without
     * detail message.
     */
    public NoRateFoundException() {
    }

    /**
     * Constructs an instance of <code>NoRateFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoRateFoundException(String msg) {
        super(msg);
    }
}
