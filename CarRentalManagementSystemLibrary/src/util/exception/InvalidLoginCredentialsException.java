package util.exception;

public class InvalidLoginCredentialsException extends Exception {

    /**
     * Creates a new instance of <code>InvalidLoginCredentialsException</code>
     * without detail message.
     */
    public InvalidLoginCredentialsException() {
    }

    /**
     * Constructs an instance of <code>InvalidLoginCredentialsException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidLoginCredentialsException(String msg) {
        super(msg);
    }
}
