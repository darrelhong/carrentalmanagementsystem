package util.exception;

public class EmployeeNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>EmployeeNotFoundException</code> without
     * detail message.
     */
    public EmployeeNotFoundException() {
    }

    /**
     * Constructs an instance of <code>EmployeeNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EmployeeNotFoundException(String msg) {
        super(msg);
    }
}
