package magic.core.cards;

import magic.core.cards.creatures.Creature;
import magic.core.cards.creatures.Abilities;
import magic.core.cards.magics.attachments.DamageLifeBoost;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author ldavid
 */
public class CreatureTest {

    public CreatureTest() {
    }

    @Test
    public void testDefaultLackeys() {
        assertNotNull(Creature.SAMPLES);
        assertFalse(Creature.SAMPLES.isEmpty());
    }

    @Test
    public void testToString() {
        System.out.println(Creature.SAMPLES.stream()
                .findFirst()
                .orElse(null));
    }

    @Test
    public void testBuffsAreImmutable() {
        Creature creature = new Creature("Joe", 10, 4, 5, Arrays.asList(Abilities.HASTE),
                Collections.emptyList());

        Collection<Abilities> buffs = creature.getAbilities();
        buffs.add(Abilities.PROVOKE);
        buffs.add(Abilities.DOUBLE_STRIKE);

        assertEquals(creature.getAbilities().size(), 1);
        assertTrue(creature.getAbilities().contains(Abilities.HASTE));
    }

    @Test
    public void testLackeyWithAttachments() {
        DamageLifeBoost a = DamageLifeBoost.DEFAULT_CARDS.stream()
                .findFirst()
                .orElse(null);


        assertEquals(2, a.damageIncrease());
        assertEquals(0, a.lifeIncrease());

        Creature l = new Creature("Joe", 53, 31, 1, Collections.emptyList(), Arrays.asList(a));

        assertEquals(53 + 2, l.effectiveDamage());
        assertEquals(31 + 0, l.effectiveLife());
        assertEquals(31 + 0, l.effectiveLife());
    }
}
