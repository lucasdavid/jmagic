package magic.core.contracts.cards;

/**
 * Tappable Interface.
 * <p>
 * Defines cards that can be tapped.
 *
 * @author ldavid
 */
public interface ITappable extends ICard {

    ITappable tap();

    ITappable untap();

    boolean tapped();
}
