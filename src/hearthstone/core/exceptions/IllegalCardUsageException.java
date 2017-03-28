package hearthstone.core.exceptions;

/**
 *
 * @author ldavid
 */
public class IllegalCardUsageException extends PlayerException {

    /**
     * Creates a new instance of <code>NullActionException</code> without detail
     * message.
     */
    public IllegalCardUsageException() {
    }

    /**
     * Constructs an instance of <code>NullActionException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public IllegalCardUsageException(String msg) {
        super(msg);
    }
}
