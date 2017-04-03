package magic.core.cards;

import magic.core.cards.creatures.Abilities;
import magic.core.cards.creatures.Creature;
import magic.core.cards.lands.BasicLands;
import magic.core.cards.magics.Burn;
import magic.core.cards.magics.attachments.DamageLifeBoost;
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
            new Creature("Frodo Baggins", 2, 1,
                    List.of(BasicLands.MOUNTAIN), Collections.emptyList(),
                    Collections.emptyList()),
            new Creature("Aragorn", 5, 7,
                    List.of(BasicLands.PLAINS, BasicLands.PLAINS, BasicLands.WASTES), List.of(Abilities.PROVOKE), Collections.emptyList()),
            new Creature("Legolas", 8, 4,
                    List.of(BasicLands.PLAINS, BasicLands.PLAINS, BasicLands.WASTES), List.of(Abilities.DOUBLE_STRIKE), Collections.emptyList()),
            new Creature("Sauron", 10, 10,
                    List.of(BasicLands.SWAMP, BasicLands.SWAMP, BasicLands.SWAMP,
                            BasicLands.WASTES, BasicLands.WASTES),
                    List.of(Abilities.HASTE, Abilities.PROVOKE, Abilities.DOUBLE_STRIKE),
                    Collections.emptyList()),

            new Burn("Backstabbing", 3, List.of(BasicLands.MOUNTAIN)),
            new Burn("You shall not pass", 4, List.of(BasicLands.MOUNTAIN, BasicLands.SWAMP)),
            new Burn("Telekinesis", 3, List.of(BasicLands.ISLAND, BasicLands.WASTES)),

            new Burn("Brats with Rocks", 1, 3, List.of(BasicLands.FOREST, BasicLands.WASTES)),
            new Burn("Fire Rain", 2, 3, List.of(BasicLands.MOUNTAIN, BasicLands.MOUNTAIN)),
            new Burn("Great Flood", 4, 3, List.of(BasicLands.ISLAND, BasicLands.ISLAND)),
            new Burn("Blood Feast", 6, 3, List.of(BasicLands.MOUNTAIN, BasicLands.SWAMP)),

            new DamageLifeBoost("Sword of Dawn", 2, 0,
                    List.of(BasicLands.PLAINS)),
            new DamageLifeBoost("Heavenly Blessing", 4, 2,
                    List.of(BasicLands.PLAINS, BasicLands.PLAINS, BasicLands.WASTES)));

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
