package magic.core.exceptions;

/**
 * @author ldavid
 */
public class IllegalCardUsageException extends PlayerException {

    /**
     * Constructs an instance of <code>IllegalCardUsageException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public IllegalCardUsageException(String msg) {
        super(msg);
    }
}
