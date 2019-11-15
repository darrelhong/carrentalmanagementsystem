package util.exception;

public class CarNotAssignedException extends Exception {

    /**
     * Creates a new instance of <code>CarNotAssignedException</code> without
     * detail message.
     */
    public CarNotAssignedException() {
    }

    /**
     * Constructs an instance of <code>CarNotAssignedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CarNotAssignedException(String msg) {
        super(msg);
    }
}
