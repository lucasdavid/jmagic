package org.jmagic.infrastructure.exceptions;

/**
 * JMagic Exception Base.
 * <p>
 * Base class for exceptions thrown by the game.
 *
 * @author ldavid
 */
public class JMagicException extends Exception {

    /**
     * Constructs an instance of <code>JMagicException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public JMagicException(String msg) {
        super(msg);
    }
}
