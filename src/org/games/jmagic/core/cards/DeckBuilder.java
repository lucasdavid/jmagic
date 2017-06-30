package org.games.jmagic.core.cards;

import org.games.jmagic.core.cards.lands.BasicLands;
import org.games.jmagic.core.cards.lands.Land;
import org.games.jmagic.core.cards.attachments.Boost;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Deck Builder.
 * <p>
 * Build randomState decks for players.
 *
 * @author ldavid
 */
public class DeckBuilder {

    public static final List<ICard> AVAILABLE_CARDS = List.of(
        new Creature("Wyvern", 2, 1,
            List.of(BasicLands.MOUNTAIN)),
        new Creature("Miner Dwarfs", 4, 4,
            List.of(BasicLands.MOUNTAIN, BasicLands.MOUNTAIN)),

        new Creature("Grey Knight", 5, 7,
            List.of(BasicLands.PLAINS, BasicLands.PLAINS), List.of(Properties.PROVOKE)),
        new Creature("Gabriel", 8, 4,
            List.of(BasicLands.PLAINS, BasicLands.PLAINS), List.of(Properties.DOUBLE_STRIKE)),

        new Creature("Assassin", 3, 1,
            List.of(BasicLands.SWAMP, BasicLands.SWAMP)),
        new Creature("Undead Zelot", 1, 1,
            List.of(BasicLands.SWAMP)),
        new Creature("Higher Vampire", 10, 10,
            List.of(BasicLands.SWAMP, BasicLands.SWAMP, BasicLands.SWAMP, BasicLands.SWAMP, BasicLands.SWAMP),
            List.of(Properties.HASTE, Properties.PROVOKE, Properties.DOUBLE_STRIKE)),

        new Burn("Poisoning", 2, List.of(BasicLands.SWAMP)),
        new Burn("Backstabbing", 3, List.of(BasicLands.SWAMP, BasicLands.SWAMP)),
        new Burn("Black Plague", 6, List.of(BasicLands.SWAMP, BasicLands.SWAMP, BasicLands.SWAMP)),

        new Burn("Unpremeditated Earthquake", 4, List.of(BasicLands.MOUNTAIN, BasicLands.FOREST)),

        new Burn("Telekinesis", 3, List.of(BasicLands.ISLAND, BasicLands.WASTES)),

        new Burn("Brats with Rocks", 1, 3, List.of(BasicLands.FOREST, BasicLands.WASTES)),
        new Burn("Fire Rain", 2, 3, List.of(BasicLands.MOUNTAIN, BasicLands.MOUNTAIN)),
        new Burn("Great Flood", 4, 3, List.of(BasicLands.ISLAND, BasicLands.ISLAND)),
        new Burn("Blood Feast", 6, 3, List.of(BasicLands.MOUNTAIN, BasicLands.SWAMP)),

        new Boost("Sword of Dawn", 2, 0,
            List.of(BasicLands.PLAINS)),
        new Boost("Heavenly Blessing", 4, 2,
            List.of(BasicLands.PLAINS, BasicLands.PLAINS, BasicLands.WASTES)),

        new Land(BasicLands.FOREST),
        new Land(BasicLands.ISLAND),
        new Land(BasicLands.MOUNTAIN),
        new Land(BasicLands.PLAINS),
        new Land(BasicLands.SWAMP),
        new Land(BasicLands.WASTES));

    private final int numberOfCards;
    private final Random randomState;
    private final List<ICard> possibleCards;

    public DeckBuilder(int numberOfCards, Random randomState) {
        this(numberOfCards, randomState, AVAILABLE_CARDS);
    }

    public DeckBuilder(int numberOfCards, Random randomState, List<ICard> possibleCards) {
        this.numberOfCards = numberOfCards;
        this.randomState = randomState;
        this.possibleCards = possibleCards;
    }

    public Cards random() {
        return random(IntStream
            .range(0, 3)
            .mapToObj(i -> BasicLands.values()[randomState.nextInt(BasicLands.values().length)])
            .collect(Collectors.toSet()));
    }

    public Cards random(Set<BasicLands> colors) {
        List<ICard> cardsWithRightColors = possibleCards.stream()
            .filter(c -> colors.containsAll(c.cost())
                && (!(c instanceof Land) || colors.contains(((Land) c).kind())))
            .collect(Collectors.toList());

        List<Integer> cardIndices = IntStream.range(0, cardsWithRightColors.size())
            .boxed()
            .collect(Collectors.toList());

        while (cardIndices.size() < numberOfCards) {
            cardIndices.addAll(cardIndices);
        }

        Collections.shuffle(cardIndices, randomState);

        return new Cards(cardIndices
            .subList(0, numberOfCards).stream()
            .map(i -> cardsWithRightColors.get(i).duplicate())
            .collect(Collectors.toList()));
    }
}
