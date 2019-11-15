package util.exception;

public class CarCategoryNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>CarCategoryNotFoundException</code>
     * without detail message.
     */
    public CarCategoryNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CarCategoryNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CarCategoryNotFoundException(String msg) {
        super(msg);
    }
}
