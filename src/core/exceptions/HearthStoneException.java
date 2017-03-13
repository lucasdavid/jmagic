package core.exceptions;

/**
 *
 * @author ldavid
 */
public class HearthStoneException extends Exception {

    /**
     * Creates a new instance of <code>HearthStoneException</code> without
     * detail message.
     */
    public HearthStoneException() {
    }

    /**
     * Constructs an instance of <code>HearthStoneException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public HearthStoneException(String msg) {
        super(msg);
    }
}
