package magic.core.exceptions;

/**
 *
 * @author ldavid
 */
public class PlayerActTimeoutException extends PlayerException {

    /**
     * Creates a new instance of <code>NullActionException</code> without detail
     * message.
     */
    public PlayerActTimeoutException() {
    }

    /**
     * Constructs an instance of <code>NullActionException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PlayerActTimeoutException(String msg) {
        super(msg);
    }
}
