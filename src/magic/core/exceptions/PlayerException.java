package magic.core.exceptions;

/**
 *
 * @author ldavid
 */
public class PlayerException extends JMagicException {

    /**
     * Creates a new instance of <code>PlayerException</code> without detail
     * message.
     */
    public PlayerException() {
    }

    /**
     * Constructs an instance of <code>PlayerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PlayerException(String msg) {
        super(msg);
    }
}
