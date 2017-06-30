package org.games.jmagic.infrastructure.exceptions;

/**
 * JMagic Exception Base.
 * <p>
 * Base class for exceptions thrown by the game.
 *
 * @author ldavid
 */
public class JMagicException extends Exception {

    /**
     * Creates a new instance of <code>JMagicException</code> without
     * detail message.
     */
    public JMagicException() {
    }

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
