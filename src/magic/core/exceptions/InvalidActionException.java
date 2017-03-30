package magic.core.exceptions;

/**
 *
 * @author ldavid
 */
public class InvalidActionException extends PlayerException {

    /**
     * Creates a new instance of <code>NullActionException</code> without detail
     * message.
     */
    public InvalidActionException() {
    }

    /**
     * Constructs an instance of <code>NullActionException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidActionException(String msg) {
        super(msg);
    }
}
