package magic.core.exceptions;

/**
 *
 * @author ldavid
 */
public class IllegalActionException extends PlayerException {

    /**
     * Creates a new instance of <code>NullActionException</code> without detail
     * message.
     */
    public IllegalActionException() {
    }

    /**
     * Constructs an instance of <code>NullActionException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public IllegalActionException(String msg) {
        super(msg);
    }
}
