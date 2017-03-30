package magic.core.cards;

import magic.core.cards.creatures.Abilities;
import magic.core.cards.creatures.Creature;
import magic.core.cards.magics.AreaBurn;
import magic.core.cards.magics.Burn;
import magic.core.contracts.ICard;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Deck Builder.
 * <p>
 * Build random decks for players.
 *
 * @author ldavid
 */
public class DeckBuilder {

    public static final List<ICard> AVAILABLE_CARDS = List.of(
            new Creature("Frodo Baggins", 2, 1, 1, Collections.emptyList(), Collections.emptyList()),
            new Creature("Aragorn", 5, 7, 6, List.of(Abilities.PROVOKE), Collections.emptyList()),
            new Creature("Legolas", 8, 4, 6, List.of(Abilities.DOUBLE_STRIKE), Collections.emptyList()),
            new Creature("Sauron", 10, 10, 10, List.of(Abilities.HASTE, Abilities.PROVOKE, Abilities.DOUBLE_STRIKE), Collections.emptyList()),

            new Burn("Backstabbing", 3, 1),
            new Burn("You shall not pass", 4, 4),
            new Burn("Telekinesis", 3, 2),

            new AreaBurn("Brats with Rocks", 1, 1),
            new AreaBurn("Fire Rain", 2, 2),
            new AreaBurn("Great Flood", 4, 3),
            new AreaBurn("Blood Feast", 6, 4)
    );

    private final int numberOfCards;
    private final Random random;
    private final List<ICard> possibleCards;

    public DeckBuilder(int numberOfCards, Random random) {
        this(numberOfCards, random, AVAILABLE_CARDS);
    }

    public DeckBuilder(int numberOfCards, Random random, List<ICard> possibleCards) {
        this.numberOfCards = numberOfCards;
        this.random = random;
        this.possibleCards = possibleCards;
    }

    public Cards random() {
        List<Integer> cardIndices = IntStream.range(0, possibleCards.size())
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(cardIndices, random);

        return new Cards(cardIndices
                .subList(0, numberOfCards).stream()
                .map(i -> possibleCards.get(i).duplicate())
                .collect(Collectors.toList()));
    }
}
