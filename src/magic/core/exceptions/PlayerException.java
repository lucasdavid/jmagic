package magic.core.exceptions;

/**
 *
 * @author ldavid
 */
public class PlayerException extends MagicException {

    /**
     * Creates a new instance of <code>NullActionException</code> without detail
     * message.
     */
    public PlayerException() {
    }

    /**
     * Constructs an instance of <code>NullActionException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PlayerException(String msg) {
        super(msg);
    }
}
