package magic.core.cards;

import magic.core.cards.creatures.Creature;
import magic.core.cards.lands.BasicLands;
import magic.core.cards.magics.attachments.Boost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
        this.creature = new Creature(UUID.randomUUID(), "mocked-creature", 3, 2, 3, false,
            List.of(BasicLands.FOREST), Collections.emptySet(), Collections.emptySet());
    }

    @Test
    public void testToString() {
        System.out.println(creature);
    }

    @Test
    public void testBuffsAreImmutable() {
        Creature creature = new Creature("Joe", 10, 4,
            List.of(BasicLands.PLAINS), List.of(Properties.HASTE));

        Collection<Properties> buffs = creature.properties();

        buffs.add(Properties.PROVOKE);
        buffs.add(Properties.DOUBLE_STRIKE);

        assertEquals(creature.properties().size(), 1);
        assertTrue(creature.properties().contains(Properties.HASTE));
    }

    @Test
    public void testLackeyWithAttachments() {
        Boost a = new Boost("mocked-damage", 2, 0,
            List.of(BasicLands.PLAINS));

        Creature l = new Creature("Joe", 53, 31,
            List.of(BasicLands.PLAINS), Collections.emptyList(), List.of(a));

        assertEquals(53 + 2, l.effectiveDamage());
        assertEquals(31 + 0, l.effectiveLife());
        assertEquals(31 + 0, l.effectiveLife());
    }
}
