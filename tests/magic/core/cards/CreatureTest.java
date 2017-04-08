package magic.core.cards;

import magic.core.cards.creatures.Abilities;
import magic.core.cards.creatures.Creature;
import magic.core.cards.lands.BasicLands;
import magic.core.cards.magics.attachments.DamageLifeBoost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author ldavid
 */
public class CreatureTest {

    private Creature creature;

    public CreatureTest() {
    }

    @BeforeEach
    public void setUp() {
        this.creature = new Creature("mocked-creature", 3, 2, false,
            List.of(BasicLands.FOREST),
            Collections.emptyList(),
            Collections.emptyList());
    }

    @Test
    public void testToString() {
        System.out.println(creature);
    }

    @Test
    public void testBuffsAreImmutable() {
        Creature creature = new Creature("Joe", 10, 4, false,
            List.of(BasicLands.PLAINS), List.of(Abilities.HASTE),
            Collections.emptyList());

        Collection<Abilities> buffs = creature.abilities();

        assertThrows(UnsupportedOperationException.class, () -> {
            buffs.add(Abilities.PROVOKE);
            buffs.add(Abilities.DOUBLE_STRIKE);
        });

        assertEquals(creature.abilities().size(), 1);
        assertTrue(creature.abilities().contains(Abilities.HASTE));
    }

    @Test
    public void testLackeyWithAttachments() {
        DamageLifeBoost a = new DamageLifeBoost("mocked-damage", 2, 0,
            List.of(BasicLands.PLAINS));

        Creature l = new Creature("Joe", 53, 31, false,
            List.of(BasicLands.PLAINS), Collections.emptyList(), List.of(a));

        assertEquals(53 + 2, l.effectiveDamage());
        assertEquals(31 + 0, l.effectiveLife());
        assertEquals(31 + 0, l.effectiveLife());
    }
}
