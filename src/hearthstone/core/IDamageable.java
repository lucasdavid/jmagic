package hearthstone.core;

/**
 * Damageable Interface.
 *
 * @author ldavid
 */
public interface IDamageable extends ITargetable {

    IDamageable takeDamage(int damage);
}
