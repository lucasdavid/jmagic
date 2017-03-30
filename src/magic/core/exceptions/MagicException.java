package magic.core.exceptions;

/**
 *
 * @author ldavid
 */
public class MagicException extends Exception {

    /**
     * Creates a new instance of <code>MagicException</code> without
     * detail message.
     */
    public MagicException() {
    }

    /**
     * Constructs an instance of <code>MagicException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MagicException(String msg) {
        super(msg);
    }
}
