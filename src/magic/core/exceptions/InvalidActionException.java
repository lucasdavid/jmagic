package magic.core.exceptions;

/**
 *
 * @author ldavid
 */
public class InvalidActionException extends PlayerException {

    /**
     * Constructs an instance of <code>InvalidActionException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidActionException(String msg) {
        super(msg);
    }
}
