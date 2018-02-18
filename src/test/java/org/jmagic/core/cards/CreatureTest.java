package org.jmagic.core.cards;

import org.jmagic.core.cards.attachments.Boost;
import org.jmagic.core.cards.attachments.IAttachment;
import org.jmagic.core.cards.lands.BasicLands;
import org.jmagic.infrastructure.IDamageable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class CreatureTest {
    private Creature a;

    @BeforeEach
    void beforeEach() {
        this.a = new Creature("mocked-creature-1", 1, 10,
            List.of(BasicLands.SWAMP));
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
    void takeDamage1() {
        Boost b = new Boost("test-boost", 3, 5, List.of(BasicLands.PLAINS));
        Creature c = new Creature(null, "Joe", 4, 10, 12,
            true, List.of(BasicLands.PLAINS), List.of(Properties.HASTE),
            List.of(b));

        final int originalLife = a.life(),
            originalEffectiveLife = a.effectiveLife();

        IDamageable d = a.takeDamage(c);

        assertEquals(originalLife, a.life());
        assertEquals(originalEffectiveLife, a.effectiveLife());
        assertEquals(originalLife - 7, d.life());
        assertEquals(originalEffectiveLife - 7, d.effectiveLife());
    }

    @Test
    void takeDeathTouch() {
        Boost b = new Boost("test-boost", 3, 5, List.of(BasicLands.PLAINS));
        Creature c = new Creature(null, "Joe", 4, 10, 12,
            true, List.of(BasicLands.PLAINS), List.of(Properties.HASTE, Properties.DEATH_TOUCH),
            List.of(b));

        final int originalLife = a.life(),
            originalEffectiveLife = a.effectiveLife();

        IDamageable d = a.takeDamage(c);

        assertEquals(originalLife, a.life());
        assertEquals(originalEffectiveLife, a.effectiveLife());
        assertEquals(originalLife - originalEffectiveLife, d.life());
        assertEquals(0, d.effectiveLife());
    }

    @Test
    void attach() {
        int initialDamage = a.damage(),
            initialEffectiveDamage = a.effectiveDamage(),
            initialLife = a.life(),
            initialEffectiveLife = a.effectiveLife();

        Boost attachment = new Boost("mocked-attachment-1",
            3, 2, List.of(BasicLands.FOREST));

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
        Boost attachment = new Boost("mocked-attachment-1",
            3, 2, List.of(BasicLands.MOUNTAIN));
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
        assertEquals(initialEffectiveDamage - 3, b.effectiveDamage());
        assertEquals(initialLife, b.life());
        assertEquals(initialEffectiveLife - 2, b.effectiveLife());
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

    @Test
    void duplicate() {
        ICard b = a.duplicate();

        assertNotEquals(a, b);
        assertNotEquals(a.id(), b.id());
    }

    @Test
    void testBuffsAreImmutable() {
        Creature creature = new Creature("Joe", 10, 4,
                List.of(BasicLands.PLAINS), List.of(Properties.HASTE));

        Collection<Properties> buffs = creature.properties();

        buffs.add(Properties.PROVOKE);
        buffs.add(Properties.DOUBLE_STRIKE);

        assertEquals(creature.properties().size(), 1);
        assertTrue(creature.properties().contains(Properties.HASTE));
    }

    @Test
    void testLackeyWithAttachments() {
        Boost a = new Boost("mocked-damage", 2, 0,
                List.of(BasicLands.PLAINS));

        Creature l = new Creature("Joe", 53, 31,
                List.of(BasicLands.PLAINS), Collections.emptyList(), List.of(a));

        assertEquals(53 + 2, l.effectiveDamage());
        assertEquals(31 + 0, l.effectiveLife());
        assertEquals(31 + 0, l.effectiveLife());
    }

    @Test
    void life() {
        Boost b = new Boost("test-boost", 3, 5, List.of(BasicLands.PLAINS));
        Creature c = new Creature(null, "Joe", 4, 10, 12,
            false, List.of(BasicLands.PLAINS), List.of(Properties.HASTE),
            List.of(b));

        assertEquals(10, c.life());
    }

    @Test
    void originalLife() {
        Boost b = new Boost("test-boost", 3, 5, List.of(BasicLands.PLAINS));
        Creature c = new Creature(null, "Joe", 4, 10, 12,
            false, List.of(BasicLands.PLAINS), List.of(Properties.HASTE),
            List.of(b));

        assertEquals(12, c.originalLife());
    }

    @Test
    void effectiveLife() {
        Boost b = new Boost("test-boost", 3, 5, List.of(BasicLands.PLAINS));
        Creature c = new Creature(null, "Joe", 4, 10, 12,
            false, List.of(BasicLands.PLAINS), List.of(Properties.HASTE),
            List.of(b));

        assertEquals(15, c.effectiveLife());
    }

    @Test
    void effectiveOriginalLife() {
        Boost b = new Boost("test-boost", 3, 5, List.of(BasicLands.PLAINS));
        Creature c = new Creature(null, "Joe", 4, 10, 12,
            false, List.of(BasicLands.PLAINS), List.of(Properties.HASTE),
            List.of(b));

        assertEquals(17, c.effectiveOriginalLife());
    }

    @Test
    void die() {
        Boost b = new Boost("test-boost", 3, 5, List.of(BasicLands.PLAINS));
        Creature c = new Creature(null, "Joe", 4, 10, 12,
            false, List.of(BasicLands.PLAINS), List.of(Properties.HASTE),
            List.of(b));

        Creature d = c.die();
        assertTrue(d.effectiveLife() <= 0);
    }

    @Test
    void attached() {
        Boost b = new Boost("test-boost", 3, 5, List.of(BasicLands.PLAINS));
        Creature c = new Creature(null, "Joe", 4, 10, 12,
            false, List.of(BasicLands.PLAINS), List.of(Properties.HASTE),
            List.of(b));

        assertTrue(c.attached(b));
        assertFalse(c.attached(new Boost("test-unattached-boost", 10, 10,
            List.of(BasicLands.PLAINS))));
    }

    @Test
    void tap() {
        Boost b = new Boost("test-boost", 3, 5, List.of(BasicLands.PLAINS));
        Creature c = new Creature(null, "Joe", 4, 10, 12,
            false, List.of(BasicLands.PLAINS), List.of(Properties.HASTE),
            List.of(b));

        assertFalse(c.tapped());
        assertTrue(c.tap().tapped());
    }

    @Test
    void untap() {
        Boost b = new Boost("test-boost", 3, 5, List.of(BasicLands.PLAINS));
        Creature c = new Creature(null, "Joe", 4, 10, 12,
            true, List.of(BasicLands.PLAINS), List.of(Properties.HASTE),
            List.of(b));

        assertTrue(c.tapped());
        assertFalse(c.untap().tapped());
    }

    @Test
    void tapped() {
        Boost b = new Boost("test-boost", 3, 5, List.of(BasicLands.PLAINS));
        Creature c = new Creature(null, "Joe", 4, 10, 12,
            true, List.of(BasicLands.PLAINS), List.of(Properties.HASTE),
            List.of(b));
        assertTrue(c.tapped());

        c = new Creature(null, "Joe", 4, 10, 12,
            false, List.of(BasicLands.PLAINS), List.of(Properties.HASTE),
            List.of(b));
        assertFalse(c.tapped());
    }

    @Test
    void effectiveDamage() {
        Boost b = new Boost("test-boost", 3, 5, List.of(BasicLands.PLAINS));
        Creature c = new Creature(null, "Joe", 4, 10, 12,
            true, List.of(BasicLands.PLAINS), List.of(Properties.HASTE),
            List.of(b));

        assertEquals(7, c.effectiveDamage());
    }

    @Test
    void effectiveProperties() {
        Boost b = new Boost("test-boost", 3, 5, List.of(BasicLands.PLAINS));
        Creature c = new Creature(null, "Joe", 4, 10, 12,
            true, List.of(BasicLands.PLAINS), List.of(Properties.HASTE, Properties.DEATH_TOUCH),
            List.of(b));

        assertEquals(List.of(Properties.HASTE, Properties.DEATH_TOUCH),
            c.effectiveProperties());
    }

    @Test
    void attachments() {
        Boost b = new Boost("test-boost", 3, 5, List.of(BasicLands.PLAINS));
        Creature c = new Creature(null, "Joe", 4, 10, 12,
            true, List.of(BasicLands.PLAINS), List.of(Properties.HASTE, Properties.DEATH_TOUCH),
            List.of(b));

        Collection<IAttachment> attachments = c.attachments();
        assertEquals(List.of(b), attachments);
    }

    @Test
    void testToString() {
        Boost b = new Boost("test-boost", 3, 5, List.of(BasicLands.PLAINS));
        Creature c = new Creature(null, "Joe", 4, 10, 12,
            true, List.of(BasicLands.PLAINS), List.of(Properties.HASTE, Properties.DEATH_TOUCH),
            List.of(b));

        assertEquals("\"Joe\" d:7 l:15/17", c.toString());
        assertEquals("\"Joe\" c:[PLAINS] p:[HASTE, DEATH_TOUCH] d:7 l:15/17 a:[\"test-boost\" d+:3 l+: 5]",
            c.toString(true));

        c = new Creature("Joe", 4, 10, List.of(BasicLands.PLAINS));
        assertEquals("\"Joe\" d:4 l:10", c.toString());
        assertEquals("\"Joe\" c:[PLAINS] d:4 l:10", c.toString(true));
    }

}
