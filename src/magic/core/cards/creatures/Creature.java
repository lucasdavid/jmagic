package magic.core.cards.creatures;

import magic.core.IDamageable;
import magic.core.cards.Harmful;
import magic.core.cards.ICard;
import magic.core.cards.ITappable;
import magic.core.cards.Properties;
import magic.core.cards.lands.BasicLands;
import magic.core.cards.magics.attachments.IAttachable;
import magic.core.cards.magics.attachments.IAttachment;
import magic.core.cards.magics.attachments.IDamageBoost;
import magic.core.cards.magics.attachments.ILifeBoost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

public class Creature extends Harmful implements IDamageable, IAttachable, ITappable {

    private final int life;
    private final int maxLife;
    private final boolean tapped;
    private final Collection<IAttachment> attachments;

    public Creature(String name, int damage, int life,
                    Collection<BasicLands> cost) {
        this(name, damage, life, cost, Collections.emptySet());
    }

    public Creature(String name, int damage, int life,
                    Collection<BasicLands> cost,
                    Collection<Properties> properties) {
        this(name, damage, life, cost, properties, Collections.emptySet());
    }

    public Creature(String name, int damage, int life,
                    Collection<BasicLands> cost,
                    Collection<Properties> properties,
                    Collection<IAttachment> attachments) {
        this(UUID.randomUUID(), name, damage, life, life, false, cost, properties, attachments);
    }

    public Creature(UUID id, String name, int damage, int life, int maxLife,
                    boolean tapped,
                    Collection<BasicLands> cost,
                    Collection<Properties> properties,
                    Collection<IAttachment> attachments) {
        super(id, name, damage, properties, cost);
        this.life = life;
        this.maxLife = maxLife;
        this.tapped = tapped;
        this.attachments = attachments;
    }

    @Override
    public int life() {
        return life;
    }

    @Override
    public int maxLife() {
        return maxLife;
    }

    /**
     * @return the base life plus the increase generated by the attachments.
     */
    @Override
    public int effectiveLife() {
        return life() + attachments.stream()
            .filter(a -> a instanceof ILifeBoost)
            .mapToInt(a -> ((ILifeBoost) a).lifeIncrease())
            .sum();
    }

    /**
     * @return the base max-life plus the increase generated by the attachments.
     */
    @Override
    public int effectiveMaxLife() {
        return maxLife() + attachments.stream()
            .filter(a -> a instanceof ILifeBoost)
            .mapToInt(a -> ((ILifeBoost) a).lifeIncrease())
            .sum();
    }

    @Override
    public IDamageable takeDamage(int damage) {
        return new Creature(id(), name(), damage(), life - damage, maxLife,
            tapped, cost(), properties(), attachments);
    }

    @Override
    public boolean isAlive() {
        return effectiveLife() > 0;
    }

    @Override
    public IAttachable attach(IAttachment attachment) {
        Collection<IAttachment> attachments = attachments();
        attachments.add(attachment);
        return new Creature(id(), name(), damage(), life, maxLife,
            tapped, cost(), properties(), attachments);
    }

    @Override
    public IAttachable detach(IAttachment attachment) {
        Collection<IAttachment> attachments = attachments();
        attachments.remove(attachment);

        return new Creature(id(), name(), damage(), life, maxLife, tapped,
            cost(), properties(), attachments);
    }

    @Override
    public ITappable tap() {
        return new Creature(id(), name(), damage(), life, maxLife,
            true, cost(), properties(), attachments);
    }

    @Override
    public ITappable untap() {
        return new Creature(id(), name(), damage(), life, maxLife,
            false, cost(), properties(), attachments);
    }

    @Override
    public boolean tapped() {
        return tapped;
    }

    @Override
    public int effectiveDamage() {
        return super.effectiveDamage() + attachments.stream()
            .filter(a -> a instanceof IDamageBoost)
            .mapToInt(a -> ((IDamageBoost) a).damageIncrease())
            .sum();
    }

    @Override
    public Collection<Properties> effectiveProperties() {
        Collection<Properties> properties = properties();
        properties.addAll(attachments.stream()
            .flatMap(a -> a.properties().stream())
            .collect(Collectors.toList()));
        return properties;
    }

    public Collection<IAttachment> attachments() {
        return new ArrayList<>(attachments);
    }

    @Override
    public ICard duplicate() {
        return new Creature(UUID.randomUUID(), name(), damage(), life, maxLife,
            tapped, cost(), properties(), attachments);
    }

    @Override
    public String toString(final boolean detailed) {
        String description
            = super.toString(detailed)
            + (life == maxLife
            ? String.format(" d:%d l:%d", effectiveDamage(), effectiveLife())
            : String.format(" d:%d l:%d/%d", effectiveDamage(), effectiveLife(), effectiveMaxLife()));

        if (detailed) {
            if (!properties().isEmpty()) {
                description += String.format(" p:{%s}", properties());
            }

            if (!attachments.isEmpty()) {
                description += String.format(" a:{%s}", attachments);
            }
        }

        return description;
    }
}
