package magic.core.cards.creatures;

import magic.core.cards.magics.attachments.DamageLifeBoost;
import magic.core.contracts.ICard;
import magic.core.contracts.IDamageable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CreatureTest {
    private Creature a;

    @BeforeEach
    void beforeEach() {
        this.a = new Creature("mocked-creature-1", 1, 10, 1,
                Collections.emptyList(), Collections.emptyList());
    }

    @Test
    void takeDamage() {
        final int originalLife = a.life(),
                originalEffectiveLife = a.effectiveLife();

        final int expectedDamage = 4;

        IDamageable b = a.takeDamage(expectedDamage);

        assertEquals(originalLife, a.life());
        assertEquals(originalEffectiveLife, a.effectiveLife());
        assertEquals(originalLife - expectedDamage, b.life());
        assertEquals(originalEffectiveLife - expectedDamage, b.effectiveLife());
    }

    @Test
    void attach() {
        int initialDamage = a.damage(),
                initialEffectiveDamage = a.effectiveDamage(),
                initialLife = a.life(),
                initialEffectiveLife = a.effectiveLife();

        DamageLifeBoost attachment = new DamageLifeBoost("mocked-attachment-1",
                3, 2, 1);

        Creature b = (Creature) a.attach(attachment);

        // Asserts immutability.
        assertEquals(initialDamage, a.damage());
        assertEquals(initialEffectiveDamage, a.effectiveDamage());
        assertEquals(initialLife, a.life());
        assertEquals(initialEffectiveLife, a.effectiveLife());

        assertEquals(initialDamage, b.damage());
        assertEquals(initialDamage + 3, b.effectiveDamage());
        assertEquals(initialLife, b.life());
        assertEquals(initialLife + 2, b.effectiveLife());
    }

    @Test
    void detach() {
        DamageLifeBoost attachment = new DamageLifeBoost("mocked-attachment-1",
                3, 2, 1);
        a = (Creature) a.attach(attachment);

        int initialDamage = a.damage(),
                initialEffectiveDamage = a.effectiveDamage(),
                initialLife = a.life(),
                initialEffectiveLife = a.effectiveLife();

        Creature b = (Creature) a.detach(attachment);

        // Asserts immutability.
        assertEquals(initialDamage, a.damage());
        assertEquals(initialEffectiveDamage, a.effectiveDamage());
        assertEquals(initialLife, a.life());
        assertEquals(initialEffectiveLife, a.effectiveLife());

        assertEquals(initialDamage, b.damage());
        assertEquals(initialDamage - 3, b.effectiveDamage());
        assertEquals(initialLife, b.life());
        assertEquals(initialLife - 2, b.effectiveLife());
    }

    @Test
    void isAlive() {
        IDamageable b;

        b = a.takeDamage(4);

        assertTrue(a.isAlive());
        assertTrue(b.isAlive());

        b = a.takeDamage(11);

        assertTrue(a.isAlive());
        assertFalse(b.isAlive());
    }

    /**
     * Check for immutability.
     */
    @Test
    void getAbilities() {
        Collection<Abilities> abilities = a.getAbilities();

        abilities.add(Abilities.DEATH_TOUCH);
        abilities.add(Abilities.LIFE_LINK);
        abilities.add(Abilities.FEAR);

        assertEquals(abilities.size() - 3, a.getAbilities().size());
    }

    @Test
    void duplicate() {
        ICard b = a.duplicate();

        assertNotEquals(a, b);
        assertNotEquals(a.id(), b.id());
    }
}
